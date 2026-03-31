package tn.esprit.msrecruitmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.dto.InterviewScheduleDTO;
import tn.esprit.msrecruitmentservice.entities.*;
import tn.esprit.msrecruitmentservice.mapper.InterviewScheduleMapper;
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

    @Autowired
    private InterviewScheduleMapper mapper;

    @Override
    public InterviewScheduleDTO create(InterviewScheduleDTO dto) {
        InterviewSchedule entity = mapper.toEntity(dto);
        RecruitmentApplication application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application non trouvee : " + dto.getApplicationId()));
        entity.setApplication(application);
        InterviewSchedule saved = interviewRepository.save(entity);
        application.setStatus(ApplicationStatus.INTERVIEW);
        applicationRepository.save(application);
        return mapper.toDTO(saved);
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
        return mapper.toDTO(interviewRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        interviewRepository.deleteById(id);
    }

    @Override
    public InterviewScheduleDTO getById(Long id) {
        return mapper.toDTO(interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interview non trouvee : " + id)));
    }

    @Override
    public List<InterviewScheduleDTO> getAll() {
        return interviewRepository.findAll()
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<InterviewScheduleDTO> getByApplication(Long applicationId) {
        return interviewRepository.findByApplicationOrderedByOrdre(applicationId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
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
        return mapper.toDTO(interviewRepository.save(existing));
    }

    @Override
    public List<InterviewScheduleDTO> getByStatus(InterviewStatus status) {
        return interviewRepository.findByStatus(status)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}