package tn.esprit.reviewservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.reviewservice.entity.enums.MotifReclamation;
import tn.esprit.reviewservice.entity.enums.StatusReclamation;

import java.time.LocalDateTime;

@Entity
@Table(name = "reclamations")
@Getter
@Setter
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reviewId;

    private Long reportedByUserId;

    @Enumerated(EnumType.STRING)
    private MotifReclamation motif;

    private String description;

    @Enumerated(EnumType.STRING)
    private StatusReclamation status;

    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = StatusReclamation.PENDING;
        }
    }
}