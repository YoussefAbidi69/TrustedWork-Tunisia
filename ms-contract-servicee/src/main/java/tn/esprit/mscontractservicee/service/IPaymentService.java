package tn.esprit.mscontractservicee.service;

import tn.esprit.mscontractservicee.dto.PaymentIntentResponse;
import java.math.BigDecimal;

public interface IPaymentService {

    PaymentIntentResponse createPaymentIntent(Long contractId, String email) throws Exception;

    void confirmPayment(String paymentIntentId, Long contractId) throws Exception;

    void releasePaymentToFreelancer(Long contractId, Long milestoneId, BigDecimal amount) throws Exception;

    String getPaymentStatus(String paymentIntentId) throws Exception;

}