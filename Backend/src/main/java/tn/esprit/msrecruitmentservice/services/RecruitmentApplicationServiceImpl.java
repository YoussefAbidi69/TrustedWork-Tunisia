package tn.esprit.msrecruitmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.dto.RecruitmentApplicationDTO;
import tn.esprit.msrecruitmentservice.entities.*;
import tn.esprit.msrecruitmentservice.mapper.RecruitmentApplicationMapper;
import tn.esprit.msrecruitmentservice.repositories.IJobPositionRepository;
import tn.esprit.msrecruitmentservice.repositories.IRecruitmentApplicationRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecruitmentApplicationServiceImpl implements IRecruitmentApplicationService {

    @Autowired
    private IRecruitmentApplicationRepository applicationRepository;

    @Autowired
    private IJobPositionRepository jobPositionRepository;

    @Autowired
    private RecruitmentApplicationMapper mapper;

    @Override
    public RecruitmentApplicationDTO apply(RecruitmentApplicationDTO dto) {
        RecruitmentApplication entity = mapper.toEntity(dto);
        JobPosition jp = jobPositionRepository.findById(dto.getJobPositionId())
                .orElseThrow(() -> new RuntimeException("JobPosition non trouvee : " + dto.getJobPositionId()));
        entity.setJobPosition(jp);
        entity.setEntrepriseId(dto.getEntrepriseId() != null ? dto.getEntrepriseId() : jp.getEntrepriseId());
        RecruitmentApplication saved = applicationRepository.save(entity);
        jp.setNombreCandidatures(jp.getNombreCandidatures() + 1);
        jobPositionRepository.save(jp);
        return mapper.toDTO(saved);
    }

    @Override
    public RecruitmentApplicationDTO updateStatus(Long id, ApplicationStatus newStatus, String motifRejet) {
        RecruitmentApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application non trouvee : " + id));
        app.setStatus(newStatus);
        if (motifRejet != null) app.setMotifRejet(motifRejet);
        return mapper.toDTO(applicationRepository.save(app));
    }

    @Override
    public RecruitmentApplicationDTO getById(Long id) {
        return mapper.toDTO(applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application non trouvee : " + id)));
    }

    @Override
    public List<RecruitmentApplicationDTO> getByJobPosition(Long jobPositionId) {
        return applicationRepository.findByJobPosition_Id(jobPositionId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentApplicationDTO> getByFreelancer(Long freelancerId) {
        return applicationRepository.findByFreelancerId(freelancerId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentApplicationDTO> getByEntreprise(Long entrepriseId) {
        return applicationRepository.findByEntrepriseId(entrepriseId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentApplicationDTO> getRankedByScore(Long jobPositionId) {
        return applicationRepository.findByJobPositionOrderedByScore(jobPositionId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        applicationRepository.deleteById(id);
    }

    @Override
    public List<RecruitmentApplicationDTO> getAll() {
        return applicationRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentApplicationDTO> getByStatuses(List<ApplicationStatus> statuses) {
        return applicationRepository.findByStatusIn(statuses)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}