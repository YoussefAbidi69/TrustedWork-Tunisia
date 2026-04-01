package tn.esprit.msrecruitmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class MsRecruitmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsRecruitmentServiceApplication.class, args);
    }
}