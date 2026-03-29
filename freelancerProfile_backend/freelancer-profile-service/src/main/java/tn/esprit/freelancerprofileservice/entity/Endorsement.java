package tn.esprit.freelancerprofileservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "endorsements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endorsement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromProfileId; // Freelancer qui valide

    private Long toProfileId;   // Freelancer validé

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id")
    private SkillBadge skill;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    private Boolean isModerated = false;
}