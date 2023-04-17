package org.acme;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Priority(2020)
@Interceptor
@LogException
public class LogExceptionInterceptor {
    @AroundInvoke
    Object logInvocation(InvocationContext context) throws Exception {
        try {
            return context.proceed();
        } catch (RuntimeException e) {
            Logger.error(e);
            throw e;
        }
    }
}
