package tn.esprit.freelancerprofileservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.freelancerprofileservice.dto.CertificationExamRequest;
import tn.esprit.freelancerprofileservice.entity.CertificationExam;
import tn.esprit.freelancerprofileservice.service.FreelancerProfileService;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@Tag(name = "Certification Exam", description = "Gestion des examens QCM de certification")
public class CertificationExamController {

    private final FreelancerProfileService profileService;

    @Operation(summary = "Créer un examen QCM (admin)")
    @PostMapping
    public ResponseEntity<CertificationExam> createExam(
            @RequestBody CertificationExamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createExam(request));
    }

    @Operation(summary = "Récupérer un examen par ID")
    @GetMapping("/{id}")
    public ResponseEntity<CertificationExam> getExam(
            @PathVariable Long id) {
        return ResponseEntity.ok(profileService.getExamById(id));
    }

    @Operation(summary = "Lister tous les examens")
    @GetMapping
    public ResponseEntity<List<CertificationExam>> getAllExams() {
        return ResponseEntity.ok(profileService.getAllExams());
    }

    @Operation(summary = "Lister les examens par domaine")
    @GetMapping("/domaine/{domaine}")
    public ResponseEntity<List<CertificationExam>> getByDomaine(
            @PathVariable String domaine) {
        return ResponseEntity.ok(profileService.getExamsByDomaine(domaine));
    }

    @Operation(summary = "Modifier un examen")
    @PutMapping("/{id}")
    public ResponseEntity<CertificationExam> updateExam(
            @PathVariable Long id,
            @RequestBody CertificationExamRequest request) {
        return ResponseEntity.ok(profileService.updateExam(id, request));
    }

    @Operation(summary = "Supprimer un examen")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        profileService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
}