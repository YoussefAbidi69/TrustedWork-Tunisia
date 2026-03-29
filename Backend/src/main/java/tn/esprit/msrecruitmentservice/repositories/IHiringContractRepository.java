package tn.esprit.msrecruitmentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.msrecruitmentservice.entities.ContractStatus;
import tn.esprit.msrecruitmentservice.entities.HiringContract;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface IHiringContractRepository
        extends JpaRepository<HiringContract, Long> {

    Optional<HiringContract> findByOfferId(Long offerId);

    List<HiringContract> findByFreelancerId(Long freelancerId);

    List<HiringContract> findByEntrepriseId(Long entrepriseId);

    List<HiringContract> findByStatus(ContractStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE HiringContract c SET c.status = :status, " +
            "c.dateContratSigne = CURRENT_TIMESTAMP WHERE c.id = :id")
    void signerContrat(@Param("id") Long id, @Param("status") ContractStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE HiringContract c SET c.feedbackPostEmbauche3Mois = :feedback WHERE c.id = :id")
    void addFeedback(@Param("id") Long id, @Param("feedback") String feedback);
}