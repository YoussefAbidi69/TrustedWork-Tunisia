package tn.esprit.mscontractservicee.service;

import tn.esprit.mscontractservicee.entity.Milestone;
import tn.esprit.mscontractservicee.enums.MilestoneStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IMilestoneService {

    Milestone createMilestone(Milestone milestone);

    Milestone updateMilestone(Long id, Milestone milestone);

    Optional<Milestone> findById(Long id);


    Page<Milestone> findAll(Pageable pageable);

    Milestone updateStatus(Long id, MilestoneStatus status);

    Milestone startMilestone(Long id);

    Milestone submitMilestone(Long id);

    Milestone approveMilestone(Long id);

    Milestone autoApproveMilestone(Long id);

    void deleteMilestone(Long id);
    Milestone rejectMilestone(Long id, String rejectionReason);
    List<Milestone> findByContractId(Long contractId);

    List<Milestone> findForClient(Long clientId);

    List<Milestone> findForSignedFreelancer(Long freelancerId);

}
