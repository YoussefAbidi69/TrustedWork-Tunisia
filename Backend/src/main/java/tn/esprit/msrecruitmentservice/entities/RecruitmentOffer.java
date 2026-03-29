package tn.esprit.msrecruitmentservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "recruitment_offer")
public class RecruitmentOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    private RecruitmentApplication application;

    private Double salairePropose;
    private String posteExact;
    private LocalDateTime dateDebutSouhaitee;
    private Integer periodeEssaiMois;

    @Column(columnDefinition = "TEXT")
    private String avantages;

    private LocalDateTime deadlineReponse;

    @Column(columnDefinition = "TEXT")
    private String contreOffreFreelancer;

    @Enumerated(EnumType.STRING)
    private OfferStatus status = OfferStatus.SENT;

    @Column(updatable = false)
    private LocalDateTime dateEnvoi;

    @PrePersist
    protected void onCreate() {
        this.dateEnvoi = LocalDateTime.now();
    }
}