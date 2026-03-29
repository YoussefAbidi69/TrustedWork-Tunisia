package tn.esprit.msrecruitmentservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.msrecruitmentservice.dto.HiringContractDTO;
import tn.esprit.msrecruitmentservice.services.IHiringContractService;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/contracts")
@Tag(name = "HiringContract", description = "Gestion des contrats finaux CDI/CDD")
public class HiringContractRestController {

    @Autowired
    private IHiringContractService contractService;

    @PostMapping
    @Operation(summary = "Creer un contrat")
    public ResponseEntity<HiringContractDTO> create(@RequestBody HiringContractDTO dto) {
        return ResponseEntity.ok(contractService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un contrat")
    public ResponseEntity<HiringContractDTO> update(@PathVariable Long id,
                                                    @RequestBody HiringContractDTO dto) {
        return ResponseEntity.ok(contractService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un contrat")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contractService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recuperer un contrat par ID")
    public ResponseEntity<HiringContractDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.getById(id));
    }

    @GetMapping
    @Operation(summary = "Tous les contrats")
    public ResponseEntity<List<HiringContractDTO>> getAll() {
        return ResponseEntity.ok(contractService.getAll());
    }

    @GetMapping("/offer/{offerId}")
    @Operation(summary = "Contrat par offre")
    public ResponseEntity<HiringContractDTO> getByOffer(@PathVariable Long offerId) {
        return ResponseEntity.ok(contractService.getByOffer(offerId));
    }

    @GetMapping("/freelancer/{freelancerId}")
    @Operation(summary = "Contrats par freelancer")
    public ResponseEntity<List<HiringContractDTO>> getByFreelancer(@PathVariable Long freelancerId) {
        return ResponseEntity.ok(contractService.getByFreelancer(freelancerId));
    }

    @GetMapping("/entreprise/{entrepriseId}")
    @Operation(summary = "Contrats par entreprise")
    public ResponseEntity<List<HiringContractDTO>> getByEntreprise(@PathVariable Long entrepriseId) {
        return ResponseEntity.ok(contractService.getByEntreprise(entrepriseId));
    }

    @PatchMapping("/{id}/signer")
    @Operation(summary = "Signer le contrat — status passe a SIGNED")
    public ResponseEntity<HiringContractDTO> signer(@PathVariable Long id) {
        return ResponseEntity.ok(contractService.signerContrat(id));
    }

    @PatchMapping("/{id}/feedback")
    @Operation(summary = "Ajouter feedback post-embauche 3 mois")
    public ResponseEntity<HiringContractDTO> feedback(@PathVariable Long id,
                                                      @RequestParam String feedback) {
        return ResponseEntity.ok(contractService.addFeedback(id, feedback));
    }
}