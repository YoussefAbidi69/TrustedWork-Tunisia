package tn.esprit.mscontractservicee.service;

import tn.esprit.mscontractservicee.dto.ContractWalletIdsResponse;
import tn.esprit.mscontractservicee.dto.UserDTO;
import tn.esprit.mscontractservicee.entity.Contract;
import tn.esprit.mscontractservicee.enums.ContractStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IContractService {

    Contract createContract(Contract contract, String token);
    Contract updateContract(Long id, Contract contract);
    Optional<Contract> findById(Long id);
    Page<Contract> findAll(Pageable pageable);
    Page<Contract> findByUserId(Long userId, Pageable pageable);
    Page<Contract> findByClientId(Long clientId, Pageable pageable);
    Page<Contract> findSignedByFreelancerId(Long freelancerId, Pageable pageable);
    Contract updateStatus(Long id, ContractStatus status);
    void deleteContract(Long id);
    boolean existsById(Long id);

    UserDTO getAuthenticatedUser(String token);

    // Contract-related user lookups
    UserDTO getClientInfo(Long contractId, String token);
    UserDTO getFreelancerInfo(Long contractId, String token);

    ContractWalletIdsResponse getWalletIds(Long contractId);
}

