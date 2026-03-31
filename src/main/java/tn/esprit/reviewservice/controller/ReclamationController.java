package tn.esprit.reviewservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.reviewservice.dto.request.ReclamationRequest;
import tn.esprit.reviewservice.dto.response.ReclamationResponse;
import tn.esprit.reviewservice.service.interfaces.IReclamationService;

import java.util.List;

@RestController
@RequestMapping("/api/reclamations")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReclamationController {

    private final IReclamationService service;

    @PostMapping
    public ResponseEntity<ReclamationResponse> create(@Valid @RequestBody ReclamationRequest request) {
        return ResponseEntity.ok(service.createReclamation(request));
    }

    @GetMapping
    public ResponseEntity<List<ReclamationResponse>> getAll() {
        return ResponseEntity.ok(service.getAllReclamations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReclamationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReclamationById(id));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<ReclamationResponse> resolve(@PathVariable Long id) {
        return ResponseEntity.ok(service.resolveReclamation(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReclamation(id);
        return ResponseEntity.noContent().build();
    }
}