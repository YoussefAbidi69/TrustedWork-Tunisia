package tn.esprit.msrecruitmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.dto.TalentPoolDTO;
import tn.esprit.msrecruitmentservice.entities.TalentPool;
import tn.esprit.msrecruitmentservice.entities.TalentTag;
import tn.esprit.msrecruitmentservice.repositories.ITalentPoolRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TalentPoolServiceImpl implements ITalentPoolService {

    @Autowired
    private ITalentPoolRepository talentPoolRepository;

    private TalentPoolDTO toDTO(TalentPool entity) {
        TalentPoolDTO dto = new TalentPoolDTO();
        dto.setId(entity.getId());
        dto.setEntrepriseId(entity.getEntrepriseId());
        dto.setFreelancerId(entity.getFreelancerId());
        dto.setTag(entity.getTag());
        dto.setSourceOrigine(entity.getSourceOrigine());
        dto.setAlerteDisponibilite(entity.getAlerteDisponibilite());
        dto.setNotesPrivees(entity.getNotesPrivees());
        dto.setDateAjout(entity.getDateAjout());
        return dto;
    }

    private TalentPool toEntity(TalentPoolDTO dto) {
        TalentPool entity = new TalentPool();
        entity.setEntrepriseId(dto.getEntrepriseId());
        entity.setFreelancerId(dto.getFreelancerId());
        entity.setTag(dto.getTag());
        entity.setSourceOrigine(dto.getSourceOrigine());
        entity.setAlerteDisponibilite(dto.getAlerteDisponibilite() != null ? dto.getAlerteDisponibilite() : false);
        entity.setNotesPrivees(dto.getNotesPrivees());
        return entity;
    }

    @Override
    public TalentPoolDTO addToPool(TalentPoolDTO dto) {
        return toDTO(talentPoolRepository.save(toEntity(dto)));
    }

    @Override
    public TalentPoolDTO update(Long id, TalentPoolDTO dto) {
        TalentPool existing = talentPoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TalentPool non trouve : " + id));
        existing.setTag(dto.getTag());
        existing.setSourceOrigine(dto.getSourceOrigine());
        existing.setAlerteDisponibilite(dto.getAlerteDisponibilite());
        existing.setNotesPrivees(dto.getNotesPrivees());
        return toDTO(talentPoolRepository.save(existing));
    }

    @Override
    public void removeFromPool(Long id) {
        talentPoolRepository.deleteById(id);
    }

    @Override
    public TalentPoolDTO getById(Long id) {
        return toDTO(talentPoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TalentPool non trouve : " + id)));
    }

    @Override
    public List<TalentPoolDTO> getByEntreprise(Long entrepriseId) {
        return talentPoolRepository.findByEntrepriseId(entrepriseId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TalentPoolDTO> getByEntrepriseAndTag(Long entrepriseId, TalentTag tag) {
        return talentPoolRepository.findByEntrepriseIdAndTag(entrepriseId, tag)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
}