package tn.esprit.freelancerprofileservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Cibler toute la couche service
    @Pointcut("execution(* tn.esprit.freelancerprofileservice.service.impl.*.*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("➡️ APPEL MÉTHODE : {} avec arguments : {}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @After("serviceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        log.info("✅ FIN MÉTHODE : {}",
                joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        log.error("❌ ERREUR dans {} : {}",
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }
}