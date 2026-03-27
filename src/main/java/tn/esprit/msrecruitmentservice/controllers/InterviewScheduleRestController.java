package tn.esprit.msrecruitmentservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.msrecruitmentservice.dto.InterviewScheduleDTO;
import tn.esprit.msrecruitmentservice.entities.InterviewStatus;
import tn.esprit.msrecruitmentservice.services.IInterviewScheduleService;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/interviews")
@Tag(name = "InterviewSchedule", description = "Gestion des entretiens")
public class InterviewScheduleRestController {

    @Autowired
    private IInterviewScheduleService interviewService;

    @PostMapping
    @Operation(summary = "Planifier un entretien")
    public ResponseEntity<InterviewScheduleDTO> create(@RequestBody InterviewScheduleDTO dto) {
        return ResponseEntity.ok(interviewService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un entretien")
    public ResponseEntity<InterviewScheduleDTO> update(@PathVariable Long id,
                                                       @RequestBody InterviewScheduleDTO dto) {
        return ResponseEntity.ok(interviewService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un entretien")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        interviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recuperer un entretien par ID")
    public ResponseEntity<InterviewScheduleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(interviewService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Tous les entretiens")
    public ResponseEntity<List<InterviewScheduleDTO>> getAll() {
        return ResponseEntity.ok(interviewService.getAll());
    }

    @GetMapping("/application/{applicationId}")
    @Operation(summary = "Entretiens par candidature (ordonnes)")
    public ResponseEntity<List<InterviewScheduleDTO>> getByApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(interviewService.getByApplication(applicationId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le status d'un entretien")
    public ResponseEntity<InterviewScheduleDTO> updateStatus(@PathVariable Long id,
                                                             @RequestParam InterviewStatus status) {
        return ResponseEntity.ok(interviewService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/feedback")
    @Operation(summary = "Ajouter feedback et note du recruteur")
    public ResponseEntity<InterviewScheduleDTO> addFeedback(@PathVariable Long id,
                                                            @RequestParam String feedback,
                                                            @RequestParam Integer note) {
        return ResponseEntity.ok(interviewService.addFeedback(id, feedback, note));
    }
}