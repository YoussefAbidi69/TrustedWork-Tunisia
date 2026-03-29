import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MilestoneService } from '../../../core/services/milestone.service';
import { Milestone } from '../../../core/models/milestone.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-milestone-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './milestone-list.html',
  styleUrl: './milestone-list.css'
})
export class MilestoneListComponent implements OnInit {
  milestones: Milestone[] = [];
  contractId: number | null = null;
  loading = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
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
    this.contractId = this.route.snapshot.params['contractId'];
    this.loadMilestones();
  }

  loadMilestones(): void {
    this.loading = true;
    
    if (this.contractId) {
      this.milestoneService.getByContractId(this.contractId).subscribe({
        next: (milestones) => {
          this.milestones = milestones;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Erreur lors du chargement des jalons';
          this.loading = false;
          console.error(err);
        }
      });
    } else {
      if (this.isAdmin) {
        this.milestoneService.getAll(0, 100).subscribe({
          next: (response) => {
            this.milestones = response.content || response;
            this.loading = false;
          },
          error: (err) => {
            this.error = 'Erreur lors du chargement des jalons';
            this.loading = false;
            console.error(err);
          }
        });
      } else {
        this.milestones = [];
        this.loading = false;
        this.error = 'Veuillez consulter vos jalons depuis la page de détails de l\'un de vos contrats.';
      }
    }
  }

  deleteMilestone(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce jalon ?')) {
      this.milestoneService.delete(id).subscribe({
        next: () => {
          this.loadMilestones();
        },
        error: (err) => {
          console.error(err);
          alert('Erreur lors de la suppression');
        }
      });
    }
  }

  startMilestone(id: number): void {
    if (confirm('Voulez-vous commencer ce jalon ?')) {
      this.milestoneService.start(id).subscribe({
        next: () => this.loadMilestones(),
        error: (err) => console.error(err)
      });
    }
  }

  submitMilestone(id: number): void {
    if (confirm('Voulez-vous soumettre ce jalon pour validation ?')) {
      this.milestoneService.submit(id).subscribe({
        next: () => this.loadMilestones(),
        error: (err) => console.error(err)
      });
    }
  }

  approveMilestone(id: number): void {
    if (confirm('Voulez-vous approuver ce jalon ?')) {
      this.milestoneService.approve(id).subscribe({
        next: () => this.loadMilestones(),
        error: (err) => console.error(err)
      });
    }
  }

  rejectMilestone(id: number): void {
    const reason = prompt('Motif du rejet :');
    if (reason !== null && reason.trim() !== '') {
      this.milestoneService.reject(id, reason).subscribe({
        next: () => this.loadMilestones(),
        error: (err) => console.error(err)
      });
    } else if (reason !== null) {
      alert('Un motif est obligatoire pour rejeter un jalon.');
    }
  }

  getStatusClass(status: string): string {
    return `status-badge status-${status}`;
  }
}