package tn.esprit.reviewservice.dto.ai;

import lombok.Data;

@Data
public class ReviewSummaryDTO {
    private String overall;
    private String strengths;
    private String weaknesses;
}