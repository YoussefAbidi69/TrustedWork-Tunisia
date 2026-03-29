package tn.esprit.msrecruitmentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.msrecruitmentservice.entities.JobPosition;
import tn.esprit.msrecruitmentservice.entities.JobStatus;
import java.util.List;

public interface IJobPositionRepository extends JpaRepository<JobPosition, Long> {

    // Trouver par entreprise
    List<JobPosition> findByEntrepriseId(Long entrepriseId);

    // Trouver par status
    List<JobPosition> findByStatus(JobStatus status);

    // Trouver par type de contrat
    @Query("SELECT j FROM JobPosition j WHERE j.typeContrat = :type AND j.status = 'PUBLISHED'")
    List<JobPosition> findPublishedByTypeContrat(@Param("type") String type);

    // Recherche par titre
    @Query("SELECT j FROM JobPosition j WHERE j.titre LIKE %:keyword%")
    List<JobPosition> searchByTitre(@Param("keyword") String keyword);
}