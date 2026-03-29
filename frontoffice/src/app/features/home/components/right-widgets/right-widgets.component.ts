import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SuggestedJobsComponent } from '../suggested-jobs/suggested-jobs.component';

@Component({
  selector: 'app-right-widgets',
  standalone: true,
  imports: [CommonModule, RouterLink, SuggestedJobsComponent],
  template: `
    <aside class="widgets" aria-label="Home side widgets">
      <app-suggested-jobs />

      <article class="ds-card ds-card-pad widgets__card">
        <h3 class="widgets__title">Quick Shortcuts</h3>
        <a routerLink="/jobs/applications">My applications</a>
        <a routerLink="/jobs/saved">Saved jobs</a>
        <a routerLink="/settings">Account settings</a>
        <a routerLink="/kyc">KYC progress</a>
      </article>

      <article class="ds-card ds-card-pad widgets__card">
        <h3 class="widgets__title">Announcements</h3>
        <p class="ds-subtitle">New profile completion indicator is now available in your account settings.</p>
      </article>

      <article class="ds-card ds-card-pad widgets__card">
        <h3 class="widgets__title">Tips</h3>
        <p class="ds-subtitle">Profiles with a short bio and verified KYC are viewed more often by recruiters.</p>
      </article>
    </aside>
  `,
  styles: [
    `
      .widgets {
        display: grid;
        gap: 0.8rem;
      }

      .widgets__card {
        display: grid;
        gap: 0.5rem;
      }

      .widgets__title {
        margin: 0;
        font-family: 'Outfit', system-ui, sans-serif;
        font-size: 1rem;
      }

      .widgets__card a {
        color: var(--text-muted);
        text-decoration: none;
        font-weight: 600;
      }

      .widgets__card a:hover {
        color: var(--accent);
        text-decoration: underline;
      }
    `
  ]
})
export class RightWidgetsComponent {}
