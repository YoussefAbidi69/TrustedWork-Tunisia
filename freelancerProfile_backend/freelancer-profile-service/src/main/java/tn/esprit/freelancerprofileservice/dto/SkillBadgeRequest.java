package tn.esprit.freelancerprofileservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SkillBadgeRequest {
    private Long profileId;
    private String nomSkill;
    private String niveau; // "JUNIOR" / "CONFIRMED" / "EXPERT"
    private LocalDate dateValidation;
    private String certificatHash;
}