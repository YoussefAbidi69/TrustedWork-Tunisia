import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="ds-page">
      <div class="ds-container">
        <div class="ds-card ds-card-pad">
          <h1 class="ds-title">Dashboard</h1>
          <p class="ds-subtitle">Unified metrics and quick actions for upcoming modules.</p>

          <div style="display: flex; gap: .75rem; flex-wrap: wrap; margin-top: 1rem;">
            <a class="ds-btn ds-btn-outline" routerLink="/profile">User profile</a>
            <a class="ds-btn ds-btn-outline" routerLink="/settings">Settings</a>
            <a class="ds-btn" routerLink="/kyc">KYC verification</a>
          </div>
        </div>
      </div>
    </section>
  `
})
export class DashboardComponent {}
