package tn.esprit.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.reviewservice.entity.Badge;
import tn.esprit.reviewservice.entity.enums.CategorieBadge;
import tn.esprit.reviewservice.entity.enums.Rarete;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    List<Badge> findByIsActiveTrue();

    List<Badge> findByIsActiveTrueOrderByXpRequiredAsc();

    @Query("SELECT b FROM Badge b WHERE b.isActive = true ORDER BY b.xpRequired ASC")
    List<Badge> findAvailableBadges();

    @Query("SELECT b FROM Badge b WHERE b.xpRequired <= :xp AND b.isActive = true ORDER BY b.xpRequired ASC")
    List<Badge> findEligibleBadges(@Param("xp") Integer xp);

    @Query("SELECT b FROM Badge b WHERE b.xpRequired >= :minXp")
    List<Badge> findByMinimumXp(@Param("minXp") Integer minXp);

    @Query("SELECT b FROM Badge b WHERE b.categorie = :categorie AND b.isActive = true")
    List<Badge> findByCategorie(@Param("categorie") CategorieBadge categorie);

    @Query("SELECT b FROM Badge b WHERE b.rarete = :rarete AND b.isActive = true")
    List<Badge> findByRarete(@Param("rarete") Rarete rarete);

    @Query("SELECT b FROM Badge b WHERE b.isActive = true AND b.xpRequired BETWEEN :minXp AND :maxXp ORDER BY b.xpRequired ASC")
    List<Badge> findBadgesByXpRange(@Param("minXp") Integer minXp, @Param("maxXp") Integer maxXp);

    @Query("SELECT COUNT(b) FROM Badge b WHERE b.isActive = true")
    Long countActiveBadges();

    @Query("SELECT b FROM Badge b ORDER BY b.xpRequired DESC")
    List<Badge> findAllOrderByXpRequiredDesc();
}