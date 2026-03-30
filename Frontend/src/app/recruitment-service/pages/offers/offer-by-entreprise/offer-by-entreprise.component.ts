import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OfferService } from '../../../services/offer.service';
import { RecruitmentOffer } from '../../../models';
import { TableColumn } from '../../../shared/recruitment-table/recruitment-table.component';

@Component({
  selector: 'app-offer-by-entreprise',
  templateUrl: './offer-by-entreprise.component.html',
  styleUrls: ['./offer-by-entreprise.component.css']
})
export class OfferByEntrepriseComponent implements OnInit {
  offers: RecruitmentOffer[] = [];
  filtered: RecruitmentOffer[] = [];
  loading = false;
  searched = false;
  entrepriseId: number | null = null;
  selectedStatus = '';

  statusOptions = ['', 'SENT', 'ACCEPTED', 'DECLINED', 'NEGOTIATING'];

  columns: TableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'applicationId', label: 'Application ID' },
    { key: 'posteExact', label: 'Poste' },
    { key: 'salairePropose', label: 'Salaire', type: 'currency' },
    { key: 'deadlineReponse', label: 'Deadline', type: 'date' },
    { key: 'status', label: 'Statut', type: 'badge' }
  ];

  constructor(private offerService: OfferService, private router: Router) { }

  ngOnInit(): void { }

  search(): void {
    if (!this.entrepriseId) return;
    this.loading = true;
    this.searched = true;
    this.offerService.getByEntreprise(this.entrepriseId).subscribe({
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
  onView(row: RecruitmentOffer): void { this.router.navigate(['/recruitment/offers', row.id]); }
}