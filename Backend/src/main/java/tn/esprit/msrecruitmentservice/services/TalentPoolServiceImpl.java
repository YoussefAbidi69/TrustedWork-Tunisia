package tn.esprit.msrecruitmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.dto.TalentPoolDTO;
import tn.esprit.msrecruitmentservice.entities.TalentPool;
import tn.esprit.msrecruitmentservice.entities.TalentTag;
import tn.esprit.msrecruitmentservice.mapper.TalentPoolMapper;
import tn.esprit.msrecruitmentservice.repositories.ITalentPoolRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TalentPoolServiceImpl implements ITalentPoolService {

    @Autowired
    private ITalentPoolRepository talentPoolRepository;

    @Autowired
    private TalentPoolMapper mapper;

    @Override
    public TalentPoolDTO addToPool(TalentPoolDTO dto) {
        return mapper.toDTO(talentPoolRepository.save(mapper.toEntity(dto)));
    }

    @Override
    public TalentPoolDTO update(Long id, TalentPoolDTO dto) {
        TalentPool existing = talentPoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TalentPool non trouve : " + id));
        existing.setTag(dto.getTag());
        existing.setSourceOrigine(dto.getSourceOrigine());
        existing.setAlerteDisponibilite(dto.getAlerteDisponibilite());
        existing.setNotesPrivees(dto.getNotesPrivees());
        return mapper.toDTO(talentPoolRepository.save(existing));
    }

    @Override
    public void removeFromPool(Long id) {
        talentPoolRepository.deleteById(id);
    }

    @Override
    public TalentPoolDTO getById(Long id) {
        return mapper.toDTO(talentPoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TalentPool non trouve : " + id)));
    }

    @Override
    public List<TalentPoolDTO> getByEntreprise(Long entrepriseId) {
        return talentPoolRepository.findByEntrepriseId(entrepriseId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TalentPoolDTO> getByEntrepriseAndTag(Long entrepriseId, TalentTag tag) {
        return talentPoolRepository.findByEntrepriseIdAndTag(entrepriseId, tag)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}