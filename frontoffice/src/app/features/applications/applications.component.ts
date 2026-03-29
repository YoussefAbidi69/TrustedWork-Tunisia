import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { ApplicationWithJob, ApplicationService } from '../../core/services/application.service';
import { AuthService } from '../../core/services/auth.service';
import { SpinnerComponent } from '../../shared/components/spinner/spinner.component';

@Component({
  selector: 'app-applications',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, SpinnerComponent],
  template: `
    <section class="ds-page">
      <div class="ds-container">
        <div class="ds-card ds-card-pad apps-head">
          <h1 class="ds-title">My Applications</h1>
          <p class="ds-subtitle">Track statuses for all jobs you applied to.</p>

          <nav class="apps-head__tabs" aria-label="Jobs navigation tabs">
            <a routerLink="/jobs" [routerLinkActiveOptions]="{ exact: true }" routerLinkActive="is-active">All jobs</a>
            <a routerLink="/jobs/saved" routerLinkActive="is-active">Saved jobs</a>
            <a routerLink="/jobs/applications" routerLinkActive="is-active">My applications</a>
          </nav>
        </div>

        <div class="apps-list">
          <div class="ds-card ds-card-pad apps-state" *ngIf="actionError">
            <p class="ds-subtitle">{{ actionError }}</p>
          </div>

          <div class="ds-card ds-card-pad apps-state" *ngIf="loading">
            <app-spinner />
            <p class="ds-subtitle">Loading your applications...</p>
          </div>

          <div class="ds-card ds-card-pad apps-state" *ngIf="error && !loading">
            <p class="ds-subtitle">{{ error }}</p>
            <button type="button" class="ds-btn ds-btn-outline" (click)="loadApplications()">Retry</button>
          </div>

          <div class="ds-card ds-card-pad apps-state" *ngIf="!loading && !error && applications.length === 0">
            <p class="ds-subtitle">No applications found yet.</p>
            <a class="ds-btn ds-btn-outline" routerLink="/jobs">Browse jobs</a>
          </div>

          <article class="ds-card ds-card-pad app-card" *ngFor="let item of applications; trackBy: trackByAppId">
            <div class="app-card__head">
              <h3 class="app-card__title">{{ item.job?.title || ('Job #' + item.application.jobPostId) }}</h3>
              <span class="ds-badge ds-badge--pending">{{ item.application.status || 'PENDING' }}</span>
            </div>

            <p class="ds-subtitle">{{ item.job?.region || 'Remote' }} • Applied {{ item.application.createdAt | date: 'mediumDate' }}</p>
            <p class="app-card__text ds-clamp-3">{{ item.application.coverLetter }}</p>

            <div class="app-card__meta">
              <span>Proposed amount: {{ item.application.proposedAmount | number: '1.0-0' }} TND</span>
              <span>Deadline: {{ item.application.proposedDeadline | date: 'mediumDate' }}</span>
            </div>

            <div class="app-card__actions">
              <a class="ds-btn ds-btn-outline" [routerLink]="['/jobs']">View jobs</a>
              <button
                type="button"
                class="ds-btn ds-btn-danger"
                (click)="withdraw(item.application.id)"
                [disabled]="withdrawingIds.has(item.application.id)"
              >
                {{ withdrawingIds.has(item.application.id) ? 'Withdrawing...' : 'Withdraw' }}
              </button>
            </div>
          </article>
        </div>
      </div>
    </section>
  `,
  styles: [
    `
      .apps-head {
        display: grid;
        gap: 0.9rem;
        margin-bottom: 0.95rem;
      }

      .apps-head__tabs {
        display: flex;
        gap: 0.5rem;
        flex-wrap: wrap;
      }

      .apps-head__tabs a {
        padding: 0.42rem 0.8rem;
        border-radius: 999px;
        text-decoration: none;
        color: var(--text-muted);
        border: 1px solid transparent;
      }

      .apps-head__tabs a.is-active,
      .apps-head__tabs a:hover {
        border-color: rgba(34, 211, 238, 0.3);
        background: rgba(34, 211, 238, 0.12);
        color: var(--accent);
      }

      .apps-list {
        display: grid;
        gap: 0.85rem;
      }

      .apps-state {
        text-align: center;
        display: grid;
        justify-items: center;
        gap: 0.6rem;
      }

      .app-card {
        display: grid;
        gap: 0.7rem;
      }

      .app-card__head {
        display: flex;
        justify-content: space-between;
        gap: 0.6rem;
        align-items: center;
        flex-wrap: wrap;
      }

      .app-card__title {
        margin: 0;
        font-family: 'Outfit', system-ui, sans-serif;
      }

      .app-card__text {
        margin: 0;
      }

      .app-card__meta {
        display: flex;
        flex-wrap: wrap;
        gap: 0.7rem;
        color: var(--text-muted);
      }

      .app-card__actions {
        display: flex;
        gap: 0.5rem;
        flex-wrap: wrap;
      }
    `
  ]
})
export class ApplicationsComponent {
  applications: ApplicationWithJob[] = [];
  loading = false;
  error = '';
  actionError = '';
  withdrawingIds = new Set<number>();

  constructor(
    private readonly applicationsService: ApplicationService,
    private readonly auth: AuthService
  ) {
    this.loadApplications();
  }

  trackByAppId(_: number, item: ApplicationWithJob): number {
    return item.application.id;
  }

  loadApplications(): void {
    const userId = this.auth.getUserId();
    if (!userId) {
      this.error = 'Sign in to view your applications.';
      this.applications = [];
      return;
    }

    this.loading = true;
    this.error = '';

    this.applicationsService.getMyApplicationsWithJobs(userId).subscribe({
      next: (items) => {
        this.applications = items;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.error = 'Unable to load your applications.';
      }
    });
  }

  withdraw(applicationId: number): void {
    if (this.withdrawingIds.has(applicationId)) {
      return;
    }

    this.actionError = '';
    this.withdrawingIds.add(applicationId);

    this.applicationsService.withdraw(applicationId).subscribe({
      next: () => {
        this.applications = this.applications.filter((item) => item.application.id !== applicationId);
        this.withdrawingIds.delete(applicationId);
      },
      error: () => {
        this.actionError = 'Unable to withdraw this application right now.';
        this.withdrawingIds.delete(applicationId);
      }
    });
  }
}
