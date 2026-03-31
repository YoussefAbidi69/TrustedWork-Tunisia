package tn.esprit.mscontractservicee.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.mscontractservicee.dto.DeliveryProofResponse;
import tn.esprit.mscontractservicee.dto.DeliveryProofSubmitRequest;
import tn.esprit.mscontractservicee.dto.UserDTO;
import tn.esprit.mscontractservicee.entity.Contract;
import tn.esprit.mscontractservicee.entity.Milestone;
import tn.esprit.mscontractservicee.enums.ContractStatus;
import tn.esprit.mscontractservicee.enums.MilestoneStatus;
import tn.esprit.mscontractservicee.service.IContractService;
import tn.esprit.mscontractservicee.service.IDeliveryProofService;
import tn.esprit.mscontractservicee.service.IMilestoneService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/milestones")
@RequiredArgsConstructor
@Tag(name = "Milestone", description = "API pour la gestion des jalons")
public class MilestoneController {

    private final IMilestoneService milestoneService;
    private final IContractService contractService;
    private final IDeliveryProofService deliveryProofService;

    @Value("${milestone.submission.requireDeliveryProof:false}")
    private boolean requireDeliveryProofOnSubmit;

    @Value("${milestone.approval.requireDeliveryProof:false}")
    private boolean requireDeliveryProofOnApprove;

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

    private Contract requireContract(Long contractId) {
        if (contractId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contractId is required");
        }
        return contractService.findById(contractId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Contract not found with id: " + contractId));
    }

    private void requireSignedContract(Contract contract) {
        if (contract.getDateSignature() == null || contract.getStatus() != ContractStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contract is not signed/active yet");
        }
    }

    @GetMapping("/test")
    @Operation(summary = "Test endpoint")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Milestone service is running!");
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Creer un jalon")
    public ResponseEntity<Milestone> createMilestone(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody Milestone milestone) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        if (!isAdmin(currentUser) && !"CLIENT".equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only CLIENT or ADMIN can create milestones");
        }

        Contract contract = requireContract(milestone.getContractId());
        if (!isAdmin(currentUser)
                && (contract.getClientId() == null || !contract.getClientId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to create milestones for this contract");
        }

        Milestone saved = milestoneService.createMilestone(milestone);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recuperer un jalon par ID")
    public ResponseEntity<Milestone> getMilestoneById(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Milestone milestone = milestoneService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));

        Contract contract = requireContract(milestone.getContractId());
        if (!isContractParticipant(contract, currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this milestone");
        }

        return ResponseEntity.ok(milestone);
    }

    @GetMapping("/{id}/delivery-proof")
    @Operation(summary = "Recuperer la preuve de livraison (DeliveryProof) d'un jalon")
    public ResponseEntity<DeliveryProofResponse> getDeliveryProofForMilestone(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Milestone milestone = milestoneService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));

