package com.demo.middleware.dubbo.idempotent.filter;

import org.apache.dubbo.rpc.*;
import com.demo.middleware.dubbo.idempotent.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;

public class IdempotentTokenInterceptor implements Filter {
    @Autowired
    private TokenService tokenService;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String token = tokenService.generateToken();
        RpcContext.getServiceContext().setAttachment("token", token);
        return invoker.invoke(invocation);
    }
}
