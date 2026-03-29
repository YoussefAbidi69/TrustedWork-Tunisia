import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { JobPost } from '../../core/services/job.service';
import { SavedJobService } from '../../core/services/saved-job.service';
import { JobCardComponent } from '../home/components/job-card/job-card.component';
import { JobDetailsSidePanelComponent } from './job-details-side-panel.component';
import { SpinnerComponent } from '../../shared/components/spinner/spinner.component';

@Component({
  selector: 'app-saved-jobs',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    JobCardComponent,
    SpinnerComponent,
    JobDetailsSidePanelComponent
  ],
  template: `
    <section class="ds-page">
      <div class="ds-container">
        <div class="ds-card ds-card-pad jobs-head">
          <h1 class="ds-title">Saved Jobs</h1>
          <p class="ds-subtitle">Your curated list of job opportunities.</p>

          <nav class="jobs-head__tabs" aria-label="Jobs navigation tabs">
            <a routerLink="/jobs" [routerLinkActiveOptions]="{ exact: true }" routerLinkActive="is-active">All jobs</a>
            <a routerLink="/jobs/saved" routerLinkActive="is-active">Saved jobs</a>
            <a routerLink="/jobs/applications" routerLinkActive="is-active">My applications</a>
          </nav>
        </div>

        <div class="jobs-feed">
          <div class="ds-card ds-card-pad jobs-state" *ngIf="actionError">
            <p class="ds-subtitle">{{ actionError }}</p>
          </div>

          <div class="ds-card ds-card-pad jobs-state" *ngIf="loading">
            <app-spinner />
            <p class="ds-subtitle">Loading saved jobs...</p>
          </div>

          <div class="ds-card ds-card-pad jobs-state" *ngIf="error && !loading">
            <p class="ds-subtitle">{{ error }}</p>
            <button type="button" class="ds-btn ds-btn-outline" (click)="loadSavedJobs()">Retry</button>
          </div>

          <div class="ds-card ds-card-pad jobs-state" *ngIf="!loading && !error && jobs.length === 0">
            <p class="ds-subtitle">You have not saved any jobs yet.</p>
            <a class="ds-btn ds-btn-outline" routerLink="/jobs">Browse jobs</a>
          </div>

          <app-home-job-card
            *ngFor="let job of jobs; trackBy: trackByJobId"
            [job]="job"
            [saved]="true"
            (saveToggled)="removeSaved(job)"
            (applyClicked)="openDetails(job)"
            (detailsClicked)="openDetails(job)"
          />
        </div>
      </div>

      <app-job-details-side-panel
        [open]="detailsOpen"
        [jobId]="selectedJobId"
        (close)="closeDetails()"
        (savedStateChanged)="onSavedStateChanged($event)"
      />
    </section>
  `,
  styles: [
    `
      .jobs-head {
        display: grid;
        gap: 0.9rem;
        margin-bottom: 0.95rem;
      }

      .jobs-head__tabs {
        display: flex;
        gap: 0.5rem;
        flex-wrap: wrap;
      }

      .jobs-head__tabs a {
        padding: 0.42rem 0.8rem;
        border-radius: 999px;
        text-decoration: none;
        color: var(--text-muted);
        border: 1px solid transparent;
      }

      .jobs-head__tabs a.is-active,
      .jobs-head__tabs a:hover {
        border-color: rgba(34, 211, 238, 0.3);
        background: rgba(34, 211, 238, 0.12);
        color: var(--accent);
      }

      .jobs-feed {
        display: grid;
        gap: 0.85rem;
      }

      .jobs-state {
        text-align: center;
        display: grid;
        justify-items: center;
        gap: 0.6rem;
      }
    `
  ]
})
export class SavedJobsComponent {
  jobs: JobPost[] = [];
  loading = false;
  error = '';

  detailsOpen = false;
  selectedJobId: number | null = null;
  actionError = '';

  constructor(
    private readonly savedJobs: SavedJobService,
    private readonly auth: AuthService
  ) {
    this.loadSavedJobs();
  }

  trackByJobId(_: number, job: JobPost): number {
    return job.id;
  }

  loadSavedJobs(): void {
    const userId = this.auth.getUserId();
    if (!userId) {
      this.jobs = [];
      this.error = 'Sign in to view saved jobs.';
      return;
    }

    this.loading = true;
    this.error = '';

    this.savedJobs.getSavedJobs(userId).subscribe({
      next: (jobs) => {
        this.jobs = jobs;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.error = 'Unable to load saved jobs right now.';
      }
    });
  }

  removeSaved(job: JobPost): void {
    const userId = this.auth.getUserId();
    if (!userId) {
      return;
    }

    this.actionError = '';

    this.savedJobs.unsaveJob(job.id, userId).subscribe({
      next: () => {
        this.jobs = this.jobs.filter((item) => item.id !== job.id);
      },
      error: () => {
        this.actionError = 'Unable to remove this saved job right now.';
      }
    });
  }

  openDetails(job: JobPost): void {
    this.selectedJobId = job.id;
    this.detailsOpen = true;
  }

  closeDetails(): void {
    this.detailsOpen = false;
  }

  onSavedStateChanged(event: { jobId: number; saved: boolean }): void {
    if (!event.saved) {
      this.jobs = this.jobs.filter((job) => job.id !== event.jobId);
    }
  }
}
