package tn.esprit.mscontractservicee.service;

import tn.esprit.mscontractservicee.dto.ContractWalletIdsResponse;
import tn.esprit.mscontractservicee.dto.UserDTO;
import tn.esprit.mscontractservicee.entity.Contract;
import tn.esprit.mscontractservicee.entity.Wallet;
import tn.esprit.mscontractservicee.enums.ContractStatus;
import tn.esprit.mscontractservicee.feign.UserServiceClient;
import tn.esprit.mscontractservicee.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ContractServiceImpl implements IContractService {

    @Value("${contract.creation.requireFreelancerKycApproved:true}")
    private boolean requireFreelancerKycApproved;

    private final ContractRepository contractRepository;
    private final UserServiceClient userServiceClient;
    private final IWalletService walletService;

    @Override
    public Contract createContract(Contract contract, String token) {
        Long freelancerId = contract.getFreelancerId();
        if (freelancerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "freelancerId is required");
        }

        String authorizationHeader = requireAuthorizationHeader(token);
        UserDTO authenticatedUser = fetchCurrentUser(authorizationHeader);

        if (authenticatedUser.getId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user id is missing");
        }
        if (!"CLIENT".equals(authenticatedUser.getRole()) && !"ADMIN".equals(authenticatedUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only CLIENT or ADMIN can create contracts");
        }

        contract.setClientId(authenticatedUser.getId());
        contract.setClientWalletId(null);
        contract.setFreelancerWalletId(null);

        log.info("Creating new contract for authenticated client: {} and freelancer: {}",
                authenticatedUser.getId(), freelancerId);

        // Verify that the freelancer exists
        UserDTO freelancer = fetchUserById(freelancerId, authorizationHeader);
        if (!"FREELANCER".equals(freelancer.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with id " + freelancerId + " is not a FREELANCER");
        }

        // Verify KYC
        if (requireFreelancerKycApproved
                && (freelancer.getKycStatus() == null || !freelancer.getKycStatus().equals("APPROVED"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Freelancer KYC not approved. Current status: " + freelancer.getKycStatus());
        }

        // Link wallets to the contract
        linkWallets(contract);

        // Create the contract
        contract.setReference("CTR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        contract.setCreatedAt(LocalDateTime.now());
        contract.setUpdatedAt(LocalDateTime.now());
        contract.setStatus(ContractStatus.DRAFT);

        Contract savedContract = contractRepository.save(contract);
        log.info("Contract created successfully with reference: {}", savedContract.getReference());

        return savedContract;
    }

    @Override
    public Contract updateContract(Long id, Contract contract) {
        log.info("Updating contract with id: {}", id);
        Contract existing = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));

        existing.setProjectTitle(contract.getProjectTitle());
        existing.setDescription(contract.getDescription());
        existing.setMontantTotal(contract.getMontantTotal());
        existing.setSlaFreelancerHeures(contract.getSlaFreelancerHeures());
        existing.setSlaClientJours(contract.getSlaClientJours());
        existing.setDateDebut(contract.getDateDebut());
        existing.setDateFin(contract.getDateFin());
        existing.setCommissionRate(contract.getCommissionRate());
        existing.setUpdatedAt(LocalDateTime.now());

        return contractRepository.save(existing);
    }

    @Override
    public Optional<Contract> findById(Long id) {
        return contractRepository.findById(id);
    }

    @Override
    public Page<Contract> findAll(Pageable pageable) {
        return contractRepository.findAll(pageable);
    }

    @Override
    public Page<Contract> findByUserId(Long userId, Pageable pageable) {
        return contractRepository.findByClientIdOrFreelancerId(userId, userId, pageable);
    }

    @Override
    public Page<Contract> findByClientId(Long clientId, Pageable pageable) {
        return contractRepository.findByClientId(clientId, pageable);
    }

    @Override
    public Page<Contract> findSignedByFreelancerId(Long freelancerId, Pageable pageable) {
        return contractRepository.findByFreelancerIdAndDateSignatureIsNotNull(freelancerId, pageable);
    }

    @Override
    public Contract updateStatus(Long id, ContractStatus status) {
        log.info("Updating contract {} status to: {}", id, status);
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));

        contract.setStatus(status);
        contract.setUpdatedAt(LocalDateTime.now());

        if (status == ContractStatus.CANCELLED) {
            contract.setCancelledAt(LocalDateTime.now());
        }

        return contractRepository.save(contract);
    }

    @Override
    public void deleteContract(Long id) {
        log.info("Deleting contract with id: {}", id);
        contractRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return contractRepository.existsById(id);
    }

    // ==================== UTILITY METHODS ====================

    @Override
    public UserDTO getClientInfo(Long contractId, String token) {
        log.info("Getting client info for contract: {}", contractId);

        // Verify that the contract exists
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Contract not found with id: " + contractId));

        String authorizationHeader = requireAuthorizationHeader(token);
        return fetchUserById(contract.getClientId(), authorizationHeader);
    }

    @Override
    public UserDTO getFreelancerInfo(Long contractId, String token) {
        log.info("Getting freelancer info for contract: {}", contractId);

        // Verify that the contract exists
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Contract not found with id: " + contractId));

        String authorizationHeader = requireAuthorizationHeader(token);
        return fetchUserById(contract.getFreelancerId(), authorizationHeader);
    }

    @Override
    public ContractWalletIdsResponse getWalletIds(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + contractId));

        if (contract.getClientWalletId() == null || contract.getFreelancerWalletId() == null) {
            linkWallets(contract);
            contract.setUpdatedAt(LocalDateTime.now());
            contractRepository.save(contract);
        }

        return ContractWalletIdsResponse.builder()
                .contractId(contract.getId())
                .clientId(contract.getClientId())
                .clientWalletId(contract.getClientWalletId())
                .freelancerId(contract.getFreelancerId())
                .freelancerWalletId(contract.getFreelancerWalletId())
                .build();
    }

    private void linkWallets(Contract contract) {
        if (contract.getClientId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clientId is required to link wallets");
        }
        if (contract.getFreelancerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "freelancerId is required to link wallets");
        }

        Wallet clientWallet = walletService.getOrCreateWallet(contract.getClientId());
        Wallet freelancerWallet = walletService.getOrCreateWallet(contract.getFreelancerId());
        contract.setClientWalletId(clientWallet.getId());
        contract.setFreelancerWalletId(freelancerWallet.getId());
    }

    @Override
    public UserDTO getAuthenticatedUser(String token) {
        String authorizationHeader = requireAuthorizationHeader(token);
        return fetchCurrentUser(authorizationHeader);
    }

    private UserDTO fetchCurrentUser(String authorizationHeader) {
        try {
            return userServiceClient.getCurrentUser(authorizationHeader);
        } catch (FeignException e) {
            if (e.status() == 401 || e.status() == 403) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token", e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "User service error", e);
        }
    }

    private UserDTO fetchUserById(Long userId, String authorizationHeader) {
        try {
            return userServiceClient.getUserById(userId, authorizationHeader);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId, e);
            }
            if (e.status() == 401 || e.status() == 403) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token", e);
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "User service error", e);
        }
    }

    private static String requireAuthorizationHeader(String authorizationHeader) {
        String normalized = normalizeAuthorizationHeader(authorizationHeader);
        if (normalized == null || normalized.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is required");
        }
        return normalized;
    }

    private static String normalizeAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null) {
            return null;
        }
        String value = authorizationHeader.trim();
        if (value.isBlank()) {
            return null;
        }
        if (value.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return value;
        }
        return "Bearer " + value;
    }
}
