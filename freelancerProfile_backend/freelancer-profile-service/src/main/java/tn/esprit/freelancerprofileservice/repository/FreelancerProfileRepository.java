package tn.esprit.freelancerprofileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.freelancerprofileservice.entity.FreelancerProfile;
import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {

    // Trouver par userId
    Optional<FreelancerProfile> findByUserId(Long userId);

    // Trouver par disponibilité
    List<FreelancerProfile> findByDisponibilite(FreelancerProfile.Disponibilite disponibilite);

    // Trouver par domaine d'expertise
    List<FreelancerProfile> findByDomaineExpertise(String domaine);

    // Recherche par localisation
    @Query("SELECT p FROM FreelancerProfile p WHERE p.localisation LIKE %:ville%")
    List<FreelancerProfile> findByVille(@Param("ville") String ville);

    // Profils avec taux horaire <= max
    @Query("SELECT p FROM FreelancerProfile p WHERE p.tauxHoraire <= :max")
    List<FreelancerProfile> findByTauxHoraireMax(@Param("max") java.math.BigDecimal max);
}