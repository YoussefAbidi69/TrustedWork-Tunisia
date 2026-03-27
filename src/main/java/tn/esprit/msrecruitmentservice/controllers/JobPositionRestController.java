package tn.esprit.msrecruitmentservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.msrecruitmentservice.dto.JobPositionDTO;
import tn.esprit.msrecruitmentservice.services.IJobPositionService;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/job-positions")
@Tag(name = "JobPosition", description = "Gestion des offres d'emploi CDI/CDD")
public class JobPositionRestController {

    @Autowired
    private IJobPositionService jobPositionService;

    @PostMapping
    @Operation(summary = "Creer une offre d'emploi")
    public ResponseEntity<JobPositionDTO> create(@RequestBody JobPositionDTO dto) {
        return ResponseEntity.ok(jobPositionService.createJobPosition(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une offre d'emploi")
    public ResponseEntity<JobPositionDTO> update(@PathVariable Long id,
                                                 @RequestBody JobPositionDTO dto) {
        return ResponseEntity.ok(jobPositionService.updateJobPosition(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une offre d'emploi")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobPositionService.deleteJobPosition(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recuperer une offre par ID")
    public ResponseEntity<JobPositionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPositionService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Recuperer toutes les offres")
    public ResponseEntity<List<JobPositionDTO>> getAll() {
        return ResponseEntity.ok(jobPositionService.getAll());
    }

    @GetMapping("/entreprise/{entrepriseId}")
    @Operation(summary = "Offres par entreprise")
    public ResponseEntity<List<JobPositionDTO>> getByEntreprise(@PathVariable Long entrepriseId) {
        return ResponseEntity.ok(jobPositionService.getByEntreprise(entrepriseId));
    }

    @GetMapping("/published")
    @Operation(summary = "Toutes les offres publiees")
    public ResponseEntity<List<JobPositionDTO>> getPublished() {
        return ResponseEntity.ok(jobPositionService.getPublished());
    }
}