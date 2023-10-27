package org.example.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.logging.Logger;

@Aspect
public class LoggingAspect {

    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("execution(* org.example.services.*.*(..))") // Defines which are the intercepted methods
    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
        // Prints a message in the console before the intercepted method’s execution
        logger.info("Method will execute");
        // Delegates to the actual intercepted method
        joinPoint.proceed();
        // Prints a message in the console after the intercepted method’s execution
        logger.info("Method executed");
    }
}
