package tn.esprit.msrecruitmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.dto.HiringContractDTO;
import tn.esprit.msrecruitmentservice.entities.*;
import tn.esprit.msrecruitmentservice.repositories.IHiringContractRepository;
import tn.esprit.msrecruitmentservice.repositories.IRecruitmentOfferRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HiringContractServiceImpl implements IHiringContractService {

    @Autowired
    private IHiringContractRepository contractRepository;

    @Autowired
    private IRecruitmentOfferRepository offerRepository;

    private HiringContractDTO toDTO(HiringContract entity) {
        HiringContractDTO dto = new HiringContractDTO();
        dto.setId(entity.getId());
        dto.setOfferId(entity.getOffer().getId());
        dto.setFreelancerId(entity.getFreelancerId());
        dto.setEntrepriseId(entity.getEntrepriseId());
        dto.setTypeContrat(entity.getTypeContrat());
        dto.setSalaireFinal(entity.getSalaireFinal());
        dto.setDateDebutEffective(entity.getDateDebutEffective());
        dto.setPeriodeEssai(entity.getPeriodeEssai());
        dto.setCommissionPlateforme(entity.getCommissionPlateforme());
        dto.setFeedbackPostEmbauche3Mois(entity.getFeedbackPostEmbauche3Mois());
        dto.setStatus(entity.getStatus());
        dto.setDateContratSigne(entity.getDateContratSigne());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private HiringContract toEntity(HiringContractDTO dto) {
        HiringContract entity = new HiringContract();

        RecruitmentOffer offer = offerRepository.findById(dto.getOfferId())
                .orElseThrow(() -> new RuntimeException("Offre non trouvee : " + dto.getOfferId()));
        entity.setOffer(offer);

        // Recuperer freelancerId et entrepriseId depuis l'offre (mock Phase 1)
        entity.setFreelancerId(offer.getApplication().getFreelancerId());
        entity.setEntrepriseId(offer.getApplication().getEntrepriseId());

        entity.setTypeContrat(dto.getTypeContrat());
        entity.setSalaireFinal(dto.getSalaireFinal() != null ?
                dto.getSalaireFinal() : offer.getSalairePropose());
        entity.setDateDebutEffective(dto.getDateDebutEffective());
        entity.setPeriodeEssai(dto.getPeriodeEssai() != null ?
                dto.getPeriodeEssai() : offer.getPeriodeEssaiMois());
        entity.setCommissionPlateforme(10.0);
        entity.setStatus(ContractStatus.DRAFT);
        return entity;
    }

    @Override
    public HiringContractDTO create(HiringContractDTO dto) {
        return toDTO(contractRepository.save(toEntity(dto)));
    }

    @Override
    public HiringContractDTO update(Long id, HiringContractDTO dto) {
        HiringContract existing = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrat non trouve : " + id));
        existing.setTypeContrat(dto.getTypeContrat());
        existing.setSalaireFinal(dto.getSalaireFinal());
        existing.setDateDebutEffective(dto.getDateDebutEffective());
        existing.setPeriodeEssai(dto.getPeriodeEssai());
        existing.setStatus(dto.getStatus());
        return toDTO(contractRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        contractRepository.deleteById(id);
    }

    @Override
    public HiringContractDTO getById(Long id) {
        return toDTO(contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrat non trouve : " + id)));
    }

    @Override
    public HiringContractDTO getByOffer(Long offerId) {
        return toDTO(contractRepository.findByOfferId(offerId)
                .orElseThrow(() -> new RuntimeException("Aucun contrat pour cette offre")));
    }

    @Override
    public List<HiringContractDTO> getAll() {
        return contractRepository.findAll()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<HiringContractDTO> getByFreelancer(Long freelancerId) {
        return contractRepository.findByFreelancerId(freelancerId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<HiringContractDTO> getByEntreprise(Long entrepriseId) {
        return contractRepository.findByEntrepriseId(entrepriseId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public HiringContractDTO signerContrat(Long id) {
        contractRepository.signerContrat(id, ContractStatus.SIGNED);
        return getById(id);
    }

    @Override
    public HiringContractDTO addFeedback(Long id, String feedback) {
        contractRepository.addFeedback(id, feedback);
        return getById(id);
    }
}