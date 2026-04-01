package tn.esprit.msrecruitmentservice.services;

import tn.esprit.msrecruitmentservice.dto.JobPositionDTO;
import java.util.List;

public interface IJobPositionService {
    JobPositionDTO createJobPosition(JobPositionDTO dto);
    JobPositionDTO updateJobPosition(Long id, JobPositionDTO dto);
    void deleteJobPosition(Long id);
    JobPositionDTO getById(Long id);
    List<JobPositionDTO> getAll();
    List<JobPositionDTO> getByEntreprise(Long entrepriseId);
    List<JobPositionDTO> getPublished();



    //Ia :
    JobPositionDTO generateAndSaveDescription(Long id);

}