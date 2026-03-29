package tn.esprit.freelancerprofileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.freelancerprofileservice.entity.CertificationExam;
import java.util.List;

@Repository
public interface CertificationExamRepository extends JpaRepository<CertificationExam, Long> {

    // Examens par domaine
    List<CertificationExam> findByDomaine(String domaine);

    // Examens avec tentatives < max autorisées
    @Query("SELECT e FROM CertificationExam e WHERE e.tentatives < :max")
    List<CertificationExam> findExamsWithRemainingAttempts(@Param("max") Integer max);

    // Examens par score minimum
    @Query("SELECT e FROM CertificationExam e WHERE e.scoreMinimum <= :score")
    List<CertificationExam> findByScoreMinimumLessThanEqual(@Param("score") Integer score);
}