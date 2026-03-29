package tn.esprit.reviewservice.service.interfaces;

import tn.esprit.reviewservice.dto.request.ReclamationRequest;
import tn.esprit.reviewservice.dto.response.ReclamationResponse;
import tn.esprit.reviewservice.entity.enums.StatusReclamation;

import java.util.List;

public interface IReclamationService {

    ReclamationResponse createReclamation(ReclamationRequest request);

    ReclamationResponse getById(Long id);

    List<ReclamationResponse> getAllReclamations();

    List<ReclamationResponse> getByStatus(StatusReclamation status);

    List<ReclamationResponse> getPendingReclamations();

    List<ReclamationResponse> getByReviewId(Long reviewId);

    List<ReclamationResponse> getByReportedByUserId(Long reportedByUserId);

    ReclamationResponse confirmReclamation(Long id);

    ReclamationResponse dismissReclamation(Long id);
}