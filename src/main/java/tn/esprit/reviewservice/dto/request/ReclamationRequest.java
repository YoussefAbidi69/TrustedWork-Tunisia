package tn.esprit.reviewservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.reviewservice.entity.enums.MotifReclamation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReclamationRequest {

    @NotNull(message = "Reported by user ID is required")
    private Long reportedByUserId;

    @NotNull(message = "Review ID is required")
    private Long reviewId;

    @NotNull(message = "Motif is required")
    private MotifReclamation motif;

    @NotBlank(message = "Description is required")
    private String description;
}