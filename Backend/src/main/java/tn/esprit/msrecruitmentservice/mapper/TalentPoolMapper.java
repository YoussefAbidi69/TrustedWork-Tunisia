package tn.esprit.msrecruitmentservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.msrecruitmentservice.dto.TalentPoolDTO;
import tn.esprit.msrecruitmentservice.entities.TalentPool;

@Component
public class TalentPoolMapper {

    public TalentPoolDTO toDTO(TalentPool entity) {
        if (entity == null) return null;
        TalentPoolDTO dto = new TalentPoolDTO();
        dto.setId(entity.getId());
        dto.setEntrepriseId(entity.getEntrepriseId());
        dto.setFreelancerId(entity.getFreelancerId());
        dto.setTag(entity.getTag());
        dto.setSourceOrigine(entity.getSourceOrigine());
        dto.setAlerteDisponibilite(entity.getAlerteDisponibilite());
        dto.setNotesPrivees(entity.getNotesPrivees());
        dto.setDateAjout(entity.getDateAjout());
        return dto;
    }

    public TalentPool toEntity(TalentPoolDTO dto) {
        if (dto == null) return null;
        TalentPool entity = new TalentPool();
        entity.setEntrepriseId(dto.getEntrepriseId());
        entity.setFreelancerId(dto.getFreelancerId());
        entity.setTag(dto.getTag());
        entity.setSourceOrigine(dto.getSourceOrigine());
        entity.setAlerteDisponibilite(dto.getAlerteDisponibilite() != null ? dto.getAlerteDisponibilite() : false);
        entity.setNotesPrivees(dto.getNotesPrivees());
        return entity;
    }
}