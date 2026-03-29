package tn.esprit.freelancerprofileservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "skill_badges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private FreelancerProfile profile;

    private String nomSkill;

    @Enumerated(EnumType.STRING)
    private Niveau niveau;

    private LocalDate dateValidation;

    private String certificatHash; // Hash MD5 anti-plagiat

    public enum Niveau {
        JUNIOR, CONFIRMED, EXPERT
    }
}