package tn.esprit.msrecruitmentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.msrecruitmentservice.entities.TalentPool;
import tn.esprit.msrecruitmentservice.entities.TalentTag;
import java.util.List;

public interface ITalentPoolRepository extends JpaRepository<TalentPool, Long> {

    List<TalentPool> findByEntrepriseId(Long entrepriseId);

    List<TalentPool> findByFreelancerId(Long freelancerId);

    List<TalentPool> findByEntrepriseIdAndTag(Long entrepriseId, TalentTag tag);

    @Query("SELECT t FROM TalentPool t WHERE t.entrepriseId = :eid AND t.alerteDisponibilite = true")
    List<TalentPool> findAlertsActive(@Param("eid") Long entrepriseId);
}