package tn.esprit.reviewservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.reviewservice.dto.response.TrustScoreResponse;
import tn.esprit.reviewservice.service.interfaces.ITrustScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/trust-scores")
@RequiredArgsConstructor
@Tag(name = "Trust Score", description = "Gestion des scores de confiance")
public class TrustScoreController {

    private final ITrustScoreService trustScoreService;

    @Operation(summary = "Initialiser un trust score pour un utilisateur")
    @PostMapping("/init/{userId}")
    public ResponseEntity<TrustScoreResponse> initializeTrustScore(@PathVariable Long userId) {
        return ResponseEntity.ok(trustScoreService.initializeTrustScore(userId));
    }

    @Operation(summary = "Récupérer un trust score par userId")
    @GetMapping("/user/{userId}")
    public ResponseEntity<TrustScoreResponse> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(trustScoreService.getByUserId(userId));
    }

    @Operation(summary = "Recalculer le trust score d'un utilisateur")
    @PutMapping("/recalculate/{userId}")
    public ResponseEntity<TrustScoreResponse> recalculateTrustScore(@PathVariable Long userId) {
        return ResponseEntity.ok(trustScoreService.recalculateTrustScore(userId));
    }

    @Operation(summary = "Récupérer le leaderboard des trust scores")
    @GetMapping("/leaderboard")
    public ResponseEntity<List<TrustScoreResponse>> getLeaderboard() {
        return ResponseEntity.ok(trustScoreService.getLeaderboard());
    }

    @Operation(summary = "Récupérer tous les trust scores")
    @GetMapping
    public ResponseEntity<List<TrustScoreResponse>> getAllTrustScores() {
        return ResponseEntity.ok(trustScoreService.getAllTrustScores());
    }
}