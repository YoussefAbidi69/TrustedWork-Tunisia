package tn.esprit.freelancerprofileservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certification_exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificationExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String domaine; // Dev / Design / Rédaction / Traduction / Marketing

    @Column(columnDefinition = "JSON")
    private String questions; // JSON généré par Gemini Flash

    private Integer dureeMinutes;

    private Integer scoreMinimum;

    private Integer tentatives;

    @Column(columnDefinition = "JSON")
    private String baremeConfig; // JSON points + pénalités
}