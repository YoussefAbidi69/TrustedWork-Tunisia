package tn.esprit.mscontractservicee.repository;

import tn.esprit.mscontractservicee.entity.DeliveryProof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryProofRepository extends JpaRepository<DeliveryProof, Long> {
}