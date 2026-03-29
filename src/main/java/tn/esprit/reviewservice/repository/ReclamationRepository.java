package tn.esprit.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.reviewservice.entity.Reclamation;
import tn.esprit.reviewservice.entity.enums.StatusReclamation;

import java.util.List;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    List<Reclamation> findByStatus(StatusReclamation status);

    List<Reclamation> findByReviewId(Long reviewId);

    List<Reclamation> findByReportedByUserId(Long reportedByUserId);

    boolean existsByReviewIdAndStatusAndReportedByUserId(
            Long reviewId,
            StatusReclamation status,
            Long reportedByUserId
    );

    @Query("SELECT r FROM Reclamation r WHERE r.status = :status ORDER BY r.createdAt DESC")
    List<Reclamation> findByStatusOrdered(@Param("status") StatusReclamation status);

    @Query("SELECT r FROM Reclamation r WHERE r.status = tn.esprit.reviewservice.entity.enums.StatusReclamation.PENDING")
    List<Reclamation> findPendingReclamations();

    @Query("SELECT r FROM Reclamation r WHERE r.status = tn.esprit.reviewservice.entity.enums.StatusReclamation.RESOLVED")
    List<Reclamation> findResolvedReclamations();

    @Query("SELECT r FROM Reclamation r WHERE r.reportedByUserId = :userId ORDER BY r.createdAt DESC")
    List<Reclamation> findUserReclamations(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Reclamation r WHERE r.status = :status")
    Long countByStatusQuery(@Param("status") StatusReclamation status);

    @Query("SELECT COUNT(r) FROM Reclamation r WHERE r.reportedByUserId = :userId")
    Long countByReportedUser(@Param("userId") Long userId);

    @Query("SELECT r FROM Reclamation r WHERE r.review.id = :reviewId")
    List<Reclamation> findByReviewIdQuery(@Param("reviewId") Long reviewId);

    @Query("SELECT r FROM Reclamation r ORDER BY r.createdAt DESC")
    List<Reclamation> findAllOrderByCreatedAtDesc();
}