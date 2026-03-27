package tn.esprit.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.reviewservice.entity.GrowthProfile;
import tn.esprit.reviewservice.entity.enums.Niveau;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrowthProfileRepository extends JpaRepository<GrowthProfile, Long> {

    Optional<GrowthProfile> findByUserId(Long userId);

    List<GrowthProfile> findAllByOrderByXpDesc();

    @Query("SELECT g FROM GrowthProfile g ORDER BY g.xp DESC")
    List<GrowthProfile> findLeaderboard();

    @Query("SELECT g FROM GrowthProfile g WHERE g.level >= :minLevel")
    List<GrowthProfile> findByMinimumLevel(@Param("minLevel") Integer minLevel);

    @Query("SELECT g FROM GrowthProfile g WHERE g.niveau = :niveau")
    List<GrowthProfile> findByNiveau(@Param("niveau") Niveau niveau);

    @Query("SELECT g FROM GrowthProfile g WHERE g.streakDays > 0 ORDER BY g.streakDays DESC")
    List<GrowthProfile> findActiveStreaks();

    @Query("SELECT g FROM GrowthProfile g WHERE g.longestStreak >= :minStreak")
    List<GrowthProfile> findTopStreakUsers(@Param("minStreak") Integer minStreak);

    @Query("SELECT g FROM GrowthProfile g WHERE g.profileCompleted = true")
    List<GrowthProfile> findCompletedProfiles();

    @Query("SELECT g FROM GrowthProfile g WHERE g.badgesCount >= :minBadges")
    List<GrowthProfile> findUsersWithMinBadges(@Param("minBadges") Integer minBadges);

    @Query("SELECT AVG(g.xp) FROM GrowthProfile g")
    Double calculateAverageXp();

    @Query("SELECT MAX(g.xp) FROM GrowthProfile g")
    Integer findMaxXp();

    @Query("SELECT COUNT(g) FROM GrowthProfile g WHERE g.profileCompleted = true")
    Long countCompletedProfiles();
}