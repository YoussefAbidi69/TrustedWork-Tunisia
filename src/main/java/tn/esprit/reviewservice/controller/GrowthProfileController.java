package tn.esprit.reviewservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.reviewservice.dto.request.AddXpRequest;
import tn.esprit.reviewservice.dto.response.GrowthProfileResponse;
import tn.esprit.reviewservice.service.interfaces.IGrowthProfileService;

import java.util.List;

@RestController
@RequestMapping("/api/growth-profiles")
@RequiredArgsConstructor
@Tag(name = "Growth Profile", description = "Gestion des profils de progression")
public class GrowthProfileController {

    private final IGrowthProfileService growthProfileService;

    @Operation(summary = "Initialiser un profil de progression")
    @PostMapping("/init/{userId}")
    public ResponseEntity<GrowthProfileResponse> initializeProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(growthProfileService.initializeProfile(userId));
    }

    @Operation(summary = "Récupérer un profil de progression par userId")
    @GetMapping("/user/{userId}")
    public ResponseEntity<GrowthProfileResponse> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(growthProfileService.getByUserId(userId));
    }

    @Operation(summary = "Ajouter de l'XP à un utilisateur")
    @PutMapping("/add-xp")
    public ResponseEntity<GrowthProfileResponse> addXp(@RequestBody AddXpRequest request) {
        return ResponseEntity.ok(growthProfileService.addXp(request));
    }

    @Operation(summary = "Récupérer le leaderboard des profils")
    @GetMapping("/leaderboard")
    public ResponseEntity<List<GrowthProfileResponse>> getLeaderboard() {
        return ResponseEntity.ok(growthProfileService.getLeaderboard());
    }

    @Operation(summary = "Récupérer tous les profils de progression")
    @GetMapping
    public ResponseEntity<List<GrowthProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(growthProfileService.getAllProfiles());
    }
}