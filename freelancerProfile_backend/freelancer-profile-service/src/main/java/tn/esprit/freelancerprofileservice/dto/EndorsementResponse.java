package tn.esprit.freelancerprofileservice.dto;

import lombok.Data;

@Data
public class EndorsementResponse {
    private Long id;
    private Long fromProfileId;
    private Long toProfileId;
    private String commentaire;
    private Boolean isModerated;
}