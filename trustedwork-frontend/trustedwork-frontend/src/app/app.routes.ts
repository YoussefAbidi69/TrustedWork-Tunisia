import { Routes } from '@angular/router';

export const routes: Routes = [
  // Redirection par défaut
  { path: '', redirectTo: '/contracts', pathMatch: 'full' },

  // ==================== CONTRATS ====================
  {
    path: 'contracts',
    loadComponent: () => import('./features/contracts/contract-list/contract-list').then(m => m.ContractListComponent)
  },
  {
    path: 'contracts/new',
    loadComponent: () => import('./features/contracts/contract-form/contract-form').then(m => m.ContractFormComponent)
  },
  {
    path: 'contracts/:id',
    loadComponent: () => import('./features/contracts/contract-detail/contract-detail').then(m => m.ContractDetailComponent)
  },
  {
    path: 'contracts/:id/edit',
    loadComponent: () => import('./features/contracts/contract-form/contract-form').then(m => m.ContractFormComponent)
  },

  // ==================== MILESTONES (JALONS) ====================
  {
    path: 'milestones',
    loadComponent: () => import('./features/milestones/milestone-list/milestone-list').then(m => m.MilestoneListComponent)
  },
  {
    path: 'milestones/:id',
    loadComponent: () => import('./features/milestones/milestone-form/milestone-form').then(m => m.MilestoneFormComponent)
  },
  {
    path: 'contracts/:contractId/milestones/new',
    loadComponent: () => import('./features/milestones/milestone-form/milestone-form').then(m => m.MilestoneFormComponent)
  },

  // ==================== PAIEMENTS (STRIPE) ====================
  {
    path: 'payment/list',
    loadComponent: () => import('./features/payment/payment-list/payment-list').then(m => m.PaymentListComponent)
  },
  {
    path: 'payment/checkout/:contractId',
    loadComponent: () => import('./features/payment/checkout/checkout').then(m => m.CheckoutComponent)
  },
  {
    path: 'payment/result',
    loadComponent: () => import('./features/payment/payment-result/payment-result').then(m => m.PaymentResultComponent)
  },

  // ==================== PORTEFEUILLE & STRIPE CONNECT ====================
  {
    path: 'wallet',
    loadComponent: () => import('./features/wallet/wallet-detail/wallet-detail').then(m => m.WalletDetailComponent)
  },
  {
    path: 'wallet/stripe-onboarding/:userId',
    loadComponent: () => import('./features/wallet/stripe-onboarding/stripe-onboarding').then(m => m.StripeOnboardingComponent)
  },

  // ==================== AUTHENTIFICATION ====================
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then(m => m.LoginComponent)
  },
  // {
  //   path: 'register',
  //   loadComponent: () => import('./features/auth/register/register').then(m => m.RegisterComponent)
  // },

  // ==================== UTILISATEURS ====================
  {
    path: 'users',
    loadComponent: () => import('./features/users/users-list/users-list').then(m => m.UsersListComponent)
  },

  // ==================== FALLBACK (404) ====================
  {
    path: '**',
    redirectTo: '/contracts'
  }
];