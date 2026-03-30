import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OfferService } from '../../../services/offer.service';
import { RecruitmentOffer } from '../../../models';
import { TableColumn } from '../../../shared/recruitment-table/recruitment-table.component';

@Component({
  selector: 'app-offer-list',
  templateUrl: './offer-list.component.html',
  styleUrls: ['./offer-list.component.css']
})
export class OfferListComponent implements OnInit {
  offers: RecruitmentOffer[] = [];
  filtered: RecruitmentOffer[] = [];
  loading = true;
  selectedStatus = '';

  statusOptions = ['', 'SENT', 'ACCEPTED', 'DECLINED', 'NEGOTIATING'];

  columns: TableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'applicationId', label: 'Application ID' },
    { key: 'posteExact', label: 'Poste' },
    { key: 'salairePropose', label: 'Salaire', type: 'currency' },
    { key: 'periodeEssaiMois', label: 'Essai (mois)' },
    { key: 'deadlineReponse', label: 'Deadline', type: 'date' },
    { key: 'status', label: 'Statut', type: 'badge' }
  ];

  constructor(private offerService: OfferService, private router: Router) { }

  ngOnInit(): void { this.loadData(); }

  loadData(): void {
    this.loading = true;
    this.offerService.getAll().subscribe({
      next: (data) => { this.offers = data; this.applyFilter(); this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  applyFilter(): void {
    this.filtered = this.selectedStatus
      ? this.offers.filter(o => o.status === this.selectedStatus)
      : [...this.offers];
  }

  onFilterStatus(s: string): void { this.selectedStatus = s; this.applyFilter(); }

  onCreate(): void { this.router.navigate(['/recruitment/offers/create']); }
  onView(row: RecruitmentOffer): void { this.router.navigate(['/recruitment/offers', row.id]); }
  onEdit(row: RecruitmentOffer): void { this.router.navigate(['/recruitment/offers', row.id, 'edit']); }
  onDelete(row: RecruitmentOffer): void {
    if (confirm('Supprimer cette offre ?')) {
      this.offerService.delete(row.id!).subscribe({ next: () => this.loadData() });
    }
  }
}