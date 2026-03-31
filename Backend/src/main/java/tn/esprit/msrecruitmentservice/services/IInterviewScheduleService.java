package tn.esprit.msrecruitmentservice.services;

import tn.esprit.msrecruitmentservice.dto.InterviewScheduleDTO;
import tn.esprit.msrecruitmentservice.entities.InterviewStatus;
import java.util.List;

public interface IInterviewScheduleService {
    InterviewScheduleDTO create(InterviewScheduleDTO dto);
    InterviewScheduleDTO update(Long id, InterviewScheduleDTO dto);
    void delete(Long id);
    InterviewScheduleDTO getById(Long id);
    List<InterviewScheduleDTO> getAll();
    List<InterviewScheduleDTO> getByApplication(Long applicationId);
    InterviewScheduleDTO updateStatus(Long id, InterviewStatus status);
    InterviewScheduleDTO addFeedback(Long id, String feedback, Integer note);


    List<InterviewScheduleDTO> getByStatus(InterviewStatus status);}