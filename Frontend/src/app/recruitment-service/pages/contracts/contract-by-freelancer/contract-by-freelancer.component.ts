import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContractService } from '../../../services/contract.service';
import { HiringContract } from '../../../models';
import { TableColumn } from '../../../shared/recruitment-table/recruitment-table.component';

@Component({
  selector: 'app-contract-by-freelancer',
  templateUrl: './contract-by-freelancer.component.html',
  styleUrls: ['./contract-by-freelancer.component.css']
})
export class ContractByFreelancerComponent implements OnInit {
  contracts: HiringContract[] = [];
  loading = false;
  searched = false;
  freelancerId: number | null = null;

  columns: TableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'offerId', label: 'Offre ID' },
    { key: 'entrepriseId', label: 'Entreprise ID' },
    { key: 'typeContrat', label: 'Type', type: 'badge' },
    { key: 'salaireFinal', label: 'Salaire Final', type: 'currency' },
    { key: 'dateDebutEffective', label: 'Début', type: 'date' },
    { key: 'status', label: 'Statut', type: 'badge' }
  ];

  constructor(private contractService: ContractService, private router: Router) { }

  ngOnInit(): void { }

  search(): void {
    if (!this.freelancerId) return;
    this.loading = true;
    this.searched = true;
    this.contractService.getByFreelancer(this.freelancerId).subscribe({
      next: (data) => { this.contracts = data; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  onView(row: HiringContract): void { this.router.navigate(['/recruitment/contracts', row.id]); }
}