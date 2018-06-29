package com.alexzfx.earlywarningmachine.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Author : Alex
 * Date : 2018/4/30 20:04
 * Description :
 */
@Aspect
@Component
@Slf4j
public class ControllerLog {

    @Pointcut(value = "execution(public * com.alexzfx.earlywarningmachine.controller..*(..))")
    private void controllerLog() {
    }

    @Before("controllerLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("URL: " + request.getRequestURL().toString());
        log.info("ARGS：" + Arrays.toString(joinPoint.getArgs()));
    }

}
