package tn.esprit.msrecruitmentservice.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "interview_schedule")
public class InterviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private RecruitmentApplication application;

    @Enumerated(EnumType.STRING)
    private InterviewType type;

    private Integer ordreEntretien;
    private LocalDateTime dateFinalConfirmee;
    private Integer dureePrevueMinutes;
    private String lienVisio;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status = InterviewStatus.PROPOSED;

    @Column(columnDefinition = "TEXT")
    private String feedbackRecruteur;

    private Integer noteRecruteur;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}