package tn.esprit.msrecruitmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Point d'entrée — ms-recruitment-service (port 8089)
 *
 * @EnableScheduling  active les tâches @Scheduled (JobPositionScheduler)
 * L'AOP (@Aspect) est activé automatiquement par Spring Boot
 * via spring-boot-starter-aop dans le pom.xml
 */
@SpringBootApplication
@EnableScheduling
public class MsRecruitmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsRecruitmentServiceApplication.class, args);
    }
}