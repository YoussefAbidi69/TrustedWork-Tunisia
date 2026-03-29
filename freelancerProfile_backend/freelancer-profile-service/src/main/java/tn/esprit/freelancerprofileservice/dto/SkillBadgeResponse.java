package tn.esprit.freelancerprofileservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SkillBadgeResponse {
    private Long id;
    private String nomSkill;
    private String niveau;
    private LocalDate dateValidation;
}