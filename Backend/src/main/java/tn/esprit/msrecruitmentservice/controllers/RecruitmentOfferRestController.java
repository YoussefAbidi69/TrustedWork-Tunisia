package tn.esprit.msrecruitmentservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.msrecruitmentservice.dto.RecruitmentOfferDTO;
import tn.esprit.msrecruitmentservice.entities.OfferStatus;
import tn.esprit.msrecruitmentservice.services.IRecruitmentOfferService;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/offers")
@Tag(name = "RecruitmentOffer", description = "Gestion des offres formelles")
public class RecruitmentOfferRestController {

    @Autowired
    private IRecruitmentOfferService offerService;

    @PostMapping
    @Operation(summary = "Envoyer une offre formelle")
    public ResponseEntity<RecruitmentOfferDTO> create(@RequestBody RecruitmentOfferDTO dto) {
        return ResponseEntity.ok(offerService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une offre")
    public ResponseEntity<RecruitmentOfferDTO> update(@PathVariable Long id,
                                                      @RequestBody RecruitmentOfferDTO dto) {
        return ResponseEntity.ok(offerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une offre")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        offerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recuperer une offre par ID")
    public ResponseEntity<RecruitmentOfferDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Toutes les offres")
    public ResponseEntity<List<RecruitmentOfferDTO>> getAll() {
        return ResponseEntity.ok(offerService.getAll());
    }

    @GetMapping("/application/{applicationId}")
    @Operation(summary = "Offre par candidature")
    public ResponseEntity<RecruitmentOfferDTO> getByApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(offerService.getByApplication(applicationId));
    }

    @GetMapping("/entreprise/{entrepriseId}")
    @Operation(summary = "Offres par entreprise")
    public ResponseEntity<List<RecruitmentOfferDTO>> getByEntreprise(@PathVariable Long entrepriseId) {
        return ResponseEntity.ok(offerService.getByEntreprise(entrepriseId));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le status de l'offre")
    public ResponseEntity<RecruitmentOfferDTO> updateStatus(@PathVariable Long id,
                                                            @RequestParam OfferStatus status) {
        return ResponseEntity.ok(offerService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/contre-offre")
    @Operation(summary = "Freelancer soumet une contre-offre")
    public ResponseEntity<RecruitmentOfferDTO> contreOffre(@PathVariable Long id,
                                                           @RequestParam String contreOffre) {
        return ResponseEntity.ok(offerService.addContreOffre(id, contreOffre));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RecruitmentOfferDTO>> getByStatus(@PathVariable OfferStatus status) {
        return ResponseEntity.ok(offerService.getByStatus(status));
    }
}