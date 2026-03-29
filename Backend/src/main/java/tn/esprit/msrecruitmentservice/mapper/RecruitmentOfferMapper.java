package tn.esprit.msrecruitmentservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.msrecruitmentservice.dto.RecruitmentOfferDTO;
import tn.esprit.msrecruitmentservice.entities.OfferStatus;
import tn.esprit.msrecruitmentservice.entities.RecruitmentOffer;

@Component
public class RecruitmentOfferMapper {

    public RecruitmentOfferDTO toDTO(RecruitmentOffer entity) {
        if (entity == null) return null;
        RecruitmentOfferDTO dto = new RecruitmentOfferDTO();
        dto.setId(entity.getId());
        dto.setApplicationId(entity.getApplication().getId());
        dto.setSalairePropose(entity.getSalairePropose());
        dto.setPosteExact(entity.getPosteExact());
        dto.setDateDebutSouhaitee(entity.getDateDebutSouhaitee());
        dto.setPeriodeEssaiMois(entity.getPeriodeEssaiMois());
        dto.setAvantages(entity.getAvantages());
        dto.setDeadlineReponse(entity.getDeadlineReponse());
        dto.setContreOffreFreelancer(entity.getContreOffreFreelancer());
        dto.setStatus(entity.getStatus());
        dto.setDateEnvoi(entity.getDateEnvoi());
        return dto;
    }

    // setApplication est fait dans le ServiceImpl car besoin du repo
    public RecruitmentOffer toEntity(RecruitmentOfferDTO dto) {
        if (dto == null) return null;
        RecruitmentOffer entity = new RecruitmentOffer();
        entity.setSalairePropose(dto.getSalairePropose());
        entity.setPosteExact(dto.getPosteExact());
        entity.setDateDebutSouhaitee(dto.getDateDebutSouhaitee());
        entity.setPeriodeEssaiMois(dto.getPeriodeEssaiMois());
        entity.setAvantages(dto.getAvantages());
        entity.setDeadlineReponse(dto.getDeadlineReponse());
        entity.setStatus(OfferStatus.SENT);
        return entity;
    }
}