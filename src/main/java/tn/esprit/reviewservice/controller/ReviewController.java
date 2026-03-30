package tn.esprit.reviewservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.reviewservice.dto.ai.AdminInsightDTO;
import tn.esprit.reviewservice.dto.ai.ReviewSummaryDTO;
import tn.esprit.reviewservice.dto.request.ReviewRequest;
import tn.esprit.reviewservice.dto.response.ReviewResponse;
import tn.esprit.reviewservice.entity.Review;
import tn.esprit.reviewservice.repository.ReviewRepository;
import tn.esprit.reviewservice.service.interfaces.IGeminiService;
import tn.esprit.reviewservice.service.interfaces.IReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;
    private final IGeminiService geminiService;
    private final ReviewRepository reviewRepository;

    @PostMapping
    public ReviewResponse createReview(@RequestBody ReviewRequest request) {
        return reviewService.createReview(request);
    }

    @PutMapping("/{id}")
    public ReviewResponse updateReview(@PathVariable Long id, @RequestBody ReviewRequest request) {
        return reviewService.updateReview(id, request);
    }

    @GetMapping
    public List<ReviewResponse> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ReviewResponse getReview(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping("/user/{userId}")
    public List<ReviewResponse> getReviewsByUser(@PathVariable Long userId) {
        return reviewService.getReviewsByReviewedUser(userId);
    }

    @GetMapping("/contract/{contractId}")
    public List<ReviewResponse> getReviewsByContract(@PathVariable Long contractId) {
        return reviewService.getReviewsByContract(contractId);
    }

    @GetMapping("/visible")
    public List<ReviewResponse> getVisibleReviews() {
        return reviewService.getVisibleReviews();
    }

    @GetMapping("/flagged")
    public List<ReviewResponse> getFlaggedReviews() {
        return reviewService.getFlaggedReviews();
    }

    @DeleteMapping("/{id}")
    public ReviewResponse softDeleteReview(@PathVariable Long id) {
        return reviewService.softDeleteReview(id);
    }

    @GetMapping("/summary/{userId}")
    public ReviewSummaryDTO getUserSummary(@PathVariable Long userId) {
        List<Review> reviews = reviewRepository.findByReviewedUserIdAndIsDeletedFalse(userId);

        if (reviews.isEmpty()) {
            ReviewSummaryDTO empty = new ReviewSummaryDTO();
            empty.setOverall("Aucune review disponible.");
            empty.setStrengths("Aucune review disponible.");
            empty.setWeaknesses("Aucune review disponible.");
            return empty;
        }

        String allComments = reviews.stream()
                .map(Review::getComment)
                .filter(c -> c != null && !c.isBlank())
                .reduce("", (a, b) -> a + "\n" + b);

        return geminiService.summarizeUserReviews(allComments);
    }

    @GetMapping("/admin-insight/{reviewId}")
    public AdminInsightDTO getAdminInsight(@PathVariable Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review introuvable"));

        return geminiService.generateAdminInsight(
                review.getComment(),
                "Possible abusive review"
        );
    }
}