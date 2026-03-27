package tn.esprit.reviewservice.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.reviewservice.entity.enums.CategorieConfiance;
import tn.esprit.reviewservice.entity.enums.Tendance;

import java.time.LocalDateTime;

@Entity
@Table(name = "trust_score")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrustScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Builder.Default
    private Double score = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Double averageRating = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private Integer totalReviews = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer positiveReviews = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer negativeReviews = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tendance tendance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategorieConfiance categorie;

    @Column(nullable = false)
    private LocalDateTime lastCalculatedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (score == null) score = 0.0;
        if (averageRating == null) averageRating = 0.0;
        if (totalReviews == null) totalReviews = 0;
        if (positiveReviews == null) positiveReviews = 0;
        if (negativeReviews == null) negativeReviews = 0;
        if (tendance == null) tendance = Tendance.STABLE;
        if (lastCalculatedAt == null) lastCalculatedAt = LocalDateTime.now();

        updateCategorie();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateCategorie();
        updatedAt = LocalDateTime.now();
    }

    public void updateCategorie() {
        if (score == null) {
            categorie = CategorieConfiance.LOW;
            return;
        }

        if (score >= 85) {
            categorie = CategorieConfiance.ELITE;
        } else if (score >= 70) {
            categorie = CategorieConfiance.HIGH;
        } else if (score >= 50) {
            categorie = CategorieConfiance.MEDIUM;
        } else {
            categorie = CategorieConfiance.LOW;
        }
    }
}