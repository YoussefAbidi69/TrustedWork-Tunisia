package tn.esprit.reviewservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.reviewservice.entity.enums.MotifReclamation;

@Getter
@Setter
public class ReclamationRequest {

    @NotNull
    private Long reviewId;

    @NotNull
    private Long reportedByUserId;

    @NotNull
    private MotifReclamation motif;

    private String description;
}