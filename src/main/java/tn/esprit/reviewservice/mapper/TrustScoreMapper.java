package tn.esprit.reviewservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.reviewservice.dto.request.TrustScoreRequest;
import tn.esprit.reviewservice.dto.response.TrustScoreResponse;
import tn.esprit.reviewservice.entity.TrustScore;

@Component
public class TrustScoreMapper {

    public TrustScore toEntity(TrustScoreRequest request) {
        TrustScore trustScore = new TrustScore();
        trustScore.setUserId(request.getUserId());
        trustScore.setScore(request.getScore());
        trustScore.setAverageRating(request.getAverageRating());
        trustScore.setTotalReviews(request.getTotalReviews());
        trustScore.setCategorie(request.getCategorie());
        trustScore.setTendance(request.getTendance());
        return trustScore;
    }

    public TrustScoreResponse toResponse(TrustScore trustScore) {
        return TrustScoreResponse.builder()
                .id(trustScore.getId())
                .userId(trustScore.getUserId())
                .score(trustScore.getScore())
                .averageRating(trustScore.getAverageRating())
                .totalReviews(trustScore.getTotalReviews())
                .categorie(trustScore.getCategorie())
                .tendance(trustScore.getTendance())
                .updatedAt(trustScore.getUpdatedAt())
                .build();
    }
}