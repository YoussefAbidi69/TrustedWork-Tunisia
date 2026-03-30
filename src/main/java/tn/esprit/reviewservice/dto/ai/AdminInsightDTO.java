package tn.esprit.reviewservice.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminInsightDTO {
    private String riskLevel;
    private String issue;
    private List<String> actions;
}