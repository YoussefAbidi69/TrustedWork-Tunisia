import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { WalletService } from '../../../core/services/wallet.service';

@Component({
  selector: 'app-stripe-onboarding',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './stripe-onboarding.html',
  styleUrls: ['./stripe-onboarding.css']
})
export class StripeOnboardingComponent implements OnInit {
  userId: number = 0;
  loading: boolean = false;
  status: string = '';
  onboardingLink: string = '';
  error: string = '';

  constructor(
    private route: ActivatedRoute,
    private walletService: WalletService
  ) {}

  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.params['userId']);
    if (this.userId) {
      this.checkStatus();
    } else {
      this.error = 'Utilisateur non identifié';
    }
  }

  checkStatus(): void {
    this.loading = true;
    this.walletService.getStripeStatus(this.userId).subscribe({
      next: (res) => {
        this.status = res.status;
        this.loading = false;
        
        if (this.status === 'NOT_CREATED') {
          this.createAccount();
        } else if (this.status === 'INCOMPLETE') {
          this.getOnboardingLink();
        }
      },
      error: (err) => {
        console.error('Error checking status:', err);
        this.status = 'NOT_CREATED';
        this.loading = false;
        this.createAccount();
      }
    });
  }

  createAccount(): void {
    // Email fixe pour les tests
    const email = 'freelancer@trustedwork.com';
    this.walletService.createStripeAccount(this.userId, email, 'FR').subscribe({
      next: (res) => {
        this.onboardingLink = res.onboardingLink;
        this.status = 'PENDING';
      },
      error: (err) => {
        this.error = err.error?.error || 'Erreur lors de la création du compte';
        console.error('Error creating account:', err);
      }
    });
  }

  getOnboardingLink(): void {
    this.walletService.getOnboardingLink(this.userId).subscribe({
      next: (res) => {
        this.onboardingLink = res.onboardingLink;
      },
      error: (err) => {
        this.error = err.error?.error || 'Erreur lors de la récupération du lien';
        console.error('Error getting onboarding link:', err);
      }
    });
  }

  redirectToStripe(): void {
    if (this.onboardingLink) {
      window.location.href = this.onboardingLink;
    }
  }
}