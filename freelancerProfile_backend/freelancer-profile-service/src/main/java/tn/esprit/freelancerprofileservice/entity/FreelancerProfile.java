package tn.esprit.freelancerprofileservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "freelancer_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // Référence vers ms-user-service

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String titre;

    @Column(precision = 10, scale = 2)
    private BigDecimal tauxHoraire;

    private String localisation;

    @Enumerated(EnumType.STRING)
    private Disponibilite disponibilite;

    private String trustPassportUrl;

    private String domaineExpertise;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SkillBadge> skillBadges;

    public enum Disponibilite {
        AVAILABLE, BUSY, NOT_AVAILABLE
    }
}