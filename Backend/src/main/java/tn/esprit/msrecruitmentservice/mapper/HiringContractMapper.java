package tn.esprit.msrecruitmentservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.msrecruitmentservice.dto.HiringContractDTO;
import tn.esprit.msrecruitmentservice.entities.ContractStatus;
import tn.esprit.msrecruitmentservice.entities.HiringContract;

@Component
public class HiringContractMapper {

    public HiringContractDTO toDTO(HiringContract entity) {
        if (entity == null) return null;
        HiringContractDTO dto = new HiringContractDTO();
        dto.setId(entity.getId());
        dto.setOfferId(entity.getOffer().getId());
        dto.setFreelancerId(entity.getFreelancerId());
        dto.setEntrepriseId(entity.getEntrepriseId());
        dto.setTypeContrat(entity.getTypeContrat());
        dto.setSalaireFinal(entity.getSalaireFinal());
        dto.setDateDebutEffective(entity.getDateDebutEffective());
        dto.setPeriodeEssai(entity.getPeriodeEssai());
        dto.setCommissionPlateforme(entity.getCommissionPlateforme());
        dto.setFeedbackPostEmbauche3Mois(entity.getFeedbackPostEmbauche3Mois());
        dto.setStatus(entity.getStatus());
        dto.setDateContratSigne(entity.getDateContratSigne());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    // setOffer + freelancerId + entrepriseId sont faits dans le ServiceImpl
    public HiringContract toEntity(HiringContractDTO dto) {
        if (dto == null) return null;
        HiringContract entity = new HiringContract();
        entity.setTypeContrat(dto.getTypeContrat());
        entity.setSalaireFinal(dto.getSalaireFinal());
        entity.setDateDebutEffective(dto.getDateDebutEffective());
        entity.setPeriodeEssai(dto.getPeriodeEssai());
        entity.setCommissionPlateforme(10.0);
        entity.setStatus(ContractStatus.DRAFT);
        return entity;
    }
}