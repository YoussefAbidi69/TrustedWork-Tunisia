package tn.esprit.freelancerprofileservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Pointcut("execution(* tn.esprit.freelancerprofileservice.service.impl.*.*(..))")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;

        log.info("⏱️ PERFORMANCE : {} exécutée en {} ms",
                joinPoint.getSignature().getName(),
                duration);

        if (duration > 1000) {
            log.warn("⚠️ MÉTHODE LENTE : {} a pris {} ms !",
                    joinPoint.getSignature().getName(),
                    duration);
        }

        return result;
    }
}
