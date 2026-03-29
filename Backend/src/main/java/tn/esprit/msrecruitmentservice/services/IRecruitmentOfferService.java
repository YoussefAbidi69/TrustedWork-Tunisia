package tn.esprit.msrecruitmentservice.services;

import tn.esprit.msrecruitmentservice.dto.RecruitmentOfferDTO;
import tn.esprit.msrecruitmentservice.entities.OfferStatus;
import java.util.List;

public interface IRecruitmentOfferService {
    RecruitmentOfferDTO create(RecruitmentOfferDTO dto);
    RecruitmentOfferDTO update(Long id, RecruitmentOfferDTO dto);
    void delete(Long id);
    RecruitmentOfferDTO getById(Long id);
    RecruitmentOfferDTO getByApplication(Long applicationId);
    List<RecruitmentOfferDTO> getAll();
    List<RecruitmentOfferDTO> getByEntreprise(Long entrepriseId);
    RecruitmentOfferDTO updateStatus(Long id, OfferStatus status);
    RecruitmentOfferDTO addContreOffre(Long id, String contreOffre);
}