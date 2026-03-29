import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ContractService } from '../../../core/services/contract.service';
import { MilestoneService } from '../../../core/services/milestone.service';
import { Contract } from '../../../core/models/contract.model';
import { Milestone } from '../../../core/models/milestone.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-contract-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './contract-detail.html',
  styleUrl: './contract-detail.css'
})
export class ContractDetailComponent implements OnInit {
  contract: Contract | null = null;
  milestones: Milestone[] = [];
  loading = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
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
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.loadContract(id);
      this.loadMilestones(id);
    }
  }

  loadContract(id: number): void {
    this.loading = true;
    this.contractService.getById(id).subscribe({
      next: (contract) => {
        this.contract = contract;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement du contrat';
        this.loading = false;
        console.error(err);
      }
    });
  }

  loadMilestones(contractId: number): void {
    this.milestoneService.getByContractId(contractId).subscribe({
      next: (milestones) => {
        this.milestones = milestones;
      },
      error: (err) => {
        console.error('Erreur chargement jalons:', err);
      }
    });
  }

  getStatusClass(status: string): string {
    return `status-badge status-${status}`;
  }

  getTotalMilestonesAmount(): number {
    return this.milestones.reduce((sum, m) => sum + m.montant, 0);
  }

  deleteMilestone(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce jalon ?')) {
      this.milestoneService.delete(id).subscribe({
        next: () => this.loadMilestones(this.contract!.id!),
        error: (err) => alert('Erreur lors de la suppression')
      });
    }
  }

  startMilestone(id: number): void {
    if (confirm('Voulez-vous commencer ce jalon ?')) {
      this.milestoneService.start(id).subscribe({
        next: () => this.loadMilestones(this.contract!.id!),
        error: (err) => console.error(err)
      });
    }
  }

  submitMilestone(id: number): void {
    if (confirm('Voulez-vous soumettre ce jalon pour validation ?')) {
      this.milestoneService.submit(id).subscribe({
        next: () => this.loadMilestones(this.contract!.id!),
        error: (err) => console.error(err)
      });
    }
  }

  approveMilestone(id: number): void {
    if (confirm('Voulez-vous approuver ce jalon ?')) {
      this.milestoneService.approve(id).subscribe({
        next: () => this.loadMilestones(this.contract!.id!),
        error: (err) => console.error(err)
      });
    }
  }

  rejectMilestone(id: number): void {
    const reason = prompt('Motif du rejet :');
    if (reason !== null && reason.trim() !== '') {
      this.milestoneService.reject(id, reason).subscribe({
        next: () => this.loadMilestones(this.contract!.id!),
        error: (err) => console.error(err)
      });
    } else if (reason !== null) {
      alert('Un motif est obligatoire pour rejeter un jalon.');
    }
  }
}