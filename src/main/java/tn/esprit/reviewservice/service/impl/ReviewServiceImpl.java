package tn.esprit.reviewservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.reviewservice.entity.Review;
import tn.esprit.reviewservice.repository.ReviewRepository;
import tn.esprit.reviewservice.service.interfaces.IReviewService;

import java.util.List;
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements IReviewService {

    private final ReviewRepository reviewRepository;


@Override
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}