        Contract contract = requireContract(milestone.getContractId());
        if (!isContractParticipant(contract, currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this delivery proof");
        }

        return deliveryProofService.findForMilestone(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "DeliveryProof not found for milestone id: " + id));
    }

    @GetMapping
    @Operation(summary = "Recuperer tous les jalons (pagine) [ADMIN]")
    public ResponseEntity<Page<Milestone>> getAllMilestones(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        if (!isAdmin(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin only");
        }
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(milestoneService.findAll(pageable));
    }

    @GetMapping("/me")
    @Operation(summary = "Recuperer mes jalons (CLIENT/FREELANCER)")
    public ResponseEntity<List<Milestone>> getMyMilestones(
            @RequestHeader(value = "Authorization", required = false) String token) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);

        if ("CLIENT".equals(currentUser.getRole())) {
            return ResponseEntity.ok(milestoneService.findForClient(currentUser.getId()));
        }
        if ("FREELANCER".equals(currentUser.getRole())) {
            return ResponseEntity.ok(milestoneService.findForSignedFreelancer(currentUser.getId()));
        }
        if (isAdmin(currentUser)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Admin should use GET /api/v1/milestones with pagination");
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unsupported role: " + currentUser.getRole());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un jalon")
    public ResponseEntity<Milestone> updateMilestone(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id,
            @RequestBody Milestone milestone) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        if (!isAdmin(currentUser) && !"CLIENT".equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only CLIENT or ADMIN can update milestones");
        }

        Milestone existing = milestoneService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));
        Contract contract = requireContract(existing.getContractId());

        if (!isAdmin(currentUser)
                && (contract.getClientId() == null || !contract.getClientId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this milestone");
        }

        return ResponseEntity.ok(milestoneService.updateMilestone(id, milestone));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le statut d'un jalon [ADMIN]")
    public ResponseEntity<Milestone> updateMilestoneStatus(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id,
            @RequestParam MilestoneStatus status) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        if (!isAdmin(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin only");
        }
        return ResponseEntity.ok(milestoneService.updateStatus(id, status));
    }

    @PostMapping("/{id}/start")
    @Operation(summary = "Demarrer un jalon")
    public ResponseEntity<Milestone> startMilestone(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        if (!isAdmin(currentUser) && !"FREELANCER".equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only FREELANCER or ADMIN can start milestones");
        }

        Milestone milestone = milestoneService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));
        Contract contract = requireContract(milestone.getContractId());
        requireSignedContract(contract);

        if (!isAdmin(currentUser)
                && (contract.getFreelancerId() == null || !contract.getFreelancerId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to start milestones for this contract");
        }

        return ResponseEntity.ok(milestoneService.startMilestone(id));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Soumettre un jalon (et optionnellement sa preuve de livraison)")
    public ResponseEntity<Milestone> submitMilestone(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id,
            @RequestBody(required = false) DeliveryProofSubmitRequest deliveryProof) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        if (!isAdmin(currentUser) && !"FREELANCER".equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only FREELANCER or ADMIN can submit milestones");
        }

        Milestone milestone = milestoneService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));
        Contract contract = requireContract(milestone.getContractId());
        requireSignedContract(contract);

        if (!isAdmin(currentUser)
                && (contract.getFreelancerId() == null || !contract.getFreelancerId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to submit milestones for this contract");
        }

        if (deliveryProof == null) {
            if (requireDeliveryProofOnSubmit) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "deliveryProof payload is required");
            }
            return ResponseEntity.ok(milestoneService.submitMilestone(id));
        }

        return ResponseEntity.ok(milestoneService.submitMilestoneWithProof(id, deliveryProof));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approuver un jalon")
    public ResponseEntity<Milestone> approveMilestone(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        if (!isAdmin(currentUser) && !"CLIENT".equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only CLIENT or ADMIN can approve milestones");
        }

        Milestone milestone = milestoneService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));
        Contract contract = requireContract(milestone.getContractId());
        requireSignedContract(contract);

        if (!isAdmin(currentUser)
                && (contract.getClientId() == null || !contract.getClientId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to approve milestones for this contract");
        }

        if (requireDeliveryProofOnApprove && deliveryProofService.findForMilestone(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DeliveryProof is required before approval");
        }

        return ResponseEntity.ok(milestoneService.approveMilestone(id, currentUser.getId()));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Rejeter un jalon avec une raison (et une nouvelle deadline optionnelle)")
    public ResponseEntity<Milestone> rejectMilestone(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id,
            @RequestParam String reason,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDeadline) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        if (!isAdmin(currentUser) && !"CLIENT".equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only CLIENT or ADMIN can reject milestones");
        }

        Milestone milestone = milestoneService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));
        Contract contract = requireContract(milestone.getContractId());
        requireSignedContract(contract);

        if (!isAdmin(currentUser)
                && (contract.getClientId() == null || !contract.getClientId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to reject milestones for this contract");
        }

        Milestone rejected = milestoneService.rejectMilestone(id, reason, newDeadline);
        return ResponseEntity.ok(rejected);
    }

    @PatchMapping("/{id}/deadline")
    @Operation(summary = "Modifier uniquement la deadline d'un jalon rejete (REJECTED) [CLIENT/ADMIN]")
    public ResponseEntity<Milestone> updateRejectedMilestoneDeadline(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDeadline) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        if (!isAdmin(currentUser) && !"CLIENT".equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only CLIENT or ADMIN can update milestone deadline");
        }

        Milestone milestone = milestoneService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));
        Contract contract = requireContract(milestone.getContractId());
        requireSignedContract(contract);

        if (!isAdmin(currentUser)
                && (contract.getClientId() == null || !contract.getClientId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to update milestone deadline for this contract");
        }

        return ResponseEntity.ok(milestoneService.updateRejectedMilestoneDeadline(id, newDeadline));
    }

    @PostMapping("/{id}/auto-approve")
    @Operation(summary = "Auto-approuver un jalon (SLA) [ADMIN]")
    public ResponseEntity<Milestone> autoApproveMilestone(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        if (!isAdmin(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin only");
        }
        if (requireDeliveryProofOnApprove && deliveryProofService.findForMilestone(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DeliveryProof is required before approval");
        }
        return ResponseEntity.ok(milestoneService.autoApproveMilestone(id, currentUser.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un jalon")
    public ResponseEntity<Void> deleteMilestone(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Milestone milestone = milestoneService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));
        Contract contract = requireContract(milestone.getContractId());

        if (!isAdmin(currentUser)) {
            if (!"CLIENT".equals(currentUser.getRole())
                    || contract.getClientId() == null
                    || !contract.getClientId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this milestone");
            }
            if (milestone.getStatus() != MilestoneStatus.PENDING) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PENDING milestones can be deleted");
            }
        }

        milestoneService.deleteMilestone(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contract/{contractId}")
    @Operation(summary = "Recuperer tous les jalons d'un contrat")
    public ResponseEntity<List<Milestone>> getMilestonesByContract(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long contractId) {
        UserDTO currentUser = contractService.getAuthenticatedUser(token);
        Contract contract = requireContract(contractId);

        if (!isContractParticipant(contract, currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access these milestones");
        }

        List<Milestone> milestones = milestoneService.findByContractId(contractId);
        return ResponseEntity.ok(milestones);
    }
}
