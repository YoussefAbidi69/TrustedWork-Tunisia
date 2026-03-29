package tn.esprit.freelancerprofileservice.dto;

import lombok.Data;

@Data
public class EndorsementRequest {
    private Long fromProfileId;
    private Long toProfileId;
    private Long skillId;
    private String commentaire;
}