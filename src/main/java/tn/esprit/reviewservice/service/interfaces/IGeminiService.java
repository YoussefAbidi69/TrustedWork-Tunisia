package tn.esprit.reviewservice.service.interfaces;

import tn.esprit.reviewservice.dto.ai.AdminInsightDTO;
import tn.esprit.reviewservice.dto.ai.AiModerationResult;
import tn.esprit.reviewservice.dto.ai.ReviewSummaryDTO;

public interface IGeminiService {

    AiModerationResult analyzeReview(String comment, Double ratingAverage);

    ReviewSummaryDTO summarizeUserReviews(String reviewsText);

    AdminInsightDTO generateAdminInsight(String reviewComment, String reclamationReason);
}