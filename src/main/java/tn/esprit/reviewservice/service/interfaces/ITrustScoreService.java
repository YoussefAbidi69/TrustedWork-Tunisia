package tn.esprit.reviewservice.service.interfaces;

import tn.esprit.reviewservice.dto.response.TrustScoreResponse;

import java.util.List;

public interface ITrustScoreService {

    TrustScoreResponse getByUserId(Long userId);

    TrustScoreResponse initializeTrustScore(Long userId);

    TrustScoreResponse recalculateTrustScore(Long userId);

    List<TrustScoreResponse> getLeaderboard();

    List<TrustScoreResponse> getAllTrustScores();
}