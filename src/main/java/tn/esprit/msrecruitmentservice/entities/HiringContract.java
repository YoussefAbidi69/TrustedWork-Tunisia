package tn.esprit.msrecruitmentservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "hiring_contract")
public class HiringContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "offer_id", nullable = false)
    private RecruitmentOffer offer;

    private Long freelancerId;
    private Long entrepriseId;

    @Enumerated(EnumType.STRING)
    private TypeContrat typeContrat;

    private Double salaireFinal;
    private LocalDateTime dateDebutEffective;
    private Integer periodeEssai;
    private Double commissionPlateforme = 10.0;

    @Column(columnDefinition = "TEXT")
    private String feedbackPostEmbauche3Mois;

    @Enumerated(EnumType.STRING)
    private ContractStatus status = ContractStatus.DRAFT;

    @Column(updatable = false)
    private LocalDateTime dateContratSigne;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}