package tn.esprit.msrecruitmentservice.dto;

import tn.esprit.msrecruitmentservice.entities.ContractStatus;
import tn.esprit.msrecruitmentservice.entities.TypeContrat;
import java.time.LocalDateTime;

public class HiringContractDTO {

    private Long id;
    private Long offerId;
    private Long freelancerId;
    private Long entrepriseId;
    private TypeContrat typeContrat;
    private Double salaireFinal;
    private LocalDateTime dateDebutEffective;
    private Integer periodeEssai;
    private Double commissionPlateforme;
    private String feedbackPostEmbauche3Mois;
    private ContractStatus status;
    private LocalDateTime dateContratSigne;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOfferId() { return offerId; }
    public void setOfferId(Long offerId) { this.offerId = offerId; }

    public Long getFreelancerId() { return freelancerId; }
    public void setFreelancerId(Long freelancerId) { this.freelancerId = freelancerId; }

    public Long getEntrepriseId() { return entrepriseId; }
    public void setEntrepriseId(Long entrepriseId) { this.entrepriseId = entrepriseId; }

    public TypeContrat getTypeContrat() { return typeContrat; }
    public void setTypeContrat(TypeContrat typeContrat) { this.typeContrat = typeContrat; }

    public Double getSalaireFinal() { return salaireFinal; }
    public void setSalaireFinal(Double salaireFinal) { this.salaireFinal = salaireFinal; }

    public LocalDateTime getDateDebutEffective() { return dateDebutEffective; }
    public void setDateDebutEffective(LocalDateTime dateDebutEffective) { this.dateDebutEffective = dateDebutEffective; }

    public Integer getPeriodeEssai() { return periodeEssai; }
    public void setPeriodeEssai(Integer periodeEssai) { this.periodeEssai = periodeEssai; }

    public Double getCommissionPlateforme() { return commissionPlateforme; }
    public void setCommissionPlateforme(Double commissionPlateforme) { this.commissionPlateforme = commissionPlateforme; }

    public String getFeedbackPostEmbauche3Mois() { return feedbackPostEmbauche3Mois; }
    public void setFeedbackPostEmbauche3Mois(String feedbackPostEmbauche3Mois) { this.feedbackPostEmbauche3Mois = feedbackPostEmbauche3Mois; }

    public ContractStatus getStatus() { return status; }
    public void setStatus(ContractStatus status) { this.status = status; }

    public LocalDateTime getDateContratSigne() { return dateContratSigne; }
    public void setDateContratSigne(LocalDateTime dateContratSigne) { this.dateContratSigne = dateContratSigne; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}