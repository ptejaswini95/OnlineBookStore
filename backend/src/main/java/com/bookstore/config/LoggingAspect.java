package com.bookstore.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.bookstore.service.*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        log.info("Starting execution of {}.{}", className, methodName);
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            
            log.info("Completed execution of {}.{} in {} ms", 
                className, methodName, stopWatch.getTotalTimeMillis());
            
            return result;
        } catch (Exception e) {
            log.error("Error in {}.{}: {}", className, methodName, e.getMessage(), e);
            throw e;
        }
    }
} 