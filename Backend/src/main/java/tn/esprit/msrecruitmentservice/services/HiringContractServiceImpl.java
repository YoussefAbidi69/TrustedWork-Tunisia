package tn.esprit.msrecruitmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.dto.HiringContractDTO;
import tn.esprit.msrecruitmentservice.entities.*;
import tn.esprit.msrecruitmentservice.mapper.HiringContractMapper;
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

    @Autowired
    private HiringContractMapper mapper;

    @Override
    public HiringContractDTO create(HiringContractDTO dto) {
        HiringContract entity = mapper.toEntity(dto);
        RecruitmentOffer offer = offerRepository.findById(dto.getOfferId())
                .orElseThrow(() -> new RuntimeException("Offre non trouvee : " + dto.getOfferId()));
        entity.setOffer(offer);
        entity.setFreelancerId(offer.getApplication().getFreelancerId());
        entity.setEntrepriseId(offer.getApplication().getEntrepriseId());
        if (entity.getSalaireFinal() == null) entity.setSalaireFinal(offer.getSalairePropose());
        if (entity.getPeriodeEssai() == null) entity.setPeriodeEssai(offer.getPeriodeEssaiMois());
        return mapper.toDTO(contractRepository.save(entity));
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
        return mapper.toDTO(contractRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        contractRepository.deleteById(id);
    }

    @Override
    public HiringContractDTO getById(Long id) {
        return mapper.toDTO(contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrat non trouve : " + id)));
    }

    @Override
    public HiringContractDTO getByOffer(Long offerId) {
        return mapper.toDTO(contractRepository.findByOfferId(offerId)
                .orElseThrow(() -> new RuntimeException("Aucun contrat pour cette offre")));
    }

    @Override
    public List<HiringContractDTO> getAll() {
        return contractRepository.findAll()
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<HiringContractDTO> getByFreelancer(Long freelancerId) {
        return contractRepository.findByFreelancerId(freelancerId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<HiringContractDTO> getByEntreprise(Long entrepriseId) {
        return contractRepository.findByEntrepriseId(entrepriseId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
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