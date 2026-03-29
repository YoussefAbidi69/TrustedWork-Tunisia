package tn.esprit.mscontractservicee.repository;

import tn.esprit.mscontractservicee.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Page<Contract> findByClientIdOrFreelancerId(Long clientId, Long freelancerId, Pageable pageable);
    Page<Contract> findByClientId(Long clientId, Pageable pageable);
    Page<Contract> findByFreelancerId(Long freelancerId, Pageable pageable);
    Page<Contract> findByFreelancerIdAndDateSignatureIsNotNull(Long freelancerId, Pageable pageable);
}
