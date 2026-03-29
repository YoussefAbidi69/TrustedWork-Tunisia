package tn.esprit.freelancerprofileservice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class FreelancerProfileResponse {
    private Long id;
    private Long userId;
    private String bio;
    private String titre;
    private BigDecimal tauxHoraire;
    private String localisation;
    private String disponibilite;
    private String domaineExpertise;
    private String trustPassportUrl;
    private List<SkillBadgeResponse> skillBadges;
}