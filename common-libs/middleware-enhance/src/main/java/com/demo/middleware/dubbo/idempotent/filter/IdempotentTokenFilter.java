package com.demo.middleware.dubbo.idempotent.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.UUID;

@Activate(group = {CommonConstants.CONSUMER})
public class IdempotentTokenFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String requestId = UUID.randomUUID().toString();
        RpcContext.getServiceContext().setAttachment("dubbo-request-id", requestId);
        return invoker.invoke(invocation);
    }
}
