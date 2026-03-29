package tn.esprit.freelancerprofileservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.freelancerprofileservice.dto.*;
import tn.esprit.freelancerprofileservice.service.FreelancerProfileService;
import java.util.List;

@RestController
@RequestMapping("/api/endorsements")
@RequiredArgsConstructor
@Tag(name = "Endorsements", description = "Validation croisée entre freelancers")
public class EndorsementController {

    private final FreelancerProfileService profileService;

    @Operation(summary = "Créer un endorsement")
    @PostMapping
    public ResponseEntity<EndorsementResponse> createEndorsement(
            @RequestBody EndorsementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createEndorsement(request));
    }

    @Operation(summary = "Lister les endorsements d'un profil")
    @GetMapping("/{profileId}")
    public ResponseEntity<List<EndorsementResponse>> getEndorsements(
            @PathVariable Long profileId) {
        return ResponseEntity.ok(profileService.getEndorsementsByProfile(profileId));
    }

    @Operation(summary = "Récupérer un endorsement par ID")
    @GetMapping("/detail/{id}")
    public ResponseEntity<EndorsementResponse> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(profileService.getEndorsementById(id));
    }

    @Operation(summary = "Modifier un endorsement")
    @PutMapping("/{id}")
    public ResponseEntity<EndorsementResponse> updateEndorsement(
            @PathVariable Long id,
            @RequestBody EndorsementRequest request) {
        return ResponseEntity.ok(profileService.updateEndorsement(id, request));
    }

    @Operation(summary = "Supprimer un endorsement")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndorsement(@PathVariable Long id) {
        profileService.deleteEndorsement(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Modérer un endorsement (admin)")
    @PutMapping("/{id}/moderate")
    public ResponseEntity<Void> moderate(@PathVariable Long id) {
        profileService.moderateEndorsement(id);
        return ResponseEntity.ok().build();
    }
}