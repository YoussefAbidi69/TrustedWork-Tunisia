package tn.esprit.msrecruitmentservice.services;

import tn.esprit.msrecruitmentservice.dto.TalentPoolDTO;
import tn.esprit.msrecruitmentservice.entities.TalentTag;
import java.util.List;

public interface ITalentPoolService {
    TalentPoolDTO addToPool(TalentPoolDTO dto);
    TalentPoolDTO update(Long id, TalentPoolDTO dto);
    void removeFromPool(Long id);
    TalentPoolDTO getById(Long id);
    List<TalentPoolDTO> getByEntreprise(Long entrepriseId);
    List<TalentPoolDTO> getByEntrepriseAndTag(Long entrepriseId, TalentTag tag);
}