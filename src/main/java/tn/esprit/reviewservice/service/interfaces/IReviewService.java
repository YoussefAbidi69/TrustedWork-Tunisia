package tn.esprit.reviewservice.service.interfaces;

import tn.esprit.reviewservice.entity.Review;

import java.util.List;

public interface IReviewService {

    Review createReview(Review review);

    List<Review> getAllReviews();

    Review getReviewById(Long id);

    void deleteReview(Long id);
}