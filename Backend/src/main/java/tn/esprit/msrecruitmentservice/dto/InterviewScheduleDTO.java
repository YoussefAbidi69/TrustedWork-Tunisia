package tn.esprit.msrecruitmentservice.dto;

import tn.esprit.msrecruitmentservice.entities.InterviewStatus;
import tn.esprit.msrecruitmentservice.entities.InterviewType;
import java.time.LocalDateTime;

public class InterviewScheduleDTO {

    private Long id;
    private Long applicationId;
    private InterviewType type;
    private Integer ordreEntretien;
    private LocalDateTime dateFinalConfirmee;
    private Integer dureePrevueMinutes;
    private String lienVisio;
    private InterviewStatus status;
    private String feedbackRecruteur;
    private Integer noteRecruteur;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public InterviewType getType() { return type; }
    public void setType(InterviewType type) { this.type = type; }

    public Integer getOrdreEntretien() { return ordreEntretien; }
    public void setOrdreEntretien(Integer ordreEntretien) { this.ordreEntretien = ordreEntretien; }

    public LocalDateTime getDateFinalConfirmee() { return dateFinalConfirmee; }
    public void setDateFinalConfirmee(LocalDateTime dateFinalConfirmee) { this.dateFinalConfirmee = dateFinalConfirmee; }

    public Integer getDureePrevueMinutes() { return dureePrevueMinutes; }
    public void setDureePrevueMinutes(Integer dureePrevueMinutes) { this.dureePrevueMinutes = dureePrevueMinutes; }

    public String getLienVisio() { return lienVisio; }
    public void setLienVisio(String lienVisio) { this.lienVisio = lienVisio; }

    public InterviewStatus getStatus() { return status; }
    public void setStatus(InterviewStatus status) { this.status = status; }

    public String getFeedbackRecruteur() { return feedbackRecruteur; }
    public void setFeedbackRecruteur(String feedbackRecruteur) { this.feedbackRecruteur = feedbackRecruteur; }

    public Integer getNoteRecruteur() { return noteRecruteur; }
    public void setNoteRecruteur(Integer noteRecruteur) { this.noteRecruteur = noteRecruteur; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}