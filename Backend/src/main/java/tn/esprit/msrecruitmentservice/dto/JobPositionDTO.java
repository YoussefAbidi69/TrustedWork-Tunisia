package tn.esprit.msrecruitmentservice.dto;

import tn.esprit.msrecruitmentservice.entities.JobStatus;
import tn.esprit.msrecruitmentservice.entities.TypeContrat;
import java.time.LocalDateTime;
import java.time.LocalDate;
public class JobPositionDTO {

    private Long id;
    private Long entrepriseId;
    private String titre;
    private String description;
    private TypeContrat typeContrat;
    private Double salaireMin;
    private Double salaireMax;
    private String localisation;
    private Boolean remote;
    private Integer experienceRequiseAns;
    private String skillsRequis;
    private LocalDate deadline;
    private JobStatus status;
    private Integer nombreCandidatures;

    // Getters et Setters manuels (pas MapStruct — exigence CDC)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEntrepriseId() { return entrepriseId; }
    public void setEntrepriseId(Long entrepriseId) { this.entrepriseId = entrepriseId; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TypeContrat getTypeContrat() { return typeContrat; }
    public void setTypeContrat(TypeContrat typeContrat) { this.typeContrat = typeContrat; }

    public Double getSalaireMin() { return salaireMin; }
    public void setSalaireMin(Double salaireMin) { this.salaireMin = salaireMin; }

    public Double getSalaireMax() { return salaireMax; }
    public void setSalaireMax(Double salaireMax) { this.salaireMax = salaireMax; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public Boolean getRemote() { return remote; }
    public void setRemote(Boolean remote) { this.remote = remote; }

    public Integer getExperienceRequiseAns() { return experienceRequiseAns; }
    public void setExperienceRequiseAns(Integer experienceRequiseAns) { this.experienceRequiseAns = experienceRequiseAns; }

    public String getSkillsRequis() { return skillsRequis; }
    public void setSkillsRequis(String skillsRequis) { this.skillsRequis = skillsRequis; }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public JobStatus getStatus() { return status; }
    public void setStatus(JobStatus status) { this.status = status; }

    public Integer getNombreCandidatures() { return nombreCandidatures; }
    public void setNombreCandidatures(Integer nombreCandidatures) { this.nombreCandidatures = nombreCandidatures; }
}