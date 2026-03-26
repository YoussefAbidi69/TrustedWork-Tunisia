package tn.esprit.msrecruitmentservice.dto;

import tn.esprit.msrecruitmentservice.entities.ApplicationStatus;
import java.time.LocalDateTime;

public class RecruitmentApplicationDTO {

    private Long id;
    private Long jobPositionId;      // on envoie juste l'ID
    private String jobPositionTitre; // pour affichage

    private Long freelancerId;
    private Long entrepriseId;
    private String lettreMotivation;
    private Double pretentionSalariale;
    private String disponibilite;
    private Double matchingScore;
    private String scoreDetails;
    private ApplicationStatus status;
    private String motifRejet;
    private LocalDateTime datePostulation;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getJobPositionId() { return jobPositionId; }
    public void setJobPositionId(Long jobPositionId) { this.jobPositionId = jobPositionId; }

    public String getJobPositionTitre() { return jobPositionTitre; }
    public void setJobPositionTitre(String jobPositionTitre) { this.jobPositionTitre = jobPositionTitre; }

    public Long getFreelancerId() { return freelancerId; }
    public void setFreelancerId(Long freelancerId) { this.freelancerId = freelancerId; }

    public Long getEntrepriseId() { return entrepriseId; }
    public void setEntrepriseId(Long entrepriseId) { this.entrepriseId = entrepriseId; }

    public String getLettreMotivation() { return lettreMotivation; }
    public void setLettreMotivation(String lettreMotivation) { this.lettreMotivation = lettreMotivation; }

    public Double getPretentionSalariale() { return pretentionSalariale; }
    public void setPretentionSalariale(Double pretentionSalariale) { this.pretentionSalariale = pretentionSalariale; }

    public String getDisponibilite() { return disponibilite; }
    public void setDisponibilite(String disponibilite) { this.disponibilite = disponibilite; }

    public Double getMatchingScore() { return matchingScore; }
    public void setMatchingScore(Double matchingScore) { this.matchingScore = matchingScore; }

    public String getScoreDetails() { return scoreDetails; }
    public void setScoreDetails(String scoreDetails) { this.scoreDetails = scoreDetails; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public String getMotifRejet() { return motifRejet; }
    public void setMotifRejet(String motifRejet) { this.motifRejet = motifRejet; }

    public LocalDateTime getDatePostulation() { return datePostulation; }
    public void setDatePostulation(LocalDateTime datePostulation) { this.datePostulation = datePostulation; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}