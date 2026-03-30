package tn.esprit.reviewservice.service.interfaces;

import tn.esprit.reviewservice.dto.request.ReviewRequest;
import tn.esprit.reviewservice.dto.response.ReviewResponse;

import java.util.List;

public interface IReviewService {

    ReviewResponse createReview(ReviewRequest request);

    ReviewResponse updateReview(Long id, ReviewRequest request);

    ReviewResponse getReviewById(Long id);

    List<ReviewResponse> getAllReviews();

    List<ReviewResponse> getReviewsByReviewedUser(Long reviewedUserId);

    List<ReviewResponse> getReviewsByContract(Long contractId);

    List<ReviewResponse> getFlaggedReviews();

    List<ReviewResponse> getVisibleReviews();

    ReviewResponse softDeleteReview(Long id);
}