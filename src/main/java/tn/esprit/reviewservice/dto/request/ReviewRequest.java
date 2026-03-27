package tn.esprit.reviewservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import tn.esprit.reviewservice.entity.enums.ReviewType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {

    @NotNull(message = "Contract ID is required")
    private Long contractId;

    @NotNull(message = "Reviewer ID is required")
    private Long reviewerId;

    @NotNull(message = "Reviewed user ID is required")
    private Long reviewedUserId;

    @NotNull(message = "Review type is required")
    private ReviewType reviewType;

    @NotBlank(message = "Comment is required")
    private String comment;

    // ===== MULTI-CRITERIA SCORES =====

    @NotNull(message = "Quality score is required")
    @Min(value = 1, message = "Quality must be at least 1")
    @Max(value = 5, message = "Quality must be at most 5")
    private Double noteQualite;

    @NotNull(message = "Delay score is required")
    @Min(value = 1, message = "Delay must be at least 1")
    @Max(value = 5, message = "Delay must be at most 5")
    private Double noteDelai;

    @NotNull(message = "Communication score is required")
    @Min(value = 1, message = "Communication must be at least 1")
    @Max(value = 5, message = "Communication must be at most 5")
    private Double noteCommunication;

    @NotNull(message = "Price score is required")
    @Min(value = 1, message = "Price must be at least 1")
    @Max(value = 5, message = "Price must be at most 5")
    private Double notePrix;

    // Optionnel (compatibilité ancienne version)
    private Double poids;
}