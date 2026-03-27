package tn.esprit.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.reviewservice.entity.UserBadge;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    List<UserBadge> findByUserId(Long userId);

    List<UserBadge> findByUserIdAndShowcasedTrue(Long userId);

    long countByUserIdAndShowcasedTrue(Long userId);

    boolean existsByUserIdAndBadgeId(Long userId, Long badgeId);

    Optional<UserBadge> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT ub FROM UserBadge ub WHERE ub.userId = :userId ORDER BY ub.earnedAt DESC")
    List<UserBadge> findByUserIdOrderByEarnedAtDesc(@Param("userId") Long userId);

    @Query("SELECT ub FROM UserBadge ub WHERE ub.showcased = true ORDER BY ub.earnedAt DESC")
    List<UserBadge> findAllShowcasedBadges();

    @Query("SELECT COUNT(ub) FROM UserBadge ub WHERE ub.userId = :userId")
    Long countAllBadgesByUserId(@Param("userId") Long userId);

    @Query("SELECT ub FROM UserBadge ub WHERE ub.badge.id = :badgeId")
    List<UserBadge> findByBadgeId(@Param("badgeId") Long badgeId);

    @Query("SELECT ub FROM UserBadge ub JOIN FETCH ub.badge WHERE ub.userId = :userId")
    List<UserBadge> findUserBadgesWithDetails(@Param("userId") Long userId);

    @Query("SELECT ub FROM UserBadge ub JOIN FETCH ub.badge WHERE ub.userId = :userId AND ub.showcased = true")
    List<UserBadge> findShowcasedBadgesWithDetails(@Param("userId") Long userId);

    @Query("SELECT COUNT(ub) FROM UserBadge ub WHERE ub.badge.id = :badgeId")
    Long countUsersByBadge(@Param("badgeId") Long badgeId);
}