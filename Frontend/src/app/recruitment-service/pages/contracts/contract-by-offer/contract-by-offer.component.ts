import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContractService } from '../../../services/contract.service';
import { HiringContract } from '../../../models';

@Component({
  selector: 'app-contract-by-offer',
  templateUrl: './contract-by-offer.component.html',
  styleUrls: ['./contract-by-offer.component.css']
})
export class ContractByOfferComponent implements OnInit {
  contract: HiringContract | null = null;
  loading = false;
  searched = false;
  offerId: number | null = null;

  constructor(private contractService: ContractService, private router: Router) { }

  ngOnInit(): void { }

  search(): void {
    if (!this.offerId) return;
    this.loading = true;
    this.searched = true;
    this.contractService.getByOffer(this.offerId).subscribe({
      next: (data) => { this.contract = data; this.loading = false; },
      error: () => { this.contract = null; this.loading = false; }
    });
  }

  onView(): void {
    if (this.contract) this.router.navigate(['/recruitment/contracts', this.contract.id]);
  }
}