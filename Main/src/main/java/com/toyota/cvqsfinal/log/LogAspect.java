package com.toyota.cvqsfinal.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
public class LogAspect {


    @Around("@annotation(CustomLogInfo)")
    public Object customLogInfoMethod(ProceedingJoinPoint joinPoint, CustomLogInfo CustomLogInfo) throws Throwable {
        String parameters = obtainParameters(joinPoint);
        log.info("===> CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), parameters);
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        log.info("<=== CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), filterString(proceed.toString()));
        return proceed;
    }

    @Around("@annotation(CustomLogDebug)")
    public Object customLogDebugMethod(ProceedingJoinPoint joinPoint, CustomLogDebug CustomLogDebug) throws Throwable {
        String parameters = obtainParameters(joinPoint);
        log.debug("===> CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), parameters);
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        log.debug("<=== CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), filterString(proceed.toString()));
        return proceed;
    }


    @Around("@annotation(CustomLogDebugHiddenReturn)")
    public Object customLogDebugMethod(ProceedingJoinPoint joinPoint, CustomLogDebugHiddenReturn CustomLogDebugHiddenReturn) throws Throwable {
        String parameters = obtainParameters(joinPoint);
        log.debug("===> CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), parameters);
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        return proceed;
    }


    @Around("@annotation(CustomLogInfoHiddenReturn)")
    public Object customLogInfoHiddenReturnMethod(ProceedingJoinPoint joinPoint, CustomLogInfoHiddenReturn CustomLogInfoHiddenReturn) throws Throwable {
        String parameters = obtainParameters(joinPoint);
        log.info("===> CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), parameters);
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        return proceed;
    }


    @Around("@annotation(CustomLogInfoWithoutParameters)")
    public Object customLogInfoWithoutParametersMethod(ProceedingJoinPoint joinPoint, CustomLogInfoWithoutParameters CustomLogInfoWithoutParameters) throws Throwable {
        log.info("===> CLASS : {} , METHOD : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        log.info("<=== CLASS : {} , METHOD : {} ",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        return proceed;
    }

    @Around("@annotation(CustomLogDebugWithoutParameters)")
    public Object customLogDebugWithoutParametersMethod(ProceedingJoinPoint joinPoint, CustomLogDebugWithoutParameters CustomLogDebugWithoutParameters) throws Throwable {
        log.info("===> CLASS : {} , METHOD : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        log.info("<=== CLASS : {} , METHOD : {} ",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        return proceed;
    }





    private String obtainParameters(ProceedingJoinPoint joinPoint) {
        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.valueToTree(joinPoint.getArgs()).toString();

        return filterString(json);
    }

    private String filterString(String json){
        //Filter password
        json = json.replaceAll("\"password\":\"[^\"]*\"", "\"password\":\"***\"");

        //Filter token
        json = json.replaceAll("\"token\":\"[^\"]*\"", "\"token\":\"***\"");

        //Filter jwt token with three parts
        json = json.replaceAll(",[^\"]*\\.[^\"]*\\.[^\"]*,", ",JWT TOKEN,");

        //Filter jwt token with three parts
        json = json.replaceAll("[^\"]*\\.[^\"]*\\.[^\"]*$", ",JWT TOKEN");

        //Filter data (image data) with , data = ***
        json = json.replaceAll(",\"data\"=\"[^\"]*\"", ",\"data\"=\"***\"");

        //Filter Base64 (image data)
        json = json.replaceAll("\"data\":\"[^\"]*\"", "\"data\":\"***\"");

        return json;
    }


}