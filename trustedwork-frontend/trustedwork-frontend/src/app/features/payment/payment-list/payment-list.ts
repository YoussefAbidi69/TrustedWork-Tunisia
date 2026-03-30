import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ContractService } from '../../../core/services/contract.service';
import { Contract } from '../../../core/models/contract.model';
import { AuthService } from '../../../core/services/auth.service';
import { MilestoneService } from '../../../core/services/milestone.service';

@Component({
  selector: 'app-payment-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './payment-list.html',
  styleUrl: './payment-list.css'
})
export class PaymentListComponent implements OnInit {
  contracts: any[] = [];
  loading = false;
  error = '';
  milestonesSum: { [contractId: number]: number } = {};

  constructor(
    private contractService: ContractService,
    private milestoneService: MilestoneService,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadContracts();
  }

  loadContracts(): void {
    this.loading = true;

    this.contractService.getAll(0, 100).subscribe({
      next: (response) => {
        const raw = response.content || response;
        this.contracts = (raw || []).map((c: any) => ({
          ...c,
          montantTotal: Number(c?.montantTotal ?? 0),
        }));
        this.loading = false;
        
        // Check milestone sums for valid payment
        this.contracts.forEach(c => {
          if (c.status === 'DRAFT') {
            this.checkMilestones(c.id!);
          }
        });
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des contrats';
        this.loading = false;
        console.error(err);
      }
    });
  }

  checkMilestones(contractId: number): void {
    this.milestoneService.getByContractId(contractId).subscribe({
      next: (milestones) => {
        this.milestonesSum[contractId] = (milestones || []).reduce((sum, m: any) => sum + Number(m?.montant ?? 0), 0);
      }
    });
  }

  isReadyToPay(contract: any): boolean {
    if (contract.status !== 'DRAFT') return false;
    const sum = this.milestonesSum[contract.id] || 0;
    // Handle floating point precision
    return Math.abs(sum - contract.montantTotal) < 0.01;
  }

  goToCheckout(id: number): void {
    this.router.navigate(['/payment/checkout', id]);
  }

  getStatusClass(status: string): string {
    return `status-badge status-${status}`;
  }
}
