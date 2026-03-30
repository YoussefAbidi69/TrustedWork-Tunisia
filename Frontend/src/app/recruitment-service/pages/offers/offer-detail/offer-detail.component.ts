import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OfferService } from '../../../services/offer.service';
import { RecruitmentOffer } from '../../../models';

@Component({
  selector: 'app-offer-detail',
  templateUrl: './offer-detail.component.html',
  styleUrls: ['./offer-detail.component.css']
})
export class OfferDetailComponent implements OnInit {
  offer: RecruitmentOffer | null = null;
  loading = true;
  statusLoading = false;
  showStatusMenu = false;
  showContreOffreModal = false;
  contreOffreText = '';

  statusOptions = ['SENT', 'ACCEPTED', 'DECLINED', 'NEGOTIATING'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private offerService: OfferService
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.offerService.getById(id).subscribe({
        next: (data) => { this.offer = data; this.loading = false; },
        error: () => { this.loading = false; }
      });
    }
  }

  // PATCH /offers/{id}/status
  changeStatus(status: string): void {
    if (!this.offer || status === this.offer.status) { this.showStatusMenu = false; return; }
    this.statusLoading = true;
    this.showStatusMenu = false;
    this.offerService.updateStatus(this.offer.id!, status).subscribe({
      next: (updated) => { this.offer = updated; this.statusLoading = false; },
      error: () => { this.statusLoading = false; }
    });
  }

  // PATCH /offers/{id}/contre-offre
  submitContreOffre(): void {
    if (!this.offer || !this.contreOffreText.trim()) return;
    this.showContreOffreModal = false;
    this.offerService.addContreOffre(this.offer.id!, this.contreOffreText).subscribe({
      next: (updated) => { this.offer = updated; this.contreOffreText = ''; }
    });
  }

  toggleStatusMenu(): void { this.showStatusMenu = !this.showStatusMenu; }
  goBack(): void { this.router.navigate(['/recruitment/offers']); }
  onEdit(): void { this.router.navigate(['/recruitment/offers', this.offer!.id, 'edit']); }
  onDelete(): void {
    if (this.offer && confirm('Supprimer cette offre ?')) {
      this.offerService.delete(this.offer.id!).subscribe({ next: () => this.goBack() });
    }
  }
}