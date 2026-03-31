package tn.esprit.msrecruitmentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.msrecruitmentservice.entities.InterviewSchedule;
import tn.esprit.msrecruitmentservice.entities.InterviewStatus;
import jakarta.transaction.Transactional;
import java.util.List;

public interface IInterviewScheduleRepository
        extends JpaRepository<InterviewSchedule, Long> {

    List<InterviewSchedule> findByApplicationId(Long applicationId);

    List<InterviewSchedule> findByStatus(InterviewStatus status);

    @Query("SELECT i FROM InterviewSchedule i WHERE i.application.id = :appId ORDER BY i.ordreEntretien ASC")
    List<InterviewSchedule> findByApplicationOrderedByOrdre(@Param("appId") Long appId);

    @Modifying
    @Transactional
    @Query("UPDATE InterviewSchedule i SET i.status = :status WHERE i.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") InterviewStatus status);


}