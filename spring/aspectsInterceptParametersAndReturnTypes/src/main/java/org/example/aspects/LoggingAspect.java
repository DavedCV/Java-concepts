package org.example.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.models.Comment;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
public class LoggingAspect {

    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    @Around("execution(* org.example.services.*.*(..))") // Defines which are the intercepted methods
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        // Obtains the name and parameters of the intercepted method
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        // Logs the name and parameters of the intercepted method
        logger.info("Method " + methodName + " with parameters " + Arrays.asList(arguments) + " will execute");

        Comment comment = new Comment();
        comment.setText("Some other text!");
        Object[] newArgs = {comment};

        // Delegates to the actual intercepted method with different parameters
        Object returnedByMethod = joinPoint.proceed(newArgs);

        logger.info("Method executed and returned " + returnedByMethod);

        // Returns a different value, not the actual one returned by the target method
        return "FAILED";
    }
}
