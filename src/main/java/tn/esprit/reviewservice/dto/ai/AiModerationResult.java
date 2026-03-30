package tn.esprit.reviewservice.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.reviewservice.entity.enums.SentimentLabel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiModerationResult {

    private SentimentLabel sentimentLabel;
    private Double riskScore;
    private Boolean flagged;
    private String shortSummary;
    private String rawResponse;
}