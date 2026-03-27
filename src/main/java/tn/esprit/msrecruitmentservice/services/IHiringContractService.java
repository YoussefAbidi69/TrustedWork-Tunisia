package tn.esprit.msrecruitmentservice.services;

import tn.esprit.msrecruitmentservice.dto.HiringContractDTO;
import java.util.List;

public interface IHiringContractService {
    HiringContractDTO create(HiringContractDTO dto);
    HiringContractDTO update(Long id, HiringContractDTO dto);
    void delete(Long id);
    HiringContractDTO getById(Long id);
    HiringContractDTO getByOffer(Long offerId);
    List<HiringContractDTO> getAll();
    List<HiringContractDTO> getByFreelancer(Long freelancerId);
    List<HiringContractDTO> getByEntreprise(Long entrepriseId);
    HiringContractDTO signerContrat(Long id);
    HiringContractDTO addFeedback(Long id, String feedback);
}