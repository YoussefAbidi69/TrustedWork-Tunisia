package tn.esprit.reviewservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.reviewservice.dto.request.BadgeRequest;
import tn.esprit.reviewservice.dto.response.BadgeResponse;
import tn.esprit.reviewservice.service.interfaces.IBadgeService;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
@Tag(name = "Badge", description = "Gestion des badges")
public class BadgeController {

    private final IBadgeService badgeService;

    @Operation(summary = "Créer un nouveau badge")
    @PostMapping
    public ResponseEntity<BadgeResponse> createBadge(@RequestBody BadgeRequest request) {
        return ResponseEntity.ok(badgeService.createBadge(request));
    }

    @Operation(summary = "Récupérer tous les badges")
    @GetMapping
    public ResponseEntity<List<BadgeResponse>> getAllBadges() {
        return ResponseEntity.ok(badgeService.getAllBadges());
    }

    @Operation(summary = "Récupérer les badges actifs")
    @GetMapping("/active")
    public ResponseEntity<List<BadgeResponse>> getActiveBadges() {
        return ResponseEntity.ok(badgeService.getActiveBadges());
    }
}