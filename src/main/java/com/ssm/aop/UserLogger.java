package com.ssm.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * @author Eizooo
 * @date 2021/4/14 18:38
 */
@Aspect
public class UserLogger {
    private static final Logger log = Logger.getLogger(UserLogger.class);

    @Pointcut("execution(* *.*.service.UserService.*(..))")
    public void pointcut(){}

    @Before("pointcut()")
    public void before(JoinPoint jp){
        log.info("调用"+jp.getTarget()+"的"+jp.getSignature().getName()+"方法,方法入参:"+ Arrays.toString(jp.getArgs()));
    }

    @AfterReturning(pointcut = "pointcut()",returning = "returnValue")
    public void after(JoinPoint jp,Object returnValue){
        log.info("调用"+jp.getTarget()+"的"+jp.getSignature().getName()+"方法,方法返回值"+ returnValue);
    }


    @AfterThrowing(pointcut = "pointcut()",throwing = "e")
    public void errorLog(JoinPoint joinPoint,RuntimeException e){
        System.out.println("错误方法:"+joinPoint.getSignature().getName()+"错误内容:"+e);
    }
}
