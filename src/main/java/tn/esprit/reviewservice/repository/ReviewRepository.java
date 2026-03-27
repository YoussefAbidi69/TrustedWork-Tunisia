package tn.esprit.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.reviewservice.entity.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReviewedUserIdAndIsDeletedFalse(Long reviewedUserId);

    List<Review> findByContractIdAndIsDeletedFalse(Long contractId);

    List<Review> findByIsFlaggedTrueAndIsDeletedFalse();

    List<Review> findByIsVisibleTrueAndIsDeletedFalse();

    Optional<Review> findByContractIdAndReviewerIdAndIsDeletedFalse(Long contractId, Long reviewerId);

    boolean existsByContractIdAndReviewerIdAndIsDeletedFalse(Long contractId, Long reviewerId);

    long countByReviewedUserIdAndIsDeletedFalse(Long reviewedUserId);
}
