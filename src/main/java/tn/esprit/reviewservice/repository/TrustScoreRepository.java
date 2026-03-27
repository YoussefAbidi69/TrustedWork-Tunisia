package tn.esprit.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.reviewservice.entity.TrustScore;

import java.util.List;
import java.util.Optional;

public interface TrustScoreRepository extends JpaRepository<TrustScore, Long> {

    // ================= BASIC =================

    Optional<TrustScore> findByUserId(Long userId);

    List<TrustScore> findAllByOrderByScoreDesc();

    // ================= JPQL =================

    @Query("SELECT t FROM TrustScore t ORDER BY t.score DESC")
    List<TrustScore> findLeaderboard();

    @Query("SELECT t FROM TrustScore t WHERE t.score >= :minScore")
    List<TrustScore> findHighTrustUsers(@Param("minScore") Double minScore);

    @Query("SELECT t FROM TrustScore t WHERE t.score <= :maxScore")
    List<TrustScore> findLowTrustUsers(@Param("maxScore") Double maxScore);

    @Query("SELECT AVG(t.score) FROM TrustScore t")
    Double calculateGlobalAverageScore();

    @Query("SELECT MAX(t.score) FROM TrustScore t")
    Double findMaxScore();

    @Query("SELECT MIN(t.score) FROM TrustScore t")
    Double findMinScore();

    @Query("SELECT COUNT(t) FROM TrustScore t WHERE t.score >= :threshold")
    Long countUsersAboveThreshold(@Param("threshold") Double threshold);

    @Query("SELECT COUNT(t) FROM TrustScore t WHERE t.score < :threshold")
    Long countUsersBelowThreshold(@Param("threshold") Double threshold);

    @Query("SELECT t FROM TrustScore t WHERE t.totalReviews >= :minReviews")
    List<TrustScore> findActiveUsers(@Param("minReviews") Integer minReviews);

    @Query("SELECT t FROM TrustScore t WHERE t.averageRating >= :minRating")
    List<TrustScore> findTopRatedUsers(@Param("minRating") Double minRating);

    @Query("SELECT t FROM TrustScore t ORDER BY t.totalReviews DESC")
    List<TrustScore> findMostReviewedUsers();

    @Query("SELECT t FROM TrustScore t WHERE t.positiveReviews > t.negativeReviews")
    List<TrustScore> findUsersWithPositiveTrend();

    @Query("SELECT t FROM TrustScore t WHERE t.negativeReviews > t.positiveReviews")
    List<TrustScore> findUsersWithNegativeTrend();
}