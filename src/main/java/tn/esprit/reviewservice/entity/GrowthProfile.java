package tn.esprit.reviewservice.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.reviewservice.entity.enums.Niveau;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrowthProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Builder.Default
    private Integer xp = 0;

    @Builder.Default
    private Integer level = 1;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    @Builder.Default
    private Integer streakDays = 0;

    @Builder.Default
    private Integer longestStreak = 0;

    private LocalDate lastActivityDate;

    @Builder.Default
    private Integer badgesCount = 0;

    @Builder.Default
    private Boolean profileCompleted = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (xp == null) xp = 0;
        if (level == null) level = 1;
        if (streakDays == null) streakDays = 0;
        if (longestStreak == null) longestStreak = 0;
        if (badgesCount == null) badgesCount = 0;
        if (profileCompleted == null) profileCompleted = false;
        updateNiveau();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateNiveau();
        updatedAt = LocalDateTime.now();
    }

    public void updateNiveau() {
        if (xp == null) {
            niveau = Niveau.BRONZE;
            level = 1;
            return;
        }

        if (xp >= 5000) {
            niveau = Niveau.ELITE;
            level = 5;
        } else if (xp >= 2500) {
            niveau = Niveau.PLATINUM;
            level = 4;
        } else if (xp >= 1200) {
            niveau = Niveau.GOLD;
            level = 3;
        } else if (xp >= 500) {
            niveau = Niveau.SILVER;
            level = 2;
        } else {
            niveau = Niveau.BRONZE;
            level = 1;
        }
    }
}