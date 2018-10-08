package com.czh.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Chen on 2017/4/17.
 */
//@Aspect
//@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.czh.controller.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        logger.info("Before method:");
    }

    @After("execution(* com.czh.controller.*.*(..))")
    public void afterMethod(JoinPoint joinPoint) {
        logger.info("After method:");
    }
}
