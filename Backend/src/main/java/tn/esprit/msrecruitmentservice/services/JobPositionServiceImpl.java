package tn.esprit.msrecruitmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.dto.JobPositionDTO;
import tn.esprit.msrecruitmentservice.entities.JobPosition;
import tn.esprit.msrecruitmentservice.entities.JobStatus;
import tn.esprit.msrecruitmentservice.mapper.JobPositionMapper;
import tn.esprit.msrecruitmentservice.repositories.IJobPositionRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPositionServiceImpl implements IJobPositionService {

    @Autowired
    private IJobPositionRepository jobPositionRepository;

    @Autowired
    private JobPositionMapper mapper;

    @Override
    public JobPositionDTO createJobPosition(JobPositionDTO dto) {
        return mapper.toDTO(jobPositionRepository.save(mapper.toEntity(dto)));
    }

    @Override
    public JobPositionDTO updateJobPosition(Long id, JobPositionDTO dto) {
        JobPosition existing = jobPositionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobPosition non trouvee : " + id));
        existing.setTitre(dto.getTitre());
        existing.setDescription(dto.getDescription());
        existing.setTypeContrat(dto.getTypeContrat());
        existing.setSalaireMin(dto.getSalaireMin());
        existing.setSalaireMax(dto.getSalaireMax());
        existing.setLocalisation(dto.getLocalisation());
        existing.setRemote(dto.getRemote());
        existing.setExperienceRequiseAns(dto.getExperienceRequiseAns());
        existing.setSkillsRequis(dto.getSkillsRequis());
        existing.setDeadline(dto.getDeadline());
        existing.setStatus(dto.getStatus());
        return mapper.toDTO(jobPositionRepository.save(existing));
    }

    @Override
    public void deleteJobPosition(Long id) {
        jobPositionRepository.deleteById(id);
    }

    @Override
    public JobPositionDTO getById(Long id) {
        return mapper.toDTO(jobPositionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("JobPosition non trouvee : " + id)));
    }

    @Override
    public List<JobPositionDTO> getAll() {
        return jobPositionRepository.findAll()
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<JobPositionDTO> getByEntreprise(Long entrepriseId) {
        return jobPositionRepository.findByEntrepriseId(entrepriseId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<JobPositionDTO> getPublished() {
        return jobPositionRepository.findByStatus(JobStatus.PUBLISHED)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}