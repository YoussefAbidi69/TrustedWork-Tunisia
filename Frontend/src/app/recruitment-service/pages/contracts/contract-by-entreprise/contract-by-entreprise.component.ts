import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContractService } from '../../../services/contract.service';
import { HiringContract } from '../../../models';
import { TableColumn } from '../../../shared/recruitment-table/recruitment-table.component';

@Component({
  selector: 'app-contract-by-entreprise',
  templateUrl: './contract-by-entreprise.component.html',
  styleUrls: ['./contract-by-entreprise.component.css']
})
export class ContractByEntrepriseComponent implements OnInit {
  contracts: HiringContract[] = [];
  filtered: HiringContract[] = [];
  loading = false;
  searched = false;
  entrepriseId: number | null = null;
  selectedStatus = '';
  selectedType = '';

  statusOptions = ['', 'DRAFT', 'SIGNED', 'ACTIVE', 'TERMINATED'];
  typeOptions = ['', 'CDI', 'CDD', 'CIVP', 'STAGE', 'ALTERNANCE'];

  columns: TableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'offerId', label: 'Offre ID' },
    { key: 'freelancerId', label: 'Freelancer ID' },
    { key: 'typeContrat', label: 'Type', type: 'badge' },
    { key: 'salaireFinal', label: 'Salaire Final', type: 'currency' },
    { key: 'dateDebutEffective', label: 'Début', type: 'date' },
    { key: 'status', label: 'Statut', type: 'badge' }
  ];

  constructor(private contractService: ContractService, private router: Router) { }

  ngOnInit(): void { }

  search(): void {
    if (!this.entrepriseId) return;
    this.loading = true;
    this.searched = true;
    this.contractService.getByEntreprise(this.entrepriseId).subscribe({
      next: (data) => { this.contracts = data; this.applyFilter(); this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  applyFilter(): void {
    this.filtered = this.contracts.filter(c => {
      const matchStatus = !this.selectedStatus || c.status === this.selectedStatus;
      const matchType = !this.selectedType || c.typeContrat === this.selectedType;
      return matchStatus && matchType;
    });
  }

  onFilterStatus(s: string): void { this.selectedStatus = s; this.applyFilter(); }
  onFilterType(t: string): void { this.selectedType = t; this.applyFilter(); }
  onView(row: HiringContract): void { this.router.navigate(['/recruitment/contracts', row.id]); }
}