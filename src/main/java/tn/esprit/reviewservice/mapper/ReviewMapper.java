package tn.esprit.reviewservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.reviewservice.dto.response.*;
import tn.esprit.reviewservice.entity.*;

@Component
public class ReviewMapper {

    public BadgeResponse toBadgeResponse(Badge badge) {
        if (badge == null) {
            return null;
        }

        return BadgeResponse.builder()
                .id(badge.getId())
                .name(badge.getName())
                .description(badge.getDescription())
                .categorie(badge.getCategorie())
                .rarete(badge.getRarete())
                .xpRequired(badge.getXpRequired())
                .isActive(badge.getIsActive())
                .build();
    }

    public ReviewResponse toReviewResponse(Review review) {
        if (review == null) {
            return null;
        }

        return ReviewResponse.builder()
                .id(review.getId())
                .contractId(review.getContractId())
                .reviewerId(review.getReviewerId())
                .reviewedUserId(review.getReviewedUserId())
                .reviewType(review.getReviewType())
                .rating(review.getRating())
                .comment(review.getComment())
                .poids(review.getPoids())

                // ===== MULTI-CRITERIA =====
                .noteQualite(review.getNoteQualite())
                .noteDelai(review.getNoteDelai())
                .noteCommunication(review.getNoteCommunication())
                .notePrix(review.getNotePrix())

                .notePonderee(review.getNotePonderee())

                .sentimentLabel(review.getSentimentLabel())
                .isFlagged(review.getIsFlagged())
                .isVisible(review.getIsVisible())
                .isDeleted(review.getIsDeleted())
                .riskScore(review.getRiskScore())
                .trustScoreId(review.getTrustScore() != null ? review.getTrustScore().getId() : null)
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    public TrustScoreResponse toTrustScoreResponse(TrustScore trustScore) {
        if (trustScore == null) {
            return null;
        }

        return TrustScoreResponse.builder()
                .id(trustScore.getId())
                .userId(trustScore.getUserId())
                .score(trustScore.getScore())
                .averageRating(trustScore.getAverageRating())
                .totalReviews(trustScore.getTotalReviews())
                .positiveReviews(trustScore.getPositiveReviews())
                .negativeReviews(trustScore.getNegativeReviews())
                .tendance(trustScore.getTendance())
                .categorie(trustScore.getCategorie())
                .lastCalculatedAt(trustScore.getLastCalculatedAt())
                .createdAt(trustScore.getCreatedAt())
                .updatedAt(trustScore.getUpdatedAt())
                .build();
    }

    public GrowthProfileResponse toGrowthProfileResponse(GrowthProfile growthProfile) {
        if (growthProfile == null) {
            return null;
        }

        return GrowthProfileResponse.builder()
                .id(growthProfile.getId())
                .userId(growthProfile.getUserId())
                .xp(growthProfile.getXp())
                .level(growthProfile.getLevel())
                .niveau(growthProfile.getNiveau())
                .streakDays(growthProfile.getStreakDays())
                .longestStreak(growthProfile.getLongestStreak())
                .lastActivityDate(growthProfile.getLastActivityDate())
                .badgesCount(growthProfile.getBadgesCount())
                .profileCompleted(growthProfile.getProfileCompleted())
                .createdAt(growthProfile.getCreatedAt())
                .updatedAt(growthProfile.getUpdatedAt())
                .build();
    }

    public ReclamationResponse toReclamationResponse(Reclamation reclamation) {
        if (reclamation == null) {
            return null;
        }

        return ReclamationResponse.builder()
                .id(reclamation.getId())
                .reportedByUserId(reclamation.getReportedByUserId())
                .reviewId(reclamation.getReview() != null ? reclamation.getReview().getId() : null)
                .motif(reclamation.getMotif())
                .description(reclamation.getDescription())
                .status(reclamation.getStatus())
                .createdAt(reclamation.getCreatedAt())
                .resolvedAt(reclamation.getResolvedAt())
                .build();
    }

    public UserBadgeResponse toUserBadgeResponse(UserBadge userBadge) {
        if (userBadge == null) {
            return null;
        }

        return UserBadgeResponse.builder()
                .id(userBadge.getId())
                .userId(userBadge.getUserId())
                .badgeId(userBadge.getBadge() != null ? userBadge.getBadge().getId() : null)
                .badgeName(userBadge.getBadge() != null ? userBadge.getBadge().getName() : null)
                .badgeDescription(userBadge.getBadge() != null ? userBadge.getBadge().getDescription() : null)
                .showcased(userBadge.getShowcased())
                .earnedAt(userBadge.getEarnedAt())
                .build();
    }
}