package tn.esprit.msrecruitmentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.msrecruitmentservice.entities.ApplicationStatus;
import tn.esprit.msrecruitmentservice.entities.RecruitmentApplication;
import java.util.List;

public interface IRecruitmentApplicationRepository
        extends JpaRepository<RecruitmentApplication, Long> {

    List<RecruitmentApplication> findByJobPosition_Id(Long jobPositionId);

    List<RecruitmentApplication> findByFreelancerId(Long freelancerId);

    List<RecruitmentApplication> findByEntrepriseId(Long entrepriseId);

    List<RecruitmentApplication> findByStatus(ApplicationStatus status);

    @Query("SELECT a FROM RecruitmentApplication a WHERE a.jobPosition.id = :jpId ORDER BY a.matchingScore DESC")
    List<RecruitmentApplication> findByJobPositionOrderedByScore(@Param("jpId") Long jobPositionId);

    @Query("SELECT COUNT(a) FROM RecruitmentApplication a WHERE a.jobPosition.id = :jpId")
    Long countByJobPositionId(@Param("jpId") Long jobPositionId);


    List<RecruitmentApplication> findByStatusIn(List<ApplicationStatus> statuses);
}