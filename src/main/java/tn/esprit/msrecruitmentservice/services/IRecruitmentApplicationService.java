package tn.esprit.msrecruitmentservice.services;

import tn.esprit.msrecruitmentservice.dto.RecruitmentApplicationDTO;
import tn.esprit.msrecruitmentservice.entities.ApplicationStatus;
import java.util.List;

public interface IRecruitmentApplicationService {
    RecruitmentApplicationDTO apply(RecruitmentApplicationDTO dto);
    RecruitmentApplicationDTO updateStatus(Long id, ApplicationStatus newStatus, String motifRejet);
    RecruitmentApplicationDTO getById(Long id);
    List<RecruitmentApplicationDTO> getByJobPosition(Long jobPositionId);
    List<RecruitmentApplicationDTO> getByFreelancer(Long freelancerId);
    List<RecruitmentApplicationDTO> getByEntreprise(Long entrepriseId);
    List<RecruitmentApplicationDTO> getRankedByScore(Long jobPositionId);
    void delete(Long id);
}