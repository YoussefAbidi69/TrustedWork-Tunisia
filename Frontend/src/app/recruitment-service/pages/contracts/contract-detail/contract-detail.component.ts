import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ContractService } from '../../../services/contract.service';
import { HiringContract } from '../../../models';

@Component({
  selector: 'app-contract-detail',
  templateUrl: './contract-detail.component.html',
  styleUrls: ['./contract-detail.component.css']
})
export class ContractDetailComponent implements OnInit {
  contract: HiringContract | null = null;
  loading = true;
  signerLoading = false;
  showFeedbackModal = false;
  feedbackText = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private contractService: ContractService
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.contractService.getById(id).subscribe({
        next: (data) => { this.contract = data; this.loading = false; },
        error: () => { this.loading = false; }
      });
    }
  }

  // PATCH /contracts/{id}/signer
  signerContrat(): void {
    if (!this.contract || !confirm('Confirmer la signature du contrat ?')) return;
    this.signerLoading = true;
    this.contractService.signerContrat(this.contract.id!).subscribe({
      next: (updated) => { this.contract = updated; this.signerLoading = false; },
      error: () => { this.signerLoading = false; }
    });
  }

  // PATCH /contracts/{id}/feedback
  submitFeedback(): void {
    if (!this.contract || !this.feedbackText.trim()) return;
    this.showFeedbackModal = false;
    this.contractService.addFeedback(this.contract.id!, this.feedbackText).subscribe({
      next: (updated) => { this.contract = updated; this.feedbackText = ''; }
    });
  }

  goBack(): void { this.router.navigate(['/recruitment/contracts']); }
  onEdit(): void { this.router.navigate(['/recruitment/contracts', this.contract!.id, 'edit']); }
  onDelete(): void {
    if (this.contract && confirm('Supprimer ce contrat ?')) {
      this.contractService.delete(this.contract.id!).subscribe({ next: () => this.goBack() });
    }
  }

  get isSigned(): boolean { return this.contract?.status === 'SIGNED' || this.contract?.status === 'ACTIVE'; }
}