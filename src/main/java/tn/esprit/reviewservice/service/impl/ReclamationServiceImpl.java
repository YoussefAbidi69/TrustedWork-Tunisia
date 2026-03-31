package tn.esprit.reviewservice.service.impl;

import org.springframework.stereotype.Service;
import tn.esprit.reviewservice.dto.request.ReclamationRequest;
import tn.esprit.reviewservice.dto.response.ReclamationResponse;
import tn.esprit.reviewservice.entity.Reclamation;
import tn.esprit.reviewservice.entity.enums.StatusReclamation;
import tn.esprit.reviewservice.exception.ResourceNotFoundException;
import tn.esprit.reviewservice.mapper.ReclamationMapper;
import tn.esprit.reviewservice.repository.ReclamationRepository;
import tn.esprit.reviewservice.service.interfaces.IReclamationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReclamationServiceImpl implements IReclamationService {

    private final ReclamationRepository repository;
    private final ReclamationMapper mapper;

    public ReclamationServiceImpl(ReclamationRepository repository, ReclamationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ReclamationResponse createReclamation(ReclamationRequest request) {
        Reclamation r = mapper.toEntity(request);
        return mapper.toResponse(repository.save(r));
    }

    @Override
    public List<ReclamationResponse> getAllReclamations() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReclamationResponse getReclamationById(Long id) {
        return mapper.toResponse(
                repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Reclamation introuvable"))
        );
    }

    @Override
    public ReclamationResponse resolveReclamation(Long id) {
        Reclamation r = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reclamation introuvable"));

        r.setStatus(StatusReclamation.CONFIRMED);
        r.setResolvedAt(LocalDateTime.now());

        return mapper.toResponse(repository.save(r));
    }

    @Override
    public void deleteReclamation(Long id) {
        Reclamation r = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reclamation introuvable"));

        repository.delete(r);
    }
}