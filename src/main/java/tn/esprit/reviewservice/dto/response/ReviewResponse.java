package tn.esprit.reviewservice.dto.response;

import lombok.*;
import tn.esprit.reviewservice.entity.enums.ReviewType;
import tn.esprit.reviewservice.entity.enums.SentimentLabel;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private Long contractId;
    private Long reviewerId;
    private Long reviewedUserId;
    private ReviewType reviewType;

    // rating global calculé automatiquement
    private Integer rating;

    private String comment;

    private Double poids;

    // notes détaillées
    private Double noteQualite;
    private Double noteDelai;
    private Double noteCommunication;
    private Double notePrix;

    // note pondérée calculée automatiquement
    private Double notePonderee;

    private SentimentLabel sentimentLabel;
    private Boolean isFlagged;
    private Boolean isVisible;
    private Boolean isDeleted;
    private Double riskScore;
    private Long trustScoreId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}