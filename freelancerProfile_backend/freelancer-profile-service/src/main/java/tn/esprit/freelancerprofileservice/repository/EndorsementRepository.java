package tn.esprit.freelancerprofileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.freelancerprofileservice.entity.Endorsement;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface EndorsementRepository extends JpaRepository<Endorsement, Long> {

    // Endorsements reçus par un profil
    List<Endorsement> findByToProfileId(Long toProfileId);

    // Endorsements validés par l'admin seulement
    List<Endorsement> findByToProfileIdAndIsModerated(Long toProfileId, Boolean isModerated);

    // Endorsements donnés par un freelancer
    List<Endorsement> findByFromProfileId(Long fromProfileId);

    // Modérer un endorsement (admin)
    @Modifying
    @Transactional
    @Query("UPDATE Endorsement e SET e.isModerated = true WHERE e.id = :id")
    void moderateEndorsement(@Param("id") Long id);
}