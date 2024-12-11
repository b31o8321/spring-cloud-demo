package com.demo.middleware.dubbo.idempotent.aspect;

import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

public class IdempotentAspect {
    @Before("@annotation(org.example.dubbo.idempotent.annotation.IdempotentCheck)")
    public Object check(MethodInvocationProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();

        return joinPoint.proceed();
    }
}
