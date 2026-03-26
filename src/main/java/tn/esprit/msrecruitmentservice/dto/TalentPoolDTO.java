package tn.esprit.msrecruitmentservice.dto;

import tn.esprit.msrecruitmentservice.entities.SourceOrigine;
import tn.esprit.msrecruitmentservice.entities.TalentTag;
import java.time.LocalDateTime;

public class TalentPoolDTO {

    private Long id;
    private Long entrepriseId;
    private Long freelancerId;
    private TalentTag tag;
    private SourceOrigine sourceOrigine;
    private Boolean alerteDisponibilite;
    private String notesPrivees;
    private LocalDateTime dateAjout;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEntrepriseId() { return entrepriseId; }
    public void setEntrepriseId(Long entrepriseId) { this.entrepriseId = entrepriseId; }

    public Long getFreelancerId() { return freelancerId; }
    public void setFreelancerId(Long freelancerId) { this.freelancerId = freelancerId; }

    public TalentTag getTag() { return tag; }
    public void setTag(TalentTag tag) { this.tag = tag; }

    public SourceOrigine getSourceOrigine() { return sourceOrigine; }
    public void setSourceOrigine(SourceOrigine sourceOrigine) { this.sourceOrigine = sourceOrigine; }

    public Boolean getAlerteDisponibilite() { return alerteDisponibilite; }
    public void setAlerteDisponibilite(Boolean alerteDisponibilite) { this.alerteDisponibilite = alerteDisponibilite; }

    public String getNotesPrivees() { return notesPrivees; }
    public void setNotesPrivees(String notesPrivees) { this.notesPrivees = notesPrivees; }

    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }
}