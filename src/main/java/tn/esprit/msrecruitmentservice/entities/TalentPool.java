package tn.esprit.msrecruitmentservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "talent_pool")
public class TalentPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long entrepriseId;
    private Long freelancerId;

    @Enumerated(EnumType.STRING)
    private TalentTag tag;

    @Enumerated(EnumType.STRING)
    private SourceOrigine sourceOrigine;

    private Boolean alerteDisponibilite = false;

    @Column(columnDefinition = "TEXT")
    private String notesPrivees;

    @Column(updatable = false)
    private LocalDateTime dateAjout;

    @PrePersist
    protected void onCreate() {
        this.dateAjout = LocalDateTime.now();
    }
}