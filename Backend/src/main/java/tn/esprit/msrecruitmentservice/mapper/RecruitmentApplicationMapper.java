package tn.esprit.msrecruitmentservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.msrecruitmentservice.dto.RecruitmentApplicationDTO;
import tn.esprit.msrecruitmentservice.entities.RecruitmentApplication;
import tn.esprit.msrecruitmentservice.entities.ApplicationStatus;

@Component
public class RecruitmentApplicationMapper {

    public RecruitmentApplicationDTO toDTO(RecruitmentApplication entity) {
        if (entity == null) return null;
        RecruitmentApplicationDTO dto = new RecruitmentApplicationDTO();
        dto.setId(entity.getId());
        dto.setJobPositionId(entity.getJobPosition().getId());
        dto.setJobPositionTitre(entity.getJobPosition().getTitre());
        dto.setFreelancerId(entity.getFreelancerId());
        dto.setEntrepriseId(entity.getEntrepriseId());
        dto.setLettreMotivation(entity.getLettreMotivation());
        dto.setPretentionSalariale(entity.getPretentionSalariale());
        dto.setDisponibilite(entity.getDisponibilite());
        dto.setMatchingScore(entity.getMatchingScore());
        dto.setScoreDetails(entity.getScoreDetails());
        dto.setStatus(entity.getStatus());
        dto.setMotifRejet(entity.getMotifRejet());
        dto.setDatePostulation(entity.getDatePostulation());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    // toEntity ne fait PAS le setJobPosition car il faut le repo
    // c'est le ServiceImpl qui s'en charge
    public RecruitmentApplication toEntity(RecruitmentApplicationDTO dto) {
        if (dto == null) return null;
        RecruitmentApplication entity = new RecruitmentApplication();
        entity.setFreelancerId(dto.getFreelancerId() != null ? dto.getFreelancerId() : 1L);
        entity.setEntrepriseId(dto.getEntrepriseId());
        entity.setLettreMotivation(dto.getLettreMotivation());
        entity.setPretentionSalariale(dto.getPretentionSalariale());
        entity.setDisponibilite(dto.getDisponibilite());
        entity.setMatchingScore(dto.getMatchingScore() != null ? dto.getMatchingScore() : 75.0);
        entity.setScoreDetails(dto.getScoreDetails());
        entity.setStatus(ApplicationStatus.SUBMITTED);
        return entity;
    }
}