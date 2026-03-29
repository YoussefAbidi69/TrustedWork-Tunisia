package tn.esprit.msrecruitmentservice.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.msrecruitmentservice.dto.InterviewScheduleDTO;
import tn.esprit.msrecruitmentservice.entities.InterviewSchedule;
import tn.esprit.msrecruitmentservice.entities.InterviewStatus;

@Component
public class InterviewScheduleMapper {

    public InterviewScheduleDTO toDTO(InterviewSchedule entity) {
        if (entity == null) return null;
        InterviewScheduleDTO dto = new InterviewScheduleDTO();
        dto.setId(entity.getId());
        dto.setApplicationId(entity.getApplication().getId());
        dto.setType(entity.getType());
        dto.setOrdreEntretien(entity.getOrdreEntretien());
        dto.setDateFinalConfirmee(entity.getDateFinalConfirmee());
        dto.setDureePrevueMinutes(entity.getDureePrevueMinutes());
        dto.setLienVisio(entity.getLienVisio());
        dto.setStatus(entity.getStatus());
        dto.setFeedbackRecruteur(entity.getFeedbackRecruteur());
        dto.setNoteRecruteur(entity.getNoteRecruteur());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    // setApplication est fait dans le ServiceImpl car besoin du repo
    public InterviewSchedule toEntity(InterviewScheduleDTO dto) {
        if (dto == null) return null;
        InterviewSchedule entity = new InterviewSchedule();
        entity.setType(dto.getType());
        entity.setOrdreEntretien(dto.getOrdreEntretien() != null ? dto.getOrdreEntretien() : 1);
        entity.setDateFinalConfirmee(dto.getDateFinalConfirmee());
        entity.setDureePrevueMinutes(dto.getDureePrevueMinutes() != null ? dto.getDureePrevueMinutes() : 60);
        entity.setLienVisio(dto.getLienVisio());
        entity.setStatus(InterviewStatus.PROPOSED);
        return entity;
    }
}