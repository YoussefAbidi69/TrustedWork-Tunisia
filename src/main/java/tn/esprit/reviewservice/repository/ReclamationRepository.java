package tn.esprit.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.reviewservice.entity.Reclamation;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
}