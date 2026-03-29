package tn.esprit.reviewservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.reviewservice.dto.request.ReclamationRequest;
import tn.esprit.reviewservice.dto.response.ReclamationResponse;
import tn.esprit.reviewservice.entity.enums.StatusReclamation;
import tn.esprit.reviewservice.service.interfaces.IReclamationService;

import java.util.List;

@RestController
@RequestMapping("/api/reclamations")
@RequiredArgsConstructor
@Tag(name = "Reclamation", description = "Gestion des réclamations")
public class ReclamationController {

    private final IReclamationService reclamationService;

    @Operation(summary = "Créer une nouvelle réclamation")
    @PostMapping
    public ResponseEntity<ReclamationResponse> createReclamation(@Valid @RequestBody ReclamationRequest request) {
        return ResponseEntity.ok(reclamationService.createReclamation(request));
    }

    @Operation(summary = "Récupérer une réclamation par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReclamationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.getById(id));
    }

    @Operation(summary = "Récupérer toutes les réclamations")
    @GetMapping
    public ResponseEntity<List<ReclamationResponse>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    @Operation(summary = "Récupérer les réclamations en attente")
    @GetMapping("/pending")
    public ResponseEntity<List<ReclamationResponse>> getPendingReclamations() {
        return ResponseEntity.ok(reclamationService.getPendingReclamations());
    }

    @Operation(summary = "Récupérer les réclamations par statut")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReclamationResponse>> getByStatus(@PathVariable StatusReclamation status) {
        return ResponseEntity.ok(reclamationService.getByStatus(status));
    }

    @Operation(summary = "Récupérer les réclamations par review")
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<ReclamationResponse>> getByReviewId(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reclamationService.getByReviewId(reviewId));
    }

    @Operation(summary = "Récupérer les réclamations par utilisateur déclarant")
    @GetMapping("/reported-by/{reportedByUserId}")
    public ResponseEntity<List<ReclamationResponse>> getByReportedByUserId(@PathVariable Long reportedByUserId) {
        return ResponseEntity.ok(reclamationService.getByReportedByUserId(reportedByUserId));
    }

    @Operation(summary = "Confirmer une réclamation")
    @PutMapping("/{id}/confirm")
    public ResponseEntity<ReclamationResponse> confirmReclamation(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.confirmReclamation(id));
    }

    @Operation(summary = "Rejeter une réclamation")
    @PutMapping("/{id}/dismiss")
    public ResponseEntity<ReclamationResponse> dismissReclamation(@PathVariable Long id) {
        return ResponseEntity.ok(reclamationService.dismissReclamation(id));
    }
}