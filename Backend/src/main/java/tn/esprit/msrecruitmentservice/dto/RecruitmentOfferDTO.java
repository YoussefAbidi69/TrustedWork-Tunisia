package tn.esprit.msrecruitmentservice.dto;

import tn.esprit.msrecruitmentservice.entities.OfferStatus;
import java.time.LocalDateTime;

public class RecruitmentOfferDTO {

    private Long id;
    private Long applicationId;
    private Double salairePropose;
    private String posteExact;
    private LocalDateTime dateDebutSouhaitee;
    private Integer periodeEssaiMois;
    private String avantages;
    private LocalDateTime deadlineReponse;
    private String contreOffreFreelancer;
    private OfferStatus status;
    private LocalDateTime dateEnvoi;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public Double getSalairePropose() { return salairePropose; }
    public void setSalairePropose(Double salairePropose) { this.salairePropose = salairePropose; }

    public String getPosteExact() { return posteExact; }
    public void setPosteExact(String posteExact) { this.posteExact = posteExact; }

    public LocalDateTime getDateDebutSouhaitee() { return dateDebutSouhaitee; }
    public void setDateDebutSouhaitee(LocalDateTime dateDebutSouhaitee) { this.dateDebutSouhaitee = dateDebutSouhaitee; }

    public Integer getPeriodeEssaiMois() { return periodeEssaiMois; }
    public void setPeriodeEssaiMois(Integer periodeEssaiMois) { this.periodeEssaiMois = periodeEssaiMois; }

    public String getAvantages() { return avantages; }
    public void setAvantages(String avantages) { this.avantages = avantages; }

    public LocalDateTime getDeadlineReponse() { return deadlineReponse; }
    public void setDeadlineReponse(LocalDateTime deadlineReponse) { this.deadlineReponse = deadlineReponse; }

    public String getContreOffreFreelancer() { return contreOffreFreelancer; }
    public void setContreOffreFreelancer(String contreOffreFreelancer) { this.contreOffreFreelancer = contreOffreFreelancer; }

    public OfferStatus getStatus() { return status; }
    public void setStatus(OfferStatus status) { this.status = status; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }
}