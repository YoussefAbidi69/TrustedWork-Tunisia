import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ContractService } from '../../../core/services/contract.service';
import { MilestoneService } from '../../../core/services/milestone.service';
import { Contract } from '../../../core/models/contract.model';
import { Milestone } from '../../../core/models/milestone.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-contract-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './contract-list.html',
  styleUrl: './contract-list.css'
})
export class ContractListComponent implements OnInit {
  contracts: Contract[] = [];
  milestonesMap: { [contractId: number]: Milestone[] } = {};
  loadingMilestones: { [contractId: number]: boolean } = {};
  expandedContracts: { [contractId: number]: boolean } = {};
  loading = false;
  error = '';

  constructor(
    private contractService: ContractService, 
    private milestoneService: MilestoneService,
    public authService: AuthService
  ) {}

  get isClient(): boolean {
    return this.authService.getRole() === 'CLIENT';
  }

  get isFreelancer(): boolean {
    return this.authService.getRole() === 'FREELANCER';
  }

  get isAdmin(): boolean {
    return this.authService.getRole() === 'ADMIN';
  }

  ngOnInit(): void {
    this.loadContracts();
  }

  loadContracts(): void {
    this.loading = true;
    this.contractService.getAll(0, 100).subscribe({
      next: (response) => {
        this.contracts = response.content || response;
        this.loading = false;
        this.contracts.forEach(contract => {
          if (contract.id) this.loadMilestonesForContract(contract.id);
        });
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des contrats';
        this.loading = false;
        console.error(err);
      }
    });
  }

  loadMilestonesForContract(contractId: number): void {
    this.loadingMilestones[contractId] = true;
    this.milestoneService.getByContractId(contractId).subscribe({
      next: (milestones) => {
        this.milestonesMap[contractId] = milestones;
        this.loadingMilestones[contractId] = false;
      },
      error: (err) => {
        console.error(err);
        this.loadingMilestones[contractId] = false;
      }
    });
  }

  toggleMilestones(contractId: number): void {
    this.expandedContracts[contractId] = !this.expandedContracts[contractId];
  }

  startMilestone(contractId: number, milestoneId: number): void {
    if (confirm('Voulez-vous commencer ce jalon ?')) {
      this.milestoneService.start(milestoneId).subscribe({
        next: () => this.loadMilestonesForContract(contractId),
        error: (err) => console.error(err)
      });
    }
  }

  submitMilestone(contractId: number, milestoneId: number): void {
    if (confirm('Voulez-vous soumettre ce jalon pour validation ?')) {
      this.milestoneService.submit(milestoneId).subscribe({
        next: () => this.loadMilestonesForContract(contractId),
        error: (err) => console.error(err)
      });
    }
  }

  approveMilestone(contractId: number, milestoneId: number): void {
    if (confirm('Voulez-vous approuver ce jalon ?')) {
      this.milestoneService.approve(milestoneId).subscribe({
        next: () => this.loadMilestonesForContract(contractId),
        error: (err) => console.error(err)
      });
    }
  }

  rejectMilestone(contractId: number, milestoneId: number): void {
    const reason = prompt('Motif du rejet :');
    if (reason !== null && reason.trim() !== '') {
      this.milestoneService.reject(milestoneId, reason).subscribe({
        next: () => this.loadMilestonesForContract(contractId),
        error: (err) => console.error(err)
      });
    } else if (reason !== null) {
      alert('Un motif est obligatoire pour rejeter un jalon.');
    }
  }

  deleteContract(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce contrat ?')) {
      this.contractService.delete(id).subscribe({
        next: () => {
          this.loadContracts();
        },
        error: (err) => {
          console.error(err);
          alert('Erreur lors de la suppression');
        }
      });
    }
  }

  getStatusClass(status: string): string {
    return `status-badge status-${status}`;
  }
}
