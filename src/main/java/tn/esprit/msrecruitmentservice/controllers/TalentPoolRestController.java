package tn.esprit.msrecruitmentservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.msrecruitmentservice.dto.TalentPoolDTO;
import tn.esprit.msrecruitmentservice.entities.TalentTag;
import tn.esprit.msrecruitmentservice.services.ITalentPoolService;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/talent-pool")
@Tag(name = "TalentPool", description = "Gestion du vivier de talents")
public class TalentPoolRestController {

    @Autowired
    private ITalentPoolService talentPoolService;

    @PostMapping
    @Operation(summary = "Ajouter un freelancer au vivier")
    public ResponseEntity<TalentPoolDTO> add(@RequestBody TalentPoolDTO dto) {
        return ResponseEntity.ok(talentPoolService.addToPool(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un talent du vivier")
    public ResponseEntity<TalentPoolDTO> update(@PathVariable Long id,
                                                @RequestBody TalentPoolDTO dto) {
        return ResponseEntity.ok(talentPoolService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Retirer un freelancer du vivier")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        talentPoolService.removeFromPool(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recuperer un talent par ID")
    public ResponseEntity<TalentPoolDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(talentPoolService.getById(id));
    }

    @GetMapping("/entreprise/{entrepriseId}")
    @Operation(summary = "Vivier complet d'une entreprise")
    public ResponseEntity<List<TalentPoolDTO>> getByEntreprise(@PathVariable Long entrepriseId) {
        return ResponseEntity.ok(talentPoolService.getByEntreprise(entrepriseId));
    }

    @GetMapping("/entreprise/{entrepriseId}/tag/{tag}")
    @Operation(summary = "Filtrer le vivier par tag")
    public ResponseEntity<List<TalentPoolDTO>> getByTag(@PathVariable Long entrepriseId,
                                                        @PathVariable TalentTag tag) {
        return ResponseEntity.ok(talentPoolService.getByEntrepriseAndTag(entrepriseId, tag));
    }
}