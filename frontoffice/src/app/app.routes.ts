import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: 'login', pathMatch: 'full', redirectTo: 'auth/login' },
  { path: 'register', pathMatch: 'full', redirectTo: 'auth/register' },
  {
    path: 'auth/login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'auth/register',
    loadComponent: () =>
      import('./features/auth/register/register.component').then((m) => m.RegisterComponent)
  },
  {
    path: 'auth/forgot-password',
    loadComponent: () =>
      import('./features/auth/forgot-password/forgot-password.component').then(
        (m) => m.ForgotPasswordComponent
      )
  },
  {
    path: 'auth/reset-password',
    loadComponent: () =>
      import('./features/auth/reset-password/reset-password.component').then(
        (m) => m.ResetPasswordComponent
      )
  },
  {
    path: 'home',
    loadComponent: () =>
      import('./features/home/home.component').then((m) => m.HomeComponent)
  },
  
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent)
  },
  {
    path: 'jobs',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/jobs/jobs.component').then((m) => m.JobsComponent)
  },
  {
    path: 'jobs/saved',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/jobs/saved-jobs.component').then((m) => m.SavedJobsComponent)
  },
  {
    path: 'jobs/applications',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/applications/applications.component').then((m) => m.ApplicationsComponent)
  },
  {
    path: 'applications',
    pathMatch: 'full',
    redirectTo: 'jobs/applications'
  },
  {
    path: 'profile',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/profile/profile.component').then((m) => m.ProfileComponent)
  },
  {
    path: 'profile/:id',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/profile/profile.component').then((m) => m.ProfileComponent)
  },
  {
    path: 'kyc',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/kyc/kyc-submit.component').then((m) => m.KycSubmitComponent)
  },
  {
    path: 'settings',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/settings/settings.component').then((m) => m.SettingsComponent)
  },
  { path: '**', redirectTo: 'home' }
];
