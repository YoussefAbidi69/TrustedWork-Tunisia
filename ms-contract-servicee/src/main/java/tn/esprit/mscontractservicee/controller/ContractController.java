package tn.esprit.mscontractservicee.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.mscontractservicee.dto.UserDTO;
import tn.esprit.mscontractservicee.entity.Contract;
import tn.esprit.mscontractservicee.enums.ContractStatus;
import tn.esprit.mscontractservicee.service.IContractService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@Tag(name = "Contract", description = "API pour la gestion des contrats")
public class ContractController {

    private final IContractService contractService;

    private static boolean isAdmin(UserDTO user) {
        return user != null && "ADMIN".equals(user.getRole());
    }

    private static boolean isContractParticipant(Contract contract, UserDTO user) {
        if (contract == null || user == null || user.getId() == null) {
            return false;
        }
        if (isAdmin(user)) {
            return true;
        }
        Long userId = user.getId();
        return (contract.getClientId() != null && contract.getClientId().equals(userId))
                || (contract.getFreelancerId() != null && contract.getFreelancerId().equals(userId));
    }

    @GetMapping("/test")
    @Operation(summary = "Test endpoint")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", " ms-contract-service is running!");
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Créer un contrat")
    public ResponseEntity<Contract> createContract(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody Contract contract) {
        Contract saved = contractService.createContract(contract, token);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un contrat par ID")
    public ResponseEntity<Contract> getContractById(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Contract contract = contractService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Contract not found with id: " + id));

        if (!isContractParticipant(contract, currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to access this contract");
        }

        if (!isAdmin(currentUser)
                && "FREELANCER".equals(currentUser.getRole())
                && contract.getFreelancerId() != null
                && contract.getFreelancerId().equals(currentUser.getId())
                && contract.getDateSignature() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to access an unsigned contract");
        }

        return ResponseEntity.ok(contract);
    }

    @GetMapping("/{id}/wallet-ids")
    @Operation(summary = "Récupérer les wallet IDs liés au contrat")
    public ResponseEntity<?> getContractWalletIds(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Contract contract = contractService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Contract not found with id: " + id));

        if (!isContractParticipant(contract, currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to access this contract");
        }

        if (!isAdmin(currentUser)
                && "FREELANCER".equals(currentUser.getRole())
                && contract.getFreelancerId() != null
                && contract.getFreelancerId().equals(currentUser.getId())
                && contract.getDateSignature() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to access an unsigned contract");
        }

        return ResponseEntity.ok(contractService.getWalletIds(id));
    }

    @GetMapping("/me")
    @Operation(summary = "Get my contracts")
    public ResponseEntity<Page<Contract>> getMyContracts(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Pageable pageable = PageRequest.of(page, size);
        if (isAdmin(currentUser)) {
            return ResponseEntity.ok(contractService.findAll(pageable));
        }
        if ("CLIENT".equals(currentUser.getRole())) {
            return ResponseEntity.ok(contractService.findByClientId(currentUser.getId(), pageable));
        }
        if ("FREELANCER".equals(currentUser.getRole())) {
            return ResponseEntity.ok(contractService.findSignedByFreelancerId(currentUser.getId(), pageable));
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unsupported role: " + currentUser.getRole());
    }
    @GetMapping("/freelancer/{freelancerId}/signed")
    @Operation(summary = "List signed contracts for a freelancer")
    public ResponseEntity<Page<Contract>> getSignedContractsByFreelancer(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long freelancerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Pageable pageable = PageRequest.of(page, size);

        if (!isAdmin(currentUser)
                && (!"FREELANCER".equals(currentUser.getRole()) || !freelancerId.equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to access freelancer contracts");
        }

        return ResponseEntity.ok(contractService.findSignedByFreelancerId(freelancerId, pageable));
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les contrats (paginé)")
    public ResponseEntity<Page<Contract>> getAllContracts(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long freelancerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Pageable pageable = PageRequest.of(page, size);

        if (isAdmin(currentUser)) {
            if (userId != null) {
                return ResponseEntity.ok(contractService.findByUserId(userId, pageable));
            }
            if (freelancerId != null) {
                return ResponseEntity.ok(contractService.findSignedByFreelancerId(freelancerId, pageable));
            }
            return ResponseEntity.ok(contractService.findAll(pageable));
        }

        if ("CLIENT".equals(currentUser.getRole())) {
            return ResponseEntity.ok(contractService.findByClientId(currentUser.getId(), pageable));
        }
        if ("FREELANCER".equals(currentUser.getRole())) {
            return ResponseEntity.ok(contractService.findSignedByFreelancerId(currentUser.getId(), pageable));
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unsupported role: " + currentUser.getRole());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un contrat")
    public ResponseEntity<Contract> updateContract(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id,
            @RequestBody Contract contract) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Contract existing = contractService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Contract not found with id: " + id));
        if (!"ADMIN".equals(currentUser.getRole())
                && (existing.getClientId() == null || !existing.getClientId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to update this contract");
        }
        return ResponseEntity.ok(contractService.updateContract(id, contract));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le statut d'un contrat")
    public ResponseEntity<Contract> updateContractStatus(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id,
            @RequestParam ContractStatus status) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Contract existing = contractService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Contract not found with id: " + id));
        if (!"ADMIN".equals(currentUser.getRole())
                && (existing.getClientId() == null || !existing.getClientId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to update this contract");
        }
        return ResponseEntity.ok(contractService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un contrat")
    public ResponseEntity<Void> deleteContract(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Contract existing = contractService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Contract not found with id: " + id));
        if (!"ADMIN".equals(currentUser.getRole())
                && (existing.getClientId() == null || !existing.getClientId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to delete this contract");
        }
        contractService.deleteContract(id);
        return ResponseEntity.noContent().build();
    }
}
