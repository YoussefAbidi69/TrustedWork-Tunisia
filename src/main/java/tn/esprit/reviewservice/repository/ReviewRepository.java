package tn.esprit.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.reviewservice.entity.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // ================= SIMPLE METHODS =================

    List<Review> findByReviewedUserIdAndIsDeletedFalse(Long reviewedUserId);

    List<Review> findByContractIdAndIsDeletedFalse(Long contractId);

    List<Review> findByIsFlaggedTrueAndIsDeletedFalse();

    List<Review> findByIsVisibleTrueAndIsDeletedFalse();

    Optional<Review> findByContractIdAndReviewerIdAndIsDeletedFalse(Long contractId, Long reviewerId);

    boolean existsByContractIdAndReviewerIdAndIsDeletedFalse(Long contractId, Long reviewerId);

    long countByReviewedUserIdAndIsDeletedFalse(Long reviewedUserId);

    // ================= JPQL QUERIES =================

    @Query("SELECT AVG(r.rating) " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId AND r.isDeleted = false")
    Double calculateAverageScoreByUserId(@Param("userId") Long userId);

    @Query("SELECT AVG(r.notePonderee) " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId AND r.isDeleted = false")
    Double calculateAverageWeightedScoreByUserId(@Param("userId") Long userId);

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.riskScore >= :minRisk AND r.isDeleted = false " +
            "ORDER BY r.riskScore DESC")
    List<Review> findHighRiskReviews(@Param("minRisk") Double minRisk);

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.sentimentLabel = tn.esprit.reviewservice.entity.enums.SentimentLabel.POSITIVE " +
            "AND r.isDeleted = false")
    List<Review> findPositiveReviews();

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.sentimentLabel = tn.esprit.reviewservice.entity.enums.SentimentLabel.NEGATIVE " +
            "AND r.isDeleted = false")
    List<Review> findNegativeReviews();

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.rating >= :minRating AND r.isDeleted = false " +
            "ORDER BY r.createdAt DESC")
    List<Review> findTopRatedReviews(@Param("minRating") Integer minRating);

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.noteQualite >= :minQualite AND r.isDeleted = false " +
            "ORDER BY r.noteQualite DESC")
    List<Review> findByMinimumQuality(@Param("minQualite") Double minQualite);

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.noteCommunication >= :minCommunication AND r.isDeleted = false " +
            "ORDER BY r.noteCommunication DESC")
    List<Review> findByMinimumCommunication(@Param("minCommunication") Double minCommunication);

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.notePrix >= :minPrix AND r.isDeleted = false " +
            "ORDER BY r.notePrix DESC")
    List<Review> findByMinimumPrice(@Param("minPrix") Double minPrix);

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.noteDelai >= :minDelai AND r.isDeleted = false " +
            "ORDER BY r.noteDelai DESC")
    List<Review> findByMinimumDelay(@Param("minDelai") Double minDelai);

    @Query("SELECT COUNT(r) " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId " +
            "AND r.sentimentLabel = tn.esprit.reviewservice.entity.enums.SentimentLabel.POSITIVE " +
            "AND r.isDeleted = false")
    Long countPositiveReviewsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId " +
            "AND r.sentimentLabel = tn.esprit.reviewservice.entity.enums.SentimentLabel.NEGATIVE " +
            "AND r.isDeleted = false")
    Long countNegativeReviewsByUserId(@Param("userId") Long userId);

    @Query("SELECT r.reviewedUserId " +
            "FROM Review r " +
            "WHERE r.isDeleted = false " +
            "GROUP BY r.reviewedUserId " +
            "ORDER BY AVG(r.notePonderee) DESC")
    List<Long> findLeaderboard();

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId AND r.isDeleted = false " +
            "ORDER BY r.createdAt DESC")
    List<Review> findRecentReviewsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId " +
            "AND r.isFlagged = true " +
            "AND r.isDeleted = false")
    Long countFlaggedReviewsByUserId(@Param("userId") Long userId);

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.comment IS NOT NULL " +
            "AND LENGTH(r.comment) < :maxLength " +
            "AND r.isDeleted = false")
    List<Review> findSuspiciousShortReviews(@Param("maxLength") Integer maxLength);

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId " +
            "AND r.isVisible = true " +
            "AND r.isDeleted = false " +
            "ORDER BY r.createdAt DESC")
    List<Review> findVisibleReviewsByUserId(@Param("userId") Long userId);

    @Query("SELECT AVG(r.noteQualite) " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId AND r.isDeleted = false")
    Double calculateAverageQualityByUserId(@Param("userId") Long userId);

    @Query("SELECT AVG(r.noteDelai) " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId AND r.isDeleted = false")
    Double calculateAverageDelayByUserId(@Param("userId") Long userId);

    @Query("SELECT AVG(r.noteCommunication) " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId AND r.isDeleted = false")
    Double calculateAverageCommunicationByUserId(@Param("userId") Long userId);

    @Query("SELECT AVG(r.notePrix) " +
            "FROM Review r " +
            "WHERE r.reviewedUserId = :userId AND r.isDeleted = false")
    Double calculateAveragePriceByUserId(@Param("userId") Long userId);
}