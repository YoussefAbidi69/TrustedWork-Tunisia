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
@RequestMapping("/api/skills")
@RequiredArgsConstructor
@Tag(name = "Skill Badge", description = "Gestion des compétences certifiées")
public class SkillBadgeController {

    private final FreelancerProfileService profileService;

    @Operation(summary = "Ajouter un SkillBadge")
    @PostMapping
    public ResponseEntity<SkillBadgeResponse> addSkill(
            @RequestBody SkillBadgeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(profileService.addSkill(request));
    }

    @Operation(summary = "Lister tous les SkillBadges")
    @GetMapping
    public ResponseEntity<List<SkillBadgeResponse>> getAllSkills() {
        return ResponseEntity.ok(profileService.getAllSkills());
    }

    @Operation(summary = "Récupérer un SkillBadge par ID")
    @GetMapping("/{id}")
    public ResponseEntity<SkillBadgeResponse> getSkillById(
            @PathVariable Long id) {
        return ResponseEntity.ok(profileService.getSkillById(id));
    }

    @Operation(summary = "Modifier un SkillBadge")
    @PutMapping("/{id}")
    public ResponseEntity<SkillBadgeResponse> updateSkill(
            @PathVariable Long id,
            @RequestBody SkillBadgeRequest request) {
        return ResponseEntity.ok(profileService.updateSkill(id, request));
    }

    @Operation(summary = "Supprimer un SkillBadge")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        profileService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}