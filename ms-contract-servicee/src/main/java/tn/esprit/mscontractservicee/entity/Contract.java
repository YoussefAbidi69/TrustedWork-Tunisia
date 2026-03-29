package tn.esprit.mscontractservicee.entity;

import tn.esprit.mscontractservicee.enums.ContractStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Contract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String reference;
    Long clientId;
    Long freelancerId;
    Long clientWalletId;
    Long freelancerWalletId;
    Long projectId;
    String projectTitle;
    String description;
    BigDecimal montantTotal;
    Integer slaFreelancerHeures;
    Integer slaClientJours;
    LocalDateTime dateSignature;
    LocalDate dateDebut;
    LocalDate dateFin;

    @Builder.Default
    BigDecimal commissionRate = BigDecimal.valueOf(10.0);

    @Enumerated(EnumType.STRING)
    @Builder.Default
    ContractStatus status = ContractStatus.DRAFT;

    LocalDateTime cancelledAt;
    String cancellationReason;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    @JsonIgnore  // ✅ IGNORE LA LISTE DES MILESTONES
    private List<Milestone> milestones = new ArrayList<>();

    @OneToOne(mappedBy = "contract", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore  // ✅ IGNORE ESCROWACCOUNT
    private EscrowAccount escrowAccount;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    @JsonIgnore  // ✅ IGNORE DISPUTES
    private List<Dispute> disputes = new ArrayList<>();

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    @JsonIgnore  // ✅ IGNORE TRANSACTIONS
    private List<Transaction> transactions = new ArrayList<>();
}