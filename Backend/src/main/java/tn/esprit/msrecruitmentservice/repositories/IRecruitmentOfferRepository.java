package tn.esprit.msrecruitmentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.msrecruitmentservice.entities.RecruitmentOffer;
import tn.esprit.msrecruitmentservice.entities.OfferStatus;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface IRecruitmentOfferRepository
        extends JpaRepository<RecruitmentOffer, Long> {

    Optional<RecruitmentOffer> findByApplicationId(Long applicationId);

    List<RecruitmentOffer> findByStatus(OfferStatus status);

    @Query("SELECT o FROM RecruitmentOffer o WHERE o.application.jobPosition.entrepriseId = :entrepriseId")
    List<RecruitmentOffer> findByEntrepriseId(@Param("entrepriseId") Long entrepriseId);

    @Modifying
    @Transactional
    @Query("UPDATE RecruitmentOffer o SET o.status = :status WHERE o.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") OfferStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE RecruitmentOffer o SET o.contreOffreFreelancer = :contreOffre, o.status = 'NEGOTIATING' WHERE o.id = :id")
    void addContreOffre(@Param("id") Long id, @Param("contreOffre") String contreOffre);
}