package com.demo.internal.service.user;

import com.demo.middleware.dubbo.idempotent.annotation.IdempotentCheck;

public interface AuthService {
    @IdempotentCheck
    String validate(ValidateTokenVO validateTokenVO);
}
