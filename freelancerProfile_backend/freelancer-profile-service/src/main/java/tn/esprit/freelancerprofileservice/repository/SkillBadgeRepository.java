package tn.esprit.freelancerprofileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.freelancerprofileservice.entity.SkillBadge;
import java.util.List;

@Repository
public interface SkillBadgeRepository extends JpaRepository<SkillBadge, Long> {

    // Tous les badges d'un profil
    List<SkillBadge> findByProfileId(Long profileId);

    // Badges par niveau
    List<SkillBadge> findByNiveau(SkillBadge.Niveau niveau);

    // Vérifier si un hash MD5 existe déjà (anti-plagiat)
    boolean existsByCertificatHash(String hash);

    // Badges par nom de compétence
    @Query("SELECT s FROM SkillBadge s WHERE s.nomSkill = :skill AND s.profile.id = :profileId")
    List<SkillBadge> findBySkillAndProfile(@Param("skill") String skill,
                                           @Param("profileId") Long profileId);
}