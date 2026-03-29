import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { WalletService } from '../../../core/services/wallet.service';

export interface Wallet {
  id: number;
  userId: number;
  balance: number;
  totalEarned: number;
  totalSpent: number;
  totalCommissionPaid: number;
  stripeAccountId: string;
  stripeAccountStatus: string;
  // createdAt et updatedAt peuvent ne pas exister dans la réponse API
}

export interface Transaction {
  id: number;
  reference: string;
  type: string;
  montant: number;
  description: string;
  status: string;
  createdAt: Date;
}

@Component({
  selector: 'app-wallet-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wallet-detail.html',
  styleUrls: ['./wallet-detail.css']
})
export class WalletDetailComponent implements OnInit {
  wallet: Wallet | null = null;
  transactions: Transaction[] = [];
  stripeStatus: string = '';
  stripeStatusMessage: string = '';
  loading: boolean = false;
  userId: number = 1;

  constructor(
    private walletService: WalletService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadWallet();
    this.loadStripeStatus();
    this.loadTransactions();
  }

  loadWallet(): void {
    this.loading = true;
    this.walletService.getWallet(this.userId).subscribe({
      next: (wallet) => {
        // ✅ Utiliser directement le wallet reçu de l'API
        this.wallet = wallet;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading wallet:', err);
        this.loading = false;
        // Simuler un wallet pour les tests
        this.wallet = {
          id: 1,
          userId: this.userId,
          balance: 1500,
          totalEarned: 2500,
          totalSpent: 1000,
          totalCommissionPaid: 250,
          stripeAccountId: '',
          stripeAccountStatus: 'NOT_CREATED'
        };
      }
    });
  }

  loadStripeStatus(): void {
    this.walletService.getStripeStatus(this.userId).subscribe({
      next: (res) => {
        this.stripeStatus = res.status;
        this.updateStatusMessage();
      },
      error: (err) => {
        console.error('Error loading stripe status:', err);
        this.stripeStatus = 'NOT_CREATED';
        this.updateStatusMessage();
      }
    });
  }

  loadTransactions(): void {
    // Données mockées pour les tests
    this.transactions = [
      {
        id: 1,
        reference: 'TRX-ABC123',
        type: 'CREDIT',
        montant: 500,
        description: 'Paiement contrat #1 - Jalon 1',
        status: 'PROCESSED',
        createdAt: new Date()
      },
      {
        id: 2,
        reference: 'TRX-DEF456',
        type: 'DEBIT',
        montant: 1000,
        description: 'Paiement contrat #2',
        status: 'PROCESSED',
        createdAt: new Date()
      }
    ];
  }

  updateStatusMessage(): void {
    switch (this.stripeStatus) {
      case 'ACTIVE':
        this.stripeStatusMessage = 'Votre compte Stripe est actif. Vous pouvez recevoir des paiements.';
        break;
      case 'PENDING':
        this.stripeStatusMessage = 'Votre compte Stripe est en cours de vérification.';
        break;
      case 'INCOMPLETE':
        this.stripeStatusMessage = 'Veuillez finaliser votre inscription Stripe.';
        break;
      default:
        this.stripeStatusMessage = 'Configurez Stripe pour recevoir vos paiements.';
    }
  }

  setupStripe(): void {
    this.router.navigate(['/wallet/stripe-onboarding', this.userId]);
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE': return 'status-active';
      case 'PENDING': return 'status-pending';
      case 'INCOMPLETE': return 'status-incomplete';
      default: return 'status-default';
    }
  }
}