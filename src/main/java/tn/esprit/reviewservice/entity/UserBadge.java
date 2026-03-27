package tn.esprit.reviewservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "growth_profile_id")
    private GrowthProfile growthProfile;

    @Builder.Default
    private Boolean showcased = false;

    private LocalDateTime earnedAt;

    @PrePersist
    public void prePersist() {
        if (showcased == null) showcased = false;
        if (earnedAt == null) earnedAt = LocalDateTime.now();
    }
}