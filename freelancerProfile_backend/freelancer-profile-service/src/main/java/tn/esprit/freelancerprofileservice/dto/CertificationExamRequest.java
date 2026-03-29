package tn.esprit.freelancerprofileservice.dto;

import lombok.Data;

@Data
public class CertificationExamRequest {
    private String domaine;
    private String questions;
    private Integer dureeMinutes;
    private Integer scoreMinimum;
    private String baremeConfig;
}