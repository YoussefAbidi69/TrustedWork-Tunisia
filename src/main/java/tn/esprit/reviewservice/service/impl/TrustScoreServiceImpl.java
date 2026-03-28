package tn.esprit.reviewservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.reviewservice.dto.response.TrustScoreResponse;
import tn.esprit.reviewservice.entity.Review;
import tn.esprit.reviewservice.entity.TrustScore;
import tn.esprit.reviewservice.entity.enums.Tendance;
import tn.esprit.reviewservice.exception.TrustScoreNotFoundException;
import tn.esprit.reviewservice.mapper.ReviewMapper;
import tn.esprit.reviewservice.repository.ReviewRepository;
import tn.esprit.reviewservice.repository.TrustScoreRepository;
import tn.esprit.reviewservice.service.interfaces.ITrustScoreService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrustScoreServiceImpl implements ITrustScoreService {

    private final TrustScoreRepository trustScoreRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public TrustScoreResponse getByUserId(Long userId) {
        TrustScore trustScore = trustScoreRepository.findByUserId(userId)
                .orElseThrow(() -> new TrustScoreNotFoundException(
                        "TrustScore introuvable pour userId = " + userId
                ));

        return mapper.toTrustScoreResponse(trustScore);
    }

    @Override
    public TrustScoreResponse initializeTrustScore(Long userId) {
        TrustScore existing = trustScoreRepository.findByUserId(userId).orElse(null);

        if (existing != null) {
            return mapper.toTrustScoreResponse(existing);
        }

        TrustScore trustScore = TrustScore.builder()
                .userId(userId)
                .score(0.0)
                .averageRating(0.0)
                .totalReviews(0)
                .positiveReviews(0)
                .negativeReviews(0)
                .tendance(Tendance.STABLE)
                .lastCalculatedAt(LocalDateTime.now())
                .build();

        TrustScore saved = trustScoreRepository.saveAndFlush(trustScore);
        return mapper.toTrustScoreResponse(saved);
    }

    @Override
    public TrustScoreResponse recalculateTrustScore(Long userId) {
        TrustScore trustScore = trustScoreRepository.findByUserId(userId)
                .orElseGet(() -> trustScoreRepository.save(
                        TrustScore.builder()
                                .userId(userId)
                                .score(0.0)
                                .averageRating(0.0)
                                .totalReviews(0)
                                .positiveReviews(0)
                                .negativeReviews(0)
                                .tendance(Tendance.STABLE)
                                .lastCalculatedAt(LocalDateTime.now())
                                .build()
                ));

        List<Review> reviews = reviewRepository.findByReviewedUserIdAndIsDeletedFalse(userId);

        double oldScore = trustScore.getScore() != null ? trustScore.getScore() : 0.0;

        if (reviews.isEmpty()) {
            trustScore.setAverageRating(0.0);
            trustScore.setTotalReviews(0);
            trustScore.setPositiveReviews(0);
            trustScore.setNegativeReviews(0);
            trustScore.setScore(0.0);
            trustScore.setTendance(calculateTrend(oldScore, 0.0));
            trustScore.setLastCalculatedAt(LocalDateTime.now());

            TrustScore saved = trustScoreRepository.save(trustScore);
            return mapper.toTrustScoreResponse(saved);
        }

        int totalReviews = reviews.size();

        int positiveReviews = (int) reviews.stream()
                .filter(r -> r.getRating() != null && r.getRating() >= 4)
                .count();

        int negativeReviews = (int) reviews.stream()
                .filter(r -> r.getRating() != null && r.getRating() <= 2)
                .count();

        double averageRating = reviews.stream()
                .filter(r -> r.getRating() != null)
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        double weightedAverage = reviews.stream()
                .mapToDouble(r -> r.getNotePonderee() != null ? r.getNotePonderee() : 0.0)
                .average()
                .orElse(0.0);

        double normalizedWeightedAverage = Math.min(weightedAverage, 5.0);

        double ratingScore = (normalizedWeightedAverage / 5.0) * 70.0;
        double volumeBonus = Math.min(totalReviews * 2.0, 20.0);

        double sentimentRatio = ((double) positiveReviews - negativeReviews) / totalReviews;
        double sentimentBonus = sentimentRatio * 10.0;

        double finalScore = ratingScore + volumeBonus + sentimentBonus;
        finalScore = clamp(finalScore, 0.0, 100.0);

        trustScore.setAverageRating(roundTwoDecimals(averageRating));
        trustScore.setTotalReviews(totalReviews);
        trustScore.setPositiveReviews(positiveReviews);
        trustScore.setNegativeReviews(negativeReviews);
        trustScore.setScore(roundTwoDecimals(finalScore));
        trustScore.setTendance(calculateTrend(oldScore, finalScore));
        trustScore.setLastCalculatedAt(LocalDateTime.now());

        TrustScore saved = trustScoreRepository.save(trustScore);

        boolean needsReviewUpdate = reviews.stream()
                .anyMatch(r -> r.getTrustScore() == null || !saved.getId().equals(r.getTrustScore().getId()));

        if (needsReviewUpdate) {
            reviews.forEach(r -> r.setTrustScore(saved));
            reviewRepository.saveAll(reviews);
        }

        return mapper.toTrustScoreResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrustScoreResponse> getLeaderboard() {
        return trustScoreRepository.findAllByOrderByScoreDesc()
                .stream()
                .map(mapper::toTrustScoreResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrustScoreResponse> getAllTrustScores() {
        return trustScoreRepository.findAll()
                .stream()
                .map(mapper::toTrustScoreResponse)
                .collect(Collectors.toList());
    }

    private Tendance calculateTrend(double oldScore, double newScore) {
        double diff = newScore - oldScore;

        if (diff > 1.0) {
            return Tendance.UP;
        } else if (diff < -1.0) {
            return Tendance.DOWN;
        }
        return Tendance.STABLE;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private double roundTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}