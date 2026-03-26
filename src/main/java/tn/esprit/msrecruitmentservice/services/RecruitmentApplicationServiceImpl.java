package tn.esprit.msrecruitmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.dto.RecruitmentApplicationDTO;
import tn.esprit.msrecruitmentservice.entities.ApplicationStatus;
import tn.esprit.msrecruitmentservice.entities.JobPosition;
import tn.esprit.msrecruitmentservice.entities.RecruitmentApplication;
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

    // ── Mapping Entity → DTO ──────────────────────────────────────────────
    private RecruitmentApplicationDTO toDTO(RecruitmentApplication entity) {
        RecruitmentApplicationDTO dto = new RecruitmentApplicationDTO();
        dto.setId(entity.getId());
        dto.setJobPositionId(entity.getJobPosition().getId());
        dto.setJobPositionTitre(entity.getJobPosition().getTitre());
        dto.setFreelancerId(entity.getFreelancerId());
        dto.setEntrepriseId(entity.getEntrepriseId());
        dto.setLettreMotivation(entity.getLettreMotivation());
        dto.setPretentionSalariale(entity.getPretentionSalariale());
        dto.setDisponibilite(entity.getDisponibilite());
        dto.setMatchingScore(entity.getMatchingScore());
        dto.setScoreDetails(entity.getScoreDetails());
        dto.setStatus(entity.getStatus());
        dto.setMotifRejet(entity.getMotifRejet());
        dto.setDatePostulation(entity.getDatePostulation());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    // ── Mapping DTO → Entity ──────────────────────────────────────────────
    private RecruitmentApplication toEntity(RecruitmentApplicationDTO dto) {
        RecruitmentApplication entity = new RecruitmentApplication();

        JobPosition jp = jobPositionRepository.findById(dto.getJobPositionId())
                .orElseThrow(() -> new RuntimeException("JobPosition non trouvee : " + dto.getJobPositionId()));
        entity.setJobPosition(jp);

        // Phase 1 — mock : si freelancerId non fourni, on met 1L par defaut
        entity.setFreelancerId(dto.getFreelancerId() != null ? dto.getFreelancerId() : 1L);
        entity.setEntrepriseId(dto.getEntrepriseId() != null ? dto.getEntrepriseId() : jp.getEntrepriseId());
        entity.setLettreMotivation(dto.getLettreMotivation());
        entity.setPretentionSalariale(dto.getPretentionSalariale());
        entity.setDisponibilite(dto.getDisponibilite());
        entity.setMatchingScore(dto.getMatchingScore() != null ? dto.getMatchingScore() : 75.0);
        entity.setScoreDetails(dto.getScoreDetails());
        return entity;
    }

    @Override
    public RecruitmentApplicationDTO apply(RecruitmentApplicationDTO dto) {
        RecruitmentApplication saved = applicationRepository.save(toEntity(dto));
        // Incrementer le compteur de candidatures sur le JobPosition
        JobPosition jp = saved.getJobPosition();
        jp.setNombreCandidatures(jp.getNombreCandidatures() + 1);
        jobPositionRepository.save(jp);
        return toDTO(saved);
    }

    @Override
    public RecruitmentApplicationDTO updateStatus(Long id, ApplicationStatus newStatus, String motifRejet) {
        RecruitmentApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application non trouvee : " + id));
        app.setStatus(newStatus);
        if (motifRejet != null) app.setMotifRejet(motifRejet);
        return toDTO(applicationRepository.save(app));
    }

    @Override
    public RecruitmentApplicationDTO getById(Long id) {
        return toDTO(applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application non trouvee : " + id)));
    }

    @Override
    public List<RecruitmentApplicationDTO> getByJobPosition(Long jobPositionId) {
        return applicationRepository.findByJobPosition_Id(jobPositionId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentApplicationDTO> getByFreelancer(Long freelancerId) {
        return applicationRepository.findByFreelancerId(freelancerId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentApplicationDTO> getByEntreprise(Long entrepriseId) {
        return applicationRepository.findByEntrepriseId(entrepriseId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentApplicationDTO> getRankedByScore(Long jobPositionId) {
        return applicationRepository.findByJobPositionOrderedByScore(jobPositionId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        applicationRepository.deleteById(id);
    }
}