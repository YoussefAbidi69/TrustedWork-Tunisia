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
    
    const payload = { ...this.milestone } as any;
    if (payload.deadline === '') payload.deadline = null;
    
    if (this.isEditMode) {
      this.milestoneService.update(this.milestone.id!, payload).subscribe({
        next: () => {
          this.router.navigate(['/contracts', this.milestone.contractId]);
        },
        error: (err) => {
          this.error = 'Erreur lors de la modification';
          this.loading = false;
          console.error(err);
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
          console.error(err);
        }
      });
    }
  }
}