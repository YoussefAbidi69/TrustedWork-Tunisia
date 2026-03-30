import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContractService } from '../../../services/contract.service';
import { HiringContract } from '../../../models';
import { TableColumn } from '../../../shared/recruitment-table/recruitment-table.component';

@Component({
  selector: 'app-contract-list',
  templateUrl: './contract-list.component.html',
  styleUrls: ['./contract-list.component.css']
})
export class ContractListComponent implements OnInit {
  contracts: HiringContract[] = [];
  filtered: HiringContract[] = [];
  loading = true;
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

  ngOnInit(): void { this.loadData(); }

  loadData(): void {
    this.loading = true;
    this.contractService.getAll().subscribe({
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

  onCreate(): void { this.router.navigate(['/recruitment/contracts/create']); }
  onView(row: HiringContract): void { this.router.navigate(['/recruitment/contracts', row.id]); }
  onEdit(row: HiringContract): void { this.router.navigate(['/recruitment/contracts', row.id, 'edit']); }
  onDelete(row: HiringContract): void {
    if (confirm('Supprimer ce contrat ?')) {
      this.contractService.delete(row.id!).subscribe({ next: () => this.loadData() });
    }
  }
}