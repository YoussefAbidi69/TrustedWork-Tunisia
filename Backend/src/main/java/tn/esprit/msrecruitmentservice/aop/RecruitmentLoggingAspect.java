package tn.esprit.msrecruitmentservice.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect AOP — Module 7 Recrutement
 *
 * Responsabilités :
 *   @Before  → log de l'appel entrant (méthode + arguments)
 *   @After   → log de la fin d'exécution (méthode terminée)
 *   @Around  → mesure du temps d'exécution en millisecondes
 *
 * Pointcut cible : toutes les méthodes publiques de la couche service
 *   tn.esprit.msrecruitmentservice.services.*ServiceImpl
 */
@Aspect
@Component
public class RecruitmentLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(RecruitmentLoggingAspect.class);

    // ─────────────────────────────────────────────────────────────
    // @Before : log AVANT l'exécution de chaque méthode service
    // ─────────────────────────────────────────────────────────────

    @Before("execution(* tn.esprit.msrecruitmentservice.services.*ServiceImpl.*(..))")
    public void logAvantAppel(JoinPoint joinPoint) {
        String classe  = joinPoint.getTarget().getClass().getSimpleName();
        String methode = joinPoint.getSignature().getName();
        Object[] args  = joinPoint.getArgs();

        log.info("[RECRUITMENT][BEFORE] {}.{}() — arguments : {}",
                classe, methode, Arrays.toString(args));
    }

    // ─────────────────────────────────────────────────────────────
    // @After : log APRÈS l'exécution (succès ou exception)
    // ─────────────────────────────────────────────────────────────

    @After("execution(* tn.esprit.msrecruitmentservice.services.*ServiceImpl.*(..))")
    public void logApresAppel(JoinPoint joinPoint) {
        String classe  = joinPoint.getTarget().getClass().getSimpleName();
        String methode = joinPoint.getSignature().getName();

        log.info("[RECRUITMENT][AFTER]  {}.{}() — exécution terminée.",
                classe, methode);
    }

    // ─────────────────────────────────────────────────────────────
    // @Around : mesure du TEMPS D'EXÉCUTION en millisecondes
    // ─────────────────────────────────────────────────────────────

    @Around("execution(* tn.esprit.msrecruitmentservice.services.*ServiceImpl.*(..))")
    public Object mesurerTempsExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String classe  = proceedingJoinPoint.getTarget().getClass().getSimpleName();
        String methode = proceedingJoinPoint.getSignature().getName();

        long debut = System.currentTimeMillis();

        Object resultat;
        try {
            resultat = proceedingJoinPoint.proceed(); // exécute la vraie méthode
        } catch (Throwable ex) {
            long duree = System.currentTimeMillis() - debut;
            log.error("[RECRUITMENT][PERF]   {}.{}() — EXCEPTION après {} ms : {}",
                    classe, methode, duree, ex.getMessage());
            throw ex;
        }

        long duree = System.currentTimeMillis() - debut;

        // Seuil d'alerte : log WARN si la méthode prend plus de 500 ms
        if (duree > 500) {
            log.warn("[RECRUITMENT][PERF]   {}.{}() — LENT : {} ms (seuil 500 ms dépassé)",
                    classe, methode, duree);
        } else {
            log.info("[RECRUITMENT][PERF]   {}.{}() — durée : {} ms",
                    classe, methode, duree);
        }

        return resultat;
    }
}