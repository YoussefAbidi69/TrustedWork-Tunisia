import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { ContractService } from '../../../core/services/contract.service';
import { Contract } from '../../../core/models/contract.model';
import { ContractStatus } from '../../../core/models/enums/contract-status.enum';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-contract-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './contract-form.html',
  styleUrl: './contract-form.css'
})
export class ContractFormComponent implements OnInit {
  contract: Contract = {
    reference: '',
    clientId: 0,
    freelancerId: 0,
    clientWalletId: 0,
    freelancerWalletId: 0,
    projectId: null,
    projectTitle: '',
    description: '',
    montantTotal: 0,
    slaFreelancerHeures: 24,
    slaClientJours: 7,
    dateDebut: '',
    dateFin: '',
    commissionRate: 10,
    status: ContractStatus.DRAFT
  };
  
  isEditMode = false;
  loading = false;
  error = '';

  constructor(
    private contractService: ContractService,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Automatically assign the logged in user ID as the client
    if (!this.isEditMode) {
      this.contract.clientId = this.authService.getUserId();
    }
    
    const id = this.route.snapshot.params['id'];
    if (id) {
      if (this.authService.getRole() === 'CLIENT') {
        this.error = "Les clients n'ont plus l'autorisation de modifier les contrats existants.";
        return;
      }
      this.isEditMode = true;
      this.loadContract(id);
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

  onSubmit(): void {
    this.loading = true;
    
    const payload = { ...this.contract } as any;
    if (payload.dateDebut === '') payload.dateDebut = null;
    if (payload.dateFin === '') payload.dateFin = null;
    if (payload.projectId === 0 || payload.projectId === '') payload.projectId = null;
    
    delete payload.clientId;
    delete payload.clientWalletId;
    delete payload.freelancerWalletId;
    
    if (this.isEditMode) {
      this.contractService.update(this.contract.id!, payload).subscribe({
        next: () => {
          this.router.navigate(['/contracts', this.contract.id]);
        },
        error: (err) => {
          this.error = 'Erreur lors de la modification';
          this.loading = false;
          console.error(err);
        }
      });
    } else {
      this.contractService.create(payload).subscribe({
        next: (contract) => {
          this.router.navigate(['/contracts', contract.id]);
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