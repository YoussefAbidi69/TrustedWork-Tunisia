package tn.esprit.mscontractservicee.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mscontractservicee.service.IWalletService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final IWalletService walletService;  // ✅ Interface

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getWallet(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getOrCreateWallet(userId));
    }

    @PostMapping("/user/{userId}/credit")
    public ResponseEntity<?> credit(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(walletService.credit(userId, amount, "Manual credit"));
    }

    @PostMapping("/stripe/connect/{userId}")
    public ResponseEntity<?> createStripeAccount(
            @PathVariable Long userId,
            @RequestParam String email,
            @RequestParam(defaultValue = "TN") String country) {
        try {
            String accountId = walletService.createStripeAccount(userId, email, country);
            String link = walletService.getOnboardingLink(userId);
            Map<String, String> res = new HashMap<>();
            res.put("stripeAccountId", accountId);
            res.put("onboardingLink", link);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stripe/status/{userId}")
    public ResponseEntity<?> getStripeStatus(@PathVariable Long userId) {
        try {
            String status = walletService.getStripeAccountStatus(userId);
            return ResponseEntity.ok(Map.of("status", status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}