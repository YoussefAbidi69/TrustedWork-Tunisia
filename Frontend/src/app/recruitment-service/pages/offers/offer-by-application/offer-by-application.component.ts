import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OfferService } from '../../../services/offer.service';
import { RecruitmentOffer } from '../../../models';

@Component({
  selector: 'app-offer-by-application',
  templateUrl: './offer-by-application.component.html',
  styleUrls: ['./offer-by-application.component.css']
})
export class OfferByApplicationComponent implements OnInit {
  offer: RecruitmentOffer | null = null;
  loading = false;
  searched = false;
  applicationId: number | null = null;

  constructor(private offerService: OfferService, private router: Router) { }

  ngOnInit(): void { }

  search(): void {
    if (!this.applicationId) return;
    this.loading = true;
    this.searched = true;
    this.offerService.getByApplication(this.applicationId).subscribe({
      next: (data) => { this.offer = data; this.loading = false; },
      error: () => { this.offer = null; this.loading = false; }
    });
  }

  onView(): void {
    if (this.offer) this.router.navigate(['/recruitment/offers', this.offer.id]);
  }
}