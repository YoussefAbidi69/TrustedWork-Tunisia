package tn.esprit.msrecruitmentservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.msrecruitmentservice.dto.RecruitmentApplicationDTO;
import tn.esprit.msrecruitmentservice.entities.ApplicationStatus;
import tn.esprit.msrecruitmentservice.services.IRecruitmentApplicationService;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/applications")
@Tag(name = "RecruitmentApplication", description = "Pipeline de candidatures RH")
public class RecruitmentApplicationRestController {

    @Autowired
    private IRecruitmentApplicationService applicationService;

    @PostMapping
    @Operation(summary = "Soumettre une candidature")
    public ResponseEntity<RecruitmentApplicationDTO> apply(@RequestBody RecruitmentApplicationDTO dto) {
        return ResponseEntity.ok(applicationService.apply(dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le statut d'une candidature")
    public ResponseEntity<RecruitmentApplicationDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status,
            @RequestParam(required = false) String motifRejet) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status, motifRejet));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recuperer une candidature par ID")
    public ResponseEntity<RecruitmentApplicationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getById(id));
    }

    @GetMapping("/job-position/{jobPositionId}")
    @Operation(summary = "Candidatures pour un poste")
    public ResponseEntity<List<RecruitmentApplicationDTO>> getByJobPosition(@PathVariable Long jobPositionId) {
        return ResponseEntity.ok(applicationService.getByJobPosition(jobPositionId));
    }

    @GetMapping("/job-position/{jobPositionId}/ranked")
    @Operation(summary = "Candidatures classees par score (meilleur en premier)")
    public ResponseEntity<List<RecruitmentApplicationDTO>> getRanked(@PathVariable Long jobPositionId) {
        return ResponseEntity.ok(applicationService.getRankedByScore(jobPositionId));
    }

    @GetMapping("/freelancer/{freelancerId}")
    @Operation(summary = "Candidatures d'un freelancer")
    public ResponseEntity<List<RecruitmentApplicationDTO>> getByFreelancer(@PathVariable Long freelancerId) {
        return ResponseEntity.ok(applicationService.getByFreelancer(freelancerId));
    }

    @GetMapping("/entreprise/{entrepriseId}")
    @Operation(summary = "Toutes les candidatures recues par une entreprise")
    public ResponseEntity<List<RecruitmentApplicationDTO>> getByEntreprise(@PathVariable Long entrepriseId) {
        return ResponseEntity.ok(applicationService.getByEntreprise(entrepriseId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une candidature")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Recuperer toutes les candidatures")
    public ResponseEntity<List<RecruitmentApplicationDTO>> getAll() {
        return ResponseEntity.ok(applicationService.getAll());
    }


    @GetMapping("/statuses")
    public ResponseEntity<List<RecruitmentApplicationDTO>> getByStatuses(
            @RequestParam List<ApplicationStatus> statuses) {

        return ResponseEntity.ok(applicationService.getByStatuses(statuses));
    }

    @GetMapping("/eligible-offer")
    public List<RecruitmentApplicationDTO> getEligibleForOffer() {
        return applicationService.getApplicationsEligibleForOffer();
    }
}