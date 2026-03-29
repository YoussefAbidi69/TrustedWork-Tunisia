package tn.esprit.reviewservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.reviewservice.dto.request.ReclamationRequest;
import tn.esprit.reviewservice.dto.response.ReclamationResponse;
import tn.esprit.reviewservice.entity.Reclamation;
import tn.esprit.reviewservice.entity.Review;
import tn.esprit.reviewservice.entity.enums.StatusReclamation;
import tn.esprit.reviewservice.exception.ReclamationNotFoundException;
import tn.esprit.reviewservice.exception.ReviewNotFoundException;
import tn.esprit.reviewservice.mapper.ReviewMapper;
import tn.esprit.reviewservice.repository.ReclamationRepository;
import tn.esprit.reviewservice.repository.ReviewRepository;
import tn.esprit.reviewservice.service.interfaces.IReclamationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReclamationServiceImpl implements IReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper mapper;

    @Override
    public ReclamationResponse createReclamation(ReclamationRequest request) {
        Review review = reviewRepository.findById(request.getReviewId())
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Review introuvable avec id = " + request.getReviewId()
                ));

        Reclamation reclamation = Reclamation.builder()
                .reportedByUserId(request.getReportedByUserId())
                .motif(request.getMotif())
                .description(request.getDescription())
                .status(StatusReclamation.PENDING)
                .review(review)
                .build();

        Reclamation saved = reclamationRepository.save(reclamation);

        review.setIsFlagged(true);
        reviewRepository.save(review);

        return mapper.toReclamationResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReclamationResponse getById(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new ReclamationNotFoundException(
                        "Reclamation introuvable avec id = " + id
                ));

        return mapper.toReclamationResponse(reclamation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReclamationResponse> getAllReclamations() {
        return reclamationRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toReclamationResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReclamationResponse> getByStatus(StatusReclamation status) {
        return reclamationRepository.findByStatusOrdered(status)
                .stream()
                .map(mapper::toReclamationResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReclamationResponse> getPendingReclamations() {
        return reclamationRepository.findPendingReclamations()
                .stream()
                .map(mapper::toReclamationResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReclamationResponse> getByReviewId(Long reviewId) {
        return reclamationRepository.findByReviewId(reviewId)
                .stream()
                .map(mapper::toReclamationResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReclamationResponse> getByReportedByUserId(Long reportedByUserId) {
        return reclamationRepository.findUserReclamations(reportedByUserId)
                .stream()
                .map(mapper::toReclamationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReclamationResponse confirmReclamation(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new ReclamationNotFoundException(
                        "Reclamation introuvable avec id = " + id
                ));

        reclamation.setStatus(StatusReclamation.CONFIRMED);
        reclamation.setResolvedAt(LocalDateTime.now());

        Review review = reclamation.getReview();
        review.setIsFlagged(true);
        review.setIsVisible(false);
        reviewRepository.save(review);

        Reclamation saved = reclamationRepository.save(reclamation);
        return mapper.toReclamationResponse(saved);
    }

    @Override
    public ReclamationResponse dismissReclamation(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new ReclamationNotFoundException(
                        "Reclamation introuvable avec id = " + id
                ));

        reclamation.setStatus(StatusReclamation.DISMISSED);
        reclamation.setResolvedAt(LocalDateTime.now());

        Review review = reclamation.getReview();
        review.setIsVisible(true);
        review.setIsFlagged(false);
        reviewRepository.save(review);

        Reclamation saved = reclamationRepository.save(reclamation);
        return mapper.toReclamationResponse(saved);
    }
}