package tn.esprit.msrecruitmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.dto.RecruitmentOfferDTO;
import tn.esprit.msrecruitmentservice.entities.*;
import tn.esprit.msrecruitmentservice.mapper.RecruitmentOfferMapper;
import tn.esprit.msrecruitmentservice.repositories.IRecruitmentApplicationRepository;
import tn.esprit.msrecruitmentservice.repositories.IRecruitmentOfferRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecruitmentOfferServiceImpl implements IRecruitmentOfferService {

    @Autowired
    private IRecruitmentOfferRepository offerRepository;

    @Autowired
    private IRecruitmentApplicationRepository applicationRepository;

    @Autowired
    private RecruitmentOfferMapper mapper;

    @Override
    public RecruitmentOfferDTO create(RecruitmentOfferDTO dto) {
        RecruitmentOffer entity = mapper.toEntity(dto);
        RecruitmentApplication application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Application non trouvee : " + dto.getApplicationId()));
        entity.setApplication(application);
        RecruitmentOffer saved = offerRepository.save(entity);
        application.setStatus(ApplicationStatus.OFFERED);
        applicationRepository.save(application);
        return mapper.toDTO(saved);
    }

    @Override
    public RecruitmentOfferDTO update(Long id, RecruitmentOfferDTO dto) {
        RecruitmentOffer existing = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvee : " + id));
        existing.setSalairePropose(dto.getSalairePropose());
        existing.setPosteExact(dto.getPosteExact());
        existing.setDateDebutSouhaitee(dto.getDateDebutSouhaitee());
        existing.setPeriodeEssaiMois(dto.getPeriodeEssaiMois());
        existing.setAvantages(dto.getAvantages());
        existing.setDeadlineReponse(dto.getDeadlineReponse());
        existing.setStatus(dto.getStatus());
        return mapper.toDTO(offerRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        offerRepository.deleteById(id);
    }

    @Override
    public RecruitmentOfferDTO getById(Long id) {
        return mapper.toDTO(offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvee : " + id)));
    }

    @Override
    public RecruitmentOfferDTO getByApplication(Long applicationId) {
        return mapper.toDTO(offerRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Aucune offre pour cette candidature")));
    }

    @Override
    public List<RecruitmentOfferDTO> getAll() {
        return offerRepository.findAll()
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<RecruitmentOfferDTO> getByEntreprise(Long entrepriseId) {
        return offerRepository.findByEntrepriseId(entrepriseId)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public RecruitmentOfferDTO updateStatus(Long id, OfferStatus status) {
        offerRepository.updateStatus(id, status);
        if (status == OfferStatus.ACCEPTED) {
            RecruitmentOffer offer = offerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvee : " + id));
            offer.getApplication().setStatus(ApplicationStatus.HIRED);
            applicationRepository.save(offer.getApplication());
        }
        return getById(id);
    }

    @Override
    public RecruitmentOfferDTO addContreOffre(Long id, String contreOffre) {
        offerRepository.addContreOffre(id, contreOffre);
        return getById(id);
    }

    @Override
    public List<RecruitmentOfferDTO> getByStatus(OfferStatus status) {
        return offerRepository.findByStatus(status)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }




}