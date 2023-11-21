package com.example.taskorganization.aspect;

import com.example.taskorganization.annotation.LogIgnore;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {
    @Around(value = "within(@com.example.taskorganization.annotation.Log *)")
    public Object logAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        var signature = (MethodSignature) joinPoint.getSignature();
        var logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        var parameters = buildParameters(signature, joinPoint.getArgs());
        logEvent(logger, signature, parameters);
        Object response;
        try {
            response = joinPoint.proceed();
        } catch (Throwable throwable) {
            logEvent(logger, signature, parameters, throwable);
            throw throwable;
        }
        logEvent(logger, signature, parameters, response);
        return response;
    }

    private StringBuilder buildParameters(MethodSignature signature, Object[] args) {
        var builder = new StringBuilder();
        var params = signature.getMethod().getParameters();
        for (var i = 0; i < params.length; i++) {
            var param = params[i];
            if (param.isAnnotationPresent(LogIgnore.class)) continue;
            builder
                    .append(" ")
                    .append(param.getName())
                    .append(" : ")
                    .append(objectToString(args[i]));
        }
        return builder;
    }

    private String objectToString(Object arg) {
        return arg.toString();
    }

    private void logEvent(Logger logger, MethodSignature signature, StringBuilder parameters) {
        logger.info("ActionLog.{}.start{}", signature.getName(), parameters);
    }

    private void logEvent(Logger logger, MethodSignature signature, StringBuilder parameters, Throwable throwable) {
        logger.error("ActionLog.{}.error{} -> {} - {}", signature.getName(), parameters, throwable.getClass().getName(), throwable.getMessage());
    }

    private void logEvent(Logger logger, MethodSignature signature, StringBuilder parameters, Object response) {
        if (void.class.equals(signature.getReturnType()))
            logger.info("ActionLog.{}.end{}", signature.getName(), parameters);
        else {
            try {
                logger.info("ActionLog.{}.end{} -> {}", signature.getName(), parameters, response);
            } catch (Exception e) {
                logger.info("ActionLog.{}.end{}", signature.getName(), parameters);
            }
        }
    }
}
