package com.masa.appointment.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.masa.appointment..*(..))")
    public void logBefore(JoinPoint joinPoint) {

        String user = "ANONYMOUS";
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            user = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        System.out.println(
                "[LOG] User=" + user +
                " Method=" + joinPoint.getSignature().toShortString()
        );
    }
}
