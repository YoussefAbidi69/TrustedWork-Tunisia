package tn.esprit.freelancerprofileservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FreelancerProfileRequest {
    private Long userId;
    private String bio;
    private String titre;
    private BigDecimal tauxHoraire;
    private String localisation;
    private String disponibilite; // "AVAILABLE" / "BUSY" / "NOT_AVAILABLE"
    private String domaineExpertise;
}