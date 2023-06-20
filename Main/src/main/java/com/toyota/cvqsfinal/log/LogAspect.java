package com.toyota.cvqsfinal.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyota.cvqsfinal.exception.DefectNotFoundException;
import com.toyota.cvqsfinal.exception.GenericException;
import com.toyota.cvqsfinal.exception.VehicleDefectNotFoundException;
import com.toyota.cvqsfinal.exception.VehicleNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.security.SignatureException;

@Log4j2
@Aspect
@Component
public class LogAspect {


    @Around("@annotation(CustomLogInfo)")
    public Object customLogInfoMethod(ProceedingJoinPoint joinPoint, CustomLogInfo CustomLogInfo) throws Throwable {
        String parameters = obtainParameters(joinPoint);
        log.info("===> CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), parameters);
        Object proceed =  null;
        try {
            proceed = joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        catch (VehicleNotFoundException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (DefectNotFoundException e) {
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (VehicleDefectNotFoundException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (SignatureException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (GenericException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (Exception e){
            log.error("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getStackTrace());

        }
        try {
            log.info("<=== CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), filterString(proceed.toString()));
        }
        catch (Exception e){

        }
        return proceed;
    }

    @Around("@annotation(CustomLogDebug)")
    public Object customLogDebugMethod(ProceedingJoinPoint joinPoint, CustomLogDebug CustomLogDebug) throws Throwable {
        String parameters = obtainParameters(joinPoint);
        log.debug("===> CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), parameters);
        Object proceed =  null;
        try {
            proceed = joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (VehicleNotFoundException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (DefectNotFoundException e) {
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (VehicleDefectNotFoundException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (SignatureException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (GenericException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (Exception e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getStackTrace());
            throw e;

        }
        try {
            log.debug("<=== CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), filterString(proceed.toString()));
        }
        catch (Exception e){
        }
        return proceed;
    }


    @Around("@annotation(CustomLogDebugHiddenReturn)")
    public Object customLogDebugMethod(ProceedingJoinPoint joinPoint, CustomLogDebugHiddenReturn CustomLogDebugHiddenReturn) throws Throwable {
        String parameters = obtainParameters(joinPoint);
        log.debug("===> CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), parameters);
        Object proceed =  null;
        try {
            proceed = joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (VehicleNotFoundException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (DefectNotFoundException e) {
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (VehicleDefectNotFoundException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (SignatureException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (GenericException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (Exception e){
            log.error("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getStackTrace());
        }
        return proceed;
    }


    @Around("@annotation(CustomLogInfoHiddenReturn)")
    public Object customLogInfoHiddenReturnMethod(ProceedingJoinPoint joinPoint, CustomLogInfoHiddenReturn CustomLogInfoHiddenReturn) throws Throwable {
        String parameters = obtainParameters(joinPoint);
        log.info("===> CLASS : {} , METHOD : {} , PARAMETERS : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), parameters);
        Object proceed =  null;
        try {
            proceed = joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (VehicleNotFoundException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (DefectNotFoundException e) {
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (VehicleDefectNotFoundException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (SignatureException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (GenericException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (Exception e){
            log.error("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getStackTrace());

        }
        return proceed;
    }


    @Around("@annotation(CustomLogInfoWithoutParameters)")
    public Object customLogInfoWithoutParametersMethod(ProceedingJoinPoint joinPoint, CustomLogInfoWithoutParameters CustomLogInfoWithoutParameters) throws Throwable {
        log.info("===> CLASS : {} , METHOD : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        Object proceed =  null;
        try {
            proceed = joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (VehicleNotFoundException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (DefectNotFoundException e) {
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (VehicleDefectNotFoundException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (SignatureException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (GenericException e){
            log.info("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (Exception e){
            log.error("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getStackTrace());
        }
        try {
            log.info("<=== CLASS : {} , METHOD : {} ",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        }
        catch (Exception e){

        }
        return proceed;
    }

    @Around("@annotation(CustomLogDebugWithoutParameters)")
    public Object customLogDebugWithoutParametersMethod(ProceedingJoinPoint joinPoint, CustomLogDebugWithoutParameters CustomLogDebugWithoutParameters) throws Throwable {
        log.debug("===> CLASS : {} , METHOD : {}",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        Object proceed =  null;
        try {
            proceed = joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        catch (VehicleNotFoundException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        catch (DefectNotFoundException e) {
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        catch (VehicleDefectNotFoundException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        catch (SignatureException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        catch (GenericException e){
            log.debug("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getMessage());
            throw e;

        }
        catch (Exception e){
            log.error("!!! CLASS : {} , METHOD : {} , EXCEPTION MESSAGE : {}",joinPoint.getTarget().getClass() ,joinPoint.getSignature().getName(), e.getStackTrace());
        }
        try {
            log.debug("<=== CLASS : {} , METHOD : {} ",joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());

        }
        catch (Exception e){

        }
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
