package tn.esprit.reviewservice.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.reviewservice.entity.enums.ReviewType;
import tn.esprit.reviewservice.entity.enums.SentimentLabel;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contractId;

    private Long reviewerId;

    private Long reviewedUserId;

    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;

    private Integer rating;

    @Column(length = 2000)
    private String comment;

    private Double poids;

    private Double noteQualite;

    private Double noteDelai;

    private Double noteCommunication;

    private Double notePrix;

    private Double notePonderee;

    @Enumerated(EnumType.STRING)
    private SentimentLabel sentimentLabel;

    @Builder.Default
    private Boolean isFlagged = false;

    @Builder.Default
    private Boolean isVisible = true;

    @Builder.Default
    private Boolean isDeleted = false;

    private Double riskScore;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trust_score_id")
    private TrustScore trustScore;

    @PrePersist
    public void prePersist() {
        if (poids == null) poids = 1.0;
        if (isFlagged == null) isFlagged = false;
        if (isVisible == null) isVisible = true;
        if (isDeleted == null) isDeleted = false;
        if (riskScore == null) riskScore = 0.0;
        if (sentimentLabel == null) sentimentLabel = SentimentLabel.NEUTRAL;

        calculateMultiCriteriaScores();

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        if (poids == null) poids = 1.0;
        if (riskScore == null) riskScore = 0.0;

        calculateMultiCriteriaScores();

        updatedAt = LocalDateTime.now();
    }

    public void calculateMultiCriteriaScores() {
        double q = noteQualite != null ? noteQualite : 0.0;
        double d = noteDelai != null ? noteDelai : 0.0;
        double c = noteCommunication != null ? noteCommunication : 0.0;
        double p = notePrix != null ? notePrix : 0.0;

        this.notePonderee = (q * 0.4) + (d * 0.25) + (c * 0.2) + (p * 0.15);

        this.rating = (int) Math.round((q + d + c + p) / 4.0);
    }
}