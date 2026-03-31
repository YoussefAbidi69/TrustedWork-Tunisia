import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { MilestoneService } from '../../../core/services/milestone.service';
import { Milestone } from '../../../core/models/milestone.model';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-milestone-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './milestone-form.html',
  styleUrl: './milestone-form.css'
})
export class MilestoneFormComponent implements OnInit {
  milestone: Milestone = {
    contractId: 0,
    titre: '',
    description: '',
    montant: 0,
    deadline: '',
    status: 'PENDING'
  };
  
  isEditMode = false;
  loading = false;
  error = '';

  constructor(
    private milestoneService: MilestoneService,
    private router: Router,
    private route: ActivatedRoute,
    public authService: AuthService
  ) {}

  get isFreelancer(): boolean {
    return this.authService.getRole() === 'FREELANCER';
  }

  get isClient(): boolean {
    return this.authService.getRole() === 'CLIENT';
  }

  get isAdmin(): boolean {
    return this.authService.getRole() === 'ADMIN';
  }

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    const contractId = this.route.snapshot.params['contractId'];
    
    if (contractId) {
      this.milestone.contractId = +contractId;
    }
    
    if (id) {
      this.isEditMode = true;
      this.loadMilestone(id);
    }
  }

  loadMilestone(id: number): void {
    this.loading = true;
    this.milestoneService.getById(id).subscribe({
      next: (milestone) => {
        this.milestone = milestone;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement du jalon';
        this.loading = false;
        console.error(err);
      }
    });
  }

  onSubmit(): void {
    this.loading = true;
    
    // Special case: after a client rejects a milestone, they can adjust only the deadline (backend blocks PUT).
    if (this.isEditMode && this.milestone.id && this.milestone.status === 'REJECTED') {
      if (!this.isClient && !this.isAdmin) {
        this.error = "Vous n'avez pas l'autorisation de modifier la deadline de ce jalon.";
        this.loading = false;
        return;
      }
      if (!this.milestone.deadline) {
        this.error = "La deadline est obligatoire.";
        this.loading = false;
        return;
      }

      this.milestoneService.updateRejectedDeadline(this.milestone.id, String(this.milestone.deadline || '')).subscribe({
        next: () => {
          this.router.navigate(['/contracts', this.milestone.contractId]);
        },
        error: (err) => {
          this.error = 'Erreur lors de la modification de la deadline';
          this.loading = false;
          console.error('Update Deadline Error:', err);
        }
      });
      return;
    }

    // Nettoyage du payload pour s'assurer que les types correspondent au backend
    const payload: Milestone = {
      ...this.milestone,
      contractId: Number(this.milestone.contractId),
      montant: Number(this.milestone.montant),
      deadline: this.milestone.deadline || null
    };

    if (this.isEditMode) {
      this.milestoneService.update(this.milestone.id!, payload).subscribe({
        next: () => {
          this.router.navigate(['/contracts', this.milestone.contractId]);
        },
        error: (err) => {
          this.error = 'Erreur lors de la modification (Vérifiez les données)';
          this.loading = false;
          console.error('Update Error:', err);
        }
      });
    } else {
      this.milestoneService.create(payload).subscribe({
        next: (milestone) => {
          this.router.navigate(['/contracts', milestone.contractId]);
        },
        error: (err) => {
          this.error = 'Erreur lors de la création';
          this.loading = false;
          console.error('Create Error:', err);
        }
      });
    }
  }
}
