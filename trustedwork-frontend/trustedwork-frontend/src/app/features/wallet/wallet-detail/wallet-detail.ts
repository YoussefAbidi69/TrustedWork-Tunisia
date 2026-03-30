import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { WalletService, Wallet, Transaction } from '../../../core/services/wallet.service';
import { AuthService } from '../../../core/services/auth.service';

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
  txLoading: boolean = false;
  walletError: string = '';
  userId: number = 0;

  constructor(
    private walletService: WalletService,
    private router: Router,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    if (userId) {
      this.userId = userId;
      this.loadWallet();
      this.loadStripeStatus();
      this.loadTransactions();
    } else {
      this.walletError = 'Utilisateur non connecté.';
    }
  }

  loadWallet(): void {
    this.loading = true;
    this.walletError = '';
    this.walletService.getWallet(this.userId).subscribe({
      next: (wallet) => {
        this.wallet = wallet;
        this.loading = false;
        console.log('✅ Wallet loaded:', wallet);
      },
      error: (err) => {
        console.error('❌ Error loading wallet:', err);
        this.loading = false;
        // Only show error — no static fallback to avoid misleading data
        if (err.status === 404) {
          this.walletError = 'Portefeuille non trouvé. Contactez l\'administrateur.';
        } else if (err.status === 401) {
          this.walletError = 'Session expirée. Veuillez vous reconnectez.';
        } else {
          this.walletError = `Erreur lors du chargement (${err.status || 'réseau'})`;
        }
      }
    });
  }

  loadStripeStatus(): void {
    this.walletService.getStripeStatus(this.userId).subscribe({
      next: (res) => {
        this.stripeStatus = res.status || res;
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
    this.txLoading = true;
    this.walletService.getTransactions(this.userId).subscribe({
      next: (txs) => {
        this.transactions = Array.isArray(txs) ? txs : [];
        this.txLoading = false;
      },
      error: (err) => {
        // /transactions endpoint may not exist yet on backend — silently ignore
        this.transactions = [];
        this.txLoading = false;
      }
    });
  }

  refresh(): void {
    this.loadWallet();
    this.loadTransactions();
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

  addTestFunds(): void {
    const amount = prompt('Combien souhaitez-vous ajouter ? (Simulation DEV)', '1000');
    if (amount && !isNaN(Number(amount)) && Number(amount) > 0) {
      this.walletService.credit(this.userId, Number(amount)).subscribe({
        next: (wallet) => {
          this.wallet = wallet;
          alert(`✅ ${amount} DT ajoutés avec succès ! Nouveau solde : ${wallet.balance} DT`);
        },
        error: (err) => {
          console.error(err);
          alert('❌ Erreur lors de l\'ajout de fonds : ' + (err.error?.message || err.message));
        }
      });
    }
  }
}