package tn.esprit.mscontractservicee.repository;

import tn.esprit.mscontractservicee.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByContractIdOrderByOrdreAsc(Long contractId);

    @Query("""
            select m
            from Milestone m
            join m.contract c
            where c.clientId = :clientId
            order by c.id asc, m.ordre asc
            """)
    List<Milestone> findForClient(@Param("clientId") Long clientId);

    @Query("""
            select m
            from Milestone m
            join m.contract c
            where c.freelancerId = :freelancerId
              and c.dateSignature is not null
            order by c.id asc, m.ordre asc
            """)
    List<Milestone> findForSignedFreelancer(@Param("freelancerId") Long freelancerId);
}
