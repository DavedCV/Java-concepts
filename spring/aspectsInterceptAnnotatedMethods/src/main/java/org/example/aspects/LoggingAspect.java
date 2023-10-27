package org.example.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.models.Comment;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
public class LoggingAspect {

    private Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    /*
    @Around("@annotation(ToLog)") // Weaving the aspect to the methods annotated with @ToLog
    public void log(ProceedingJoinPoint joinPoint) throws Throwable {

        // Obtains the name and parameters of the intercepted method
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        // Logs the name and parameters of the intercepted method
        logger.info("Method " + methodName + " with parameters " + Arrays.asList(arguments) + " will execute");

        // Delegates to the actual intercepted method
        joinPoint.proceed();
    }
    */

    /*
    * - The AspectJ pointcut expression specifies which methods this aspect logic weaves to
    * - Optionally, when you use @AfterReturning, you can get the value returned by the intercepted method. In this
    * case, we add the “returning” attribute with a value that corresponds to the name of the method’s parameter
    * where this value will be provided.
    * - The parameter name should be the same as the value of the “returning” attribute of the annotation or missing
    * if we don’t need to use the returned value.
    * */
    @AfterReturning(value = "@annotation(ToLog)", returning = "returnedValue")
    public void log(Object returnedValue) {
        logger.info("Method executed and returned: " + returnedValue);
    }
}
