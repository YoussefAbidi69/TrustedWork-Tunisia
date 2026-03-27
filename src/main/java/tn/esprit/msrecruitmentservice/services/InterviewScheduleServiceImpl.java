package tn.esprit.msrecruitmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.dto.InterviewScheduleDTO;
import tn.esprit.msrecruitmentservice.entities.*;
import tn.esprit.msrecruitmentservice.repositories.IInterviewScheduleRepository;
import tn.esprit.msrecruitmentservice.repositories.IRecruitmentApplicationRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewScheduleServiceImpl implements IInterviewScheduleService {

    @Autowired
    private IInterviewScheduleRepository interviewRepository;

    @Autowired
    private IRecruitmentApplicationRepository applicationRepository;

    private InterviewScheduleDTO toDTO(InterviewSchedule entity) {
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

    private InterviewSchedule toEntity(InterviewScheduleDTO dto) {
        InterviewSchedule entity = new InterviewSchedule();

        RecruitmentApplication application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application non trouvee : " + dto.getApplicationId()));
        entity.setApplication(application);

        entity.setType(dto.getType());
        entity.setOrdreEntretien(dto.getOrdreEntretien() != null ? dto.getOrdreEntretien() : 1);
        entity.setDateFinalConfirmee(dto.getDateFinalConfirmee());
        entity.setDureePrevueMinutes(dto.getDureePrevueMinutes() != null ? dto.getDureePrevueMinutes() : 60);
        entity.setLienVisio(dto.getLienVisio());
        entity.setStatus(InterviewStatus.PROPOSED);
        return entity;
    }

    @Override
    public InterviewScheduleDTO create(InterviewScheduleDTO dto) {
        InterviewSchedule saved = interviewRepository.save(toEntity(dto));
        // Mettre a jour le status de la candidature
        RecruitmentApplication app = saved.getApplication();
        app.setStatus(ApplicationStatus.INTERVIEW);
        applicationRepository.save(app);
        return toDTO(saved);
    }

    @Override
    public InterviewScheduleDTO update(Long id, InterviewScheduleDTO dto) {
        InterviewSchedule existing = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview non trouvee : " + id));
        existing.setType(dto.getType());
        existing.setOrdreEntretien(dto.getOrdreEntretien());
        existing.setDateFinalConfirmee(dto.getDateFinalConfirmee());
        existing.setDureePrevueMinutes(dto.getDureePrevueMinutes());
        existing.setLienVisio(dto.getLienVisio());
        existing.setStatus(dto.getStatus());
        return toDTO(interviewRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        interviewRepository.deleteById(id);
    }

    @Override
    public InterviewScheduleDTO getById(Long id) {
        return toDTO(interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview non trouvee : " + id)));
    }

    @Override
    public List<InterviewScheduleDTO> getAll() {
        return interviewRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<InterviewScheduleDTO> getByApplication(Long applicationId) {
        return interviewRepository.findByApplicationOrderedByOrdre(applicationId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public InterviewScheduleDTO updateStatus(Long id, InterviewStatus status) {
        interviewRepository.updateStatus(id, status);
        return getById(id);
    }

    @Override
    public InterviewScheduleDTO addFeedback(Long id, String feedback, Integer note) {
        InterviewSchedule existing = interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview non trouvee : " + id));
        existing.setFeedbackRecruteur(feedback);
        existing.setNoteRecruteur(note);
        existing.setStatus(InterviewStatus.COMPLETED);
        return toDTO(interviewRepository.save(existing));
    }
}