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
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "Freelancer Profile", description = "Gestion des profils freelancers")
public class FreelancerProfileController {

    private final FreelancerProfileService profileService;

    @Operation(summary = "Créer un profil freelancer")
    @PostMapping
    public ResponseEntity<FreelancerProfileResponse> createProfile(
            @RequestBody FreelancerProfileRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.createProfile(request));
    }

    @Operation(summary = "Récupérer un profil par ID")
    @GetMapping("/{id}")
    public ResponseEntity<FreelancerProfileResponse> getProfile(
            @PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProfileById(id));
    }

    @Operation(summary = "Lister tous les profils")
    @GetMapping
    public ResponseEntity<List<FreelancerProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @Operation(summary = "Mettre à jour un profil")
    @PutMapping("/{id}")
    public ResponseEntity<FreelancerProfileResponse> updateProfile(
            @PathVariable Long id,
            @RequestBody FreelancerProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(id, request));
    }

    @Operation(summary = "Supprimer un profil")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lister les SkillBadges d'un profil")
    @GetMapping("/{id}/skills")
    public ResponseEntity<List<SkillBadgeResponse>> getSkillsByProfile(
            @PathVariable Long id) {
        return ResponseEntity.ok(profileService.getSkillsByProfile(id));
    }
}