package tn.esprit.msrecruitmentservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.msrecruitmentservice.dto.JobPositionDTO;
import tn.esprit.msrecruitmentservice.entities.JobPosition;
import tn.esprit.msrecruitmentservice.entities.JobStatus;

@Component
public class JobPositionMapper {

    public JobPositionDTO toDTO(JobPosition entity) {
        if (entity == null) return null;
        JobPositionDTO dto = new JobPositionDTO();
        dto.setId(entity.getId());
        dto.setEntrepriseId(entity.getEntrepriseId());
        dto.setTitre(entity.getTitre());
        dto.setDescription(entity.getDescription());
        dto.setTypeContrat(entity.getTypeContrat());
        dto.setSalaireMin(entity.getSalaireMin());
        dto.setSalaireMax(entity.getSalaireMax());
        dto.setLocalisation(entity.getLocalisation());
        dto.setRemote(entity.getRemote());
        dto.setExperienceRequiseAns(entity.getExperienceRequiseAns());
        dto.setSkillsRequis(entity.getSkillsRequis());
        dto.setDeadline(entity.getDeadline());
        dto.setStatus(entity.getStatus());
        dto.setNombreCandidatures(entity.getNombreCandidatures());
        return dto;
    }

    public JobPosition toEntity(JobPositionDTO dto) {
        if (dto == null) return null;
        JobPosition entity = new JobPosition();
        entity.setEntrepriseId(dto.getEntrepriseId());
        entity.setTitre(dto.getTitre());
        entity.setDescription(dto.getDescription());
        entity.setTypeContrat(dto.getTypeContrat());
        entity.setSalaireMin(dto.getSalaireMin());
        entity.setSalaireMax(dto.getSalaireMax());
        entity.setLocalisation(dto.getLocalisation());
        entity.setRemote(dto.getRemote());
        entity.setExperienceRequiseAns(dto.getExperienceRequiseAns());
        entity.setSkillsRequis(dto.getSkillsRequis());
        entity.setDeadline(dto.getDeadline());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : JobStatus.DRAFT);
        entity.setNombreCandidatures(dto.getNombreCandidatures() != null ? dto.getNombreCandidatures() : 0);
        return entity;
    }
}