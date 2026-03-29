package tn.esprit.mscontractservicee.service;

import tn.esprit.mscontractservicee.entity.Wallet;
import java.math.BigDecimal;

public interface IWalletService {

    Wallet getOrCreateWallet(Long userId);

    Wallet createWallet(Long userId);

    Wallet credit(Long userId, BigDecimal amount, String description);

    Wallet debit(Long userId, BigDecimal amount, String description);

    String createStripeAccount(Long userId, String email, String country) throws Exception;

    String getStripeAccountStatus(Long userId) throws Exception;

    String getOnboardingLink(Long userId) throws Exception;
}