package tn.esprit.msrecruitmentservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "recruitment_application")
public class RecruitmentApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_position_id", nullable = false)
    private JobPosition jobPosition;

    private Long freelancerId;       // mock Phase 1 : valeur fixe 1L
    private Long entrepriseId;

    @Column(columnDefinition = "TEXT")
    private String lettreMotivation;

    private Double pretentionSalariale;
    private String disponibilite;

    private Double matchingScore = 75.0;   // mock Phase 1

    @Column(columnDefinition = "TEXT")
    private String scoreDetails;           // ex: "Skills:80%, Experience:70%"

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(columnDefinition = "TEXT")
    private String motifRejet;

    @Column(updatable = false)
    private LocalDateTime datePostulation;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.datePostulation = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) this.status = ApplicationStatus.SUBMITTED;
        if (this.matchingScore == null) this.matchingScore = 75.0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}