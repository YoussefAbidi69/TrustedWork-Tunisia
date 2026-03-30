package tn.esprit.mscontractservicee.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.mscontractservicee.entity.Milestone;
import tn.esprit.mscontractservicee.enums.MilestoneStatus;
import tn.esprit.mscontractservicee.repository.MilestoneRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MilestoneServiceImpl implements IMilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final IPaymentService paymentService;

    @Override
    public Milestone createMilestone(Milestone milestone) {
        if (milestone.getContractId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contractId is required");
        }
        log.info("Creating milestone for contract: {}", milestone.getContractId());
        milestone.setStatus(MilestoneStatus.PENDING);
        return milestoneRepository.save(milestone);
    }

    @Override
    public Milestone updateMilestone(Long id, Milestone milestone) {
        log.info("Updating milestone with id: {}", id);
        Milestone existing = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));

        if (existing.getStatus() != MilestoneStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only PENDING milestones can be updated. Current status: " + existing.getStatus());
        }

        existing.setTitre(milestone.getTitre());
        existing.setDescription(milestone.getDescription());
        existing.setMontant(milestone.getMontant());
        existing.setDeadline(milestone.getDeadline());

        return milestoneRepository.save(existing);
    }

    @Override
    public Optional<Milestone> findById(Long id) {
        return milestoneRepository.findById(id);
    }

    @Override
    public Page<Milestone> findAll(Pageable pageable) {
        return milestoneRepository.findAll(pageable);
    }

    @Override
    public Milestone updateStatus(Long id, MilestoneStatus status) {
        log.info("Updating milestone {} status to: {}", id, status);
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));

        milestone.setStatus(status);
        return milestoneRepository.save(milestone);
    }

    @Override
    public Milestone startMilestone(Long id) {
        log.info("Starting milestone: {}", id);
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));

        if (milestone.getStatus() != MilestoneStatus.PENDING && milestone.getStatus() != MilestoneStatus.REJECTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Milestone cannot be started. Current status: " + milestone.getStatus());
        }

        milestone.setStatus(MilestoneStatus.IN_PROGRESS);
        milestone.setStartedAt(LocalDateTime.now());

        return milestoneRepository.save(milestone);
    }

    @Override
    public Milestone submitMilestone(Long id) {
        log.info("Submitting milestone: {}", id);
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));

        if (milestone.getStatus() != MilestoneStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Milestone cannot be submitted. Current status: " + milestone.getStatus());
        }

        milestone.setStatus(MilestoneStatus.SUBMITTED);
        milestone.setSubmittedAt(LocalDateTime.now());

        return milestoneRepository.save(milestone);
    }

    @Override
    public Milestone approveMilestone(Long id) {
        log.info("Approving milestone: {}", id);
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));

        if (milestone.getStatus() != MilestoneStatus.SUBMITTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Milestone cannot be approved. Current status: " + milestone.getStatus());
        }

        milestone.setStatus(MilestoneStatus.APPROVED);
        milestone.setValidatedAt(LocalDateTime.now());

        Milestone saved = milestoneRepository.save(milestone);
        try {
            paymentService.releaseApprovedMilestone(saved.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment release failed: " + e.getMessage(), e);
        }
        return saved;
    }

    @Override
    public Milestone autoApproveMilestone(Long id) {
        log.info("Auto-approving milestone: {}", id);
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));

        if (milestone.getStatus() != MilestoneStatus.SUBMITTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Milestone cannot be auto-approved. Current status: " + milestone.getStatus());
        }

        milestone.setStatus(MilestoneStatus.AUTO_APPROVED);
        milestone.setValidatedAt(LocalDateTime.now());

        Milestone saved = milestoneRepository.save(milestone);
        try {
            paymentService.releaseApprovedMilestone(saved.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment release failed: " + e.getMessage(), e);
        }
        return saved;
    }

    @Override
    public void deleteMilestone(Long id) {
        log.info("Deleting milestone with id: {}", id);
        milestoneRepository.deleteById(id);
    }

    @Override
    public Milestone rejectMilestone(Long id, String rejectionReason) {
        log.info("Rejecting milestone: {} with reason: {}", id, rejectionReason);

        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Milestone not found with id: " + id));

        if (milestone.getStatus() != MilestoneStatus.SUBMITTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Milestone cannot be rejected. Current status: " + milestone.getStatus());
        }

        milestone.setStatus(MilestoneStatus.REJECTED);
        milestone.setRejectionReason(rejectionReason);
        milestone.setValidatedAt(LocalDateTime.now());

        return milestoneRepository.save(milestone);
    }

    @Override
    public List<Milestone> findByContractId(Long contractId) {
        log.info("Finding milestones for contract: {}", contractId);
        return milestoneRepository.findByContractIdOrderByOrdreAsc(contractId);
    }

    @Override
    public List<Milestone> findForClient(Long clientId) {
        if (clientId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "clientId is required");
        }
        log.info("Finding milestones for client: {}", clientId);
        return milestoneRepository.findForClient(clientId);
    }

    @Override
    public List<Milestone> findForSignedFreelancer(Long freelancerId) {
        if (freelancerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "freelancerId is required");
        }
        log.info("Finding milestones for signed freelancer contracts: {}", freelancerId);
        return milestoneRepository.findForSignedFreelancer(freelancerId);
    }
}
