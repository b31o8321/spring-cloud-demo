package com.demo.middleware.dubbo.idempotent.filter;

import javassist.bytecode.ByteArray;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

@Setter
@Activate(group = {CommonConstants.PROVIDER})
public class IdempotentTokenCheckFilter implements Filter {
    private RedisTemplate<String, Object> redisTemplate;
    private RedissonClient redissonClient;

    @SneakyThrows
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String requestId = RpcContext.getServiceContext().getAttachment("dubbo-request-id");
        invocation.setAttachment("provider-attachment", "test");
        if (StringUtils.isNotBlank(requestId)) { // 更严格的检查
            String redisRequestKey = "dubbo:request:" + requestId;
            Object appResponseValue = redisTemplate.opsForValue().get(redisRequestKey);
            if (ObjectUtils.isNotEmpty(appResponseValue)) {
                return AsyncRpcResult.newDefaultAsyncResult(appResponseValue, invocation);
            }

            RLock lock = redissonClient.getLock("lock:dubbo:request:" + requestId);
            if (lock.tryLock()) {
                try {
                    appResponseValue = redisTemplate.opsForValue().get(redisRequestKey);
                    if (ObjectUtils.isNotEmpty(appResponseValue)) {
                        return AsyncRpcResult.newDefaultAsyncResult(appResponseValue, invocation);
                    }

                    return invoker.invoke(invocation).whenCompleteWithContext((result, throwable) -> {
                        if (throwable != null) {
                            return;
                        }
                        String luaScript = "if redis.call('get', KEYS[1]) == false then redis.call('setex', KEYS[1], ARGV[1], ARGV[2]) return 1 else return 0 end";
                        DefaultRedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);
                        redisTemplate.execute(script, Collections.singletonList(redisRequestKey), 30, result.getValue());
                    });
                } finally {
                    lock.unlock();
                }
            }
        }

        return invoker.invoke(invocation);
    }
}
