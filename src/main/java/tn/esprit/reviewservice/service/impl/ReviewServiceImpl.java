package tn.esprit.reviewservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.reviewservice.dto.request.ReviewRequest;
import tn.esprit.reviewservice.dto.response.ReviewResponse;
import tn.esprit.reviewservice.entity.Reclamation;
import tn.esprit.reviewservice.entity.Review;
import tn.esprit.reviewservice.entity.enums.MotifReclamation;
import tn.esprit.reviewservice.entity.enums.SentimentLabel;
import tn.esprit.reviewservice.entity.enums.StatusReclamation;
import tn.esprit.reviewservice.exception.DuplicateReviewException;
import tn.esprit.reviewservice.exception.ReviewNotFoundException;
import tn.esprit.reviewservice.mapper.ReviewMapper;
import tn.esprit.reviewservice.repository.ReclamationRepository;
import tn.esprit.reviewservice.repository.ReviewRepository;
import tn.esprit.reviewservice.service.interfaces.IGeminiService;
import tn.esprit.reviewservice.service.interfaces.IReviewService;
import tn.esprit.reviewservice.service.interfaces.ITrustScoreService;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final ITrustScoreService trustScoreService;
    private final IGeminiService geminiService;
    private final ReviewMapper mapper;
    private final ReclamationRepository reclamationRepository;

    @Override
    public ReviewResponse createReview(ReviewRequest request) {

        boolean alreadyExists = reviewRepository.existsByContractIdAndReviewerIdAndIsDeletedFalse(
                request.getContractId(),
                request.getReviewerId()
        );

        if (alreadyExists) {
            throw new DuplicateReviewException(
                    "Une review existe déjà pour contractId = " + request.getContractId()
                            + " et reviewerId = " + request.getReviewerId()
            );
        }

        trustScoreService.initializeTrustScore(request.getReviewedUserId());

        Double noteGlobale = calculateGlobalRating(
                request.getNoteQualite(),
                request.getNoteDelai(),
                request.getNoteCommunication(),
                request.getNotePrix()
        );

        var aiResult = geminiService.analyzeReview(
                request.getComment(),
                noteGlobale
        );

        Double  riskScore = aiResult.getRiskScore();
        boolean isFlagged = Boolean.TRUE.equals(aiResult.getFlagged())
                || (riskScore != null && riskScore > 70);

        Review review = Review.builder()
                .contractId(request.getContractId())
                .reviewerId(request.getReviewerId())
                .reviewedUserId(request.getReviewedUserId())
                .reviewType(request.getReviewType())
                .comment(request.getComment())
                .poids(request.getPoids() != null ? request.getPoids() : 1.0)

                .noteQualite(request.getNoteQualite())
                .noteDelai(request.getNoteDelai())
                .noteCommunication(request.getNoteCommunication())
                .notePrix(request.getNotePrix())

                .rating((int) Math.round(noteGlobale))

                .sentimentLabel(aiResult.getSentimentLabel())
                .riskScore(riskScore)
                .isFlagged(isFlagged)

                .build();

        Review saved = reviewRepository.saveAndFlush(review);

        if (isFlagged) {
            Reclamation reclamation = Reclamation.builder()
                    .review(saved)
                    .reportedByUserId(0L)
                    .motif(MotifReclamation.AUTO_DETECTED)
                    .description("Review détectée automatiquement comme suspecte par l'IA")
                    .status(StatusReclamation.PENDING)
                    .build();

            reclamationRepository.save(reclamation);
        }

        trustScoreService.recalculateTrustScore(request.getReviewedUserId());

        Review refreshed = reviewRepository.findById(saved.getId())
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Review introuvable après sauvegarde, id = " + saved.getId()
                ));

        return mapper.toReviewResponse(refreshed);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .orElseThrow(() -> new ReviewNotFoundException("Review introuvable avec id = " + id));

        return mapper.toReviewResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .map(mapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByReviewedUser(Long reviewedUserId) {
        return reviewRepository.findByReviewedUserIdAndIsDeletedFalse(reviewedUserId)
                .stream()
                .map(mapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByContract(Long contractId) {
        return reviewRepository.findByContractIdAndIsDeletedFalse(contractId)
                .stream()
                .map(mapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getFlaggedReviews() {
        return reviewRepository.findByIsFlaggedTrueAndIsDeletedFalse()
                .stream()
                .map(mapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getVisibleReviews() {
        return reviewRepository.findByIsVisibleTrueAndIsDeletedFalse()
                .stream()
                .map(mapper::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse softDeleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review introuvable avec id = " + id));

        review.setIsDeleted(true);
        review.setIsVisible(false);

        Review saved = reviewRepository.save(review);

        trustScoreService.recalculateTrustScore(saved.getReviewedUserId());

        return mapper.toReviewResponse(saved);
    }

    private Double calculateGlobalRating(Double noteQualite,
                                         Double noteDelai,
                                         Double noteCommunication,
                                         Double notePrix) {

        double q = noteQualite != null ? noteQualite : 0.0;
        double d = noteDelai != null ? noteDelai : 0.0;
        double c = noteCommunication != null ? noteCommunication : 0.0;
        double p = notePrix != null ? notePrix : 0.0;

        return (q + d + c + p) / 4.0;
    }


    @Override
    public ReviewResponse updateReview(Long id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .orElseThrow(() -> new ReviewNotFoundException("Review introuvable avec id = " + id));

        Double noteGlobale = calculateGlobalRating(
                request.getNoteQualite(),
                request.getNoteDelai(),
                request.getNoteCommunication(),
                request.getNotePrix()
        );

        var aiResult = geminiService.analyzeReview(
                request.getComment(),
                noteGlobale
        );

        Double  riskScore = aiResult.getRiskScore();
        boolean isFlagged =
                Boolean.TRUE.equals(aiResult.getFlagged())
                        || (riskScore != null && riskScore > 70)
                        || (aiResult.getSentimentLabel() != null
                        && aiResult.getSentimentLabel() == SentimentLabel.TOXIC);

        review.setContractId(request.getContractId());
        review.setReviewerId(request.getReviewerId());
        review.setReviewedUserId(request.getReviewedUserId());
        review.setReviewType(request.getReviewType());
        review.setComment(request.getComment());
        review.setPoids(request.getPoids() != null ? request.getPoids() : 1.0);

        review.setNoteQualite(request.getNoteQualite());
        review.setNoteDelai(request.getNoteDelai());
        review.setNoteCommunication(request.getNoteCommunication());
        review.setNotePrix(request.getNotePrix());

        review.setRating((int) Math.round(noteGlobale));

        review.setSentimentLabel(aiResult.getSentimentLabel());
        review.setRiskScore(riskScore);
        review.setIsFlagged(isFlagged);

        Review saved = reviewRepository.saveAndFlush(review);

        boolean autoReclamationExists = reclamationRepository.existsByReviewIdAndStatusAndReportedByUserId(
                saved.getId(),
                StatusReclamation.PENDING,
                0L
        );

        if (isFlagged && !autoReclamationExists) {
            Reclamation reclamation = Reclamation.builder()
                    .review(saved)
                    .reportedByUserId(0L)
                    .motif(MotifReclamation.AUTO_DETECTED)
                    .description("Review détectée automatiquement comme suspecte par l'IA après modification")
                    .status(StatusReclamation.PENDING)
                    .build();

            reclamationRepository.save(reclamation);
        }

        trustScoreService.recalculateTrustScore(saved.getReviewedUserId());

        return mapper.toReviewResponse(saved);
    }
}