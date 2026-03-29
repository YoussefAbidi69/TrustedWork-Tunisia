import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  ViewChild
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { JobPost, JobService } from '../../core/services/job.service';
import { SavedJobService } from '../../core/services/saved-job.service';
import { JobCardComponent } from '../home/components/job-card/job-card.component';
import { SpinnerComponent } from '../../shared/components/spinner/spinner.component';
import { JobDetailsSidePanelComponent } from './job-details-side-panel.component';

@Component({
  selector: 'app-jobs',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    JobCardComponent,
    SpinnerComponent,
    JobDetailsSidePanelComponent
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <section class="ds-page">
      <div class="ds-container">
        <div class="ds-card ds-card-pad jobs-head">
          <h1 class="ds-title">Jobs</h1>
          <p class="ds-subtitle">Browse published jobs, save opportunities, and apply with your profile.</p>

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

          <div class="ds-card ds-card-pad jobs-state" *ngIf="loading && jobs.length === 0">
            <app-spinner />
            <p class="ds-subtitle">Loading jobs...</p>
          </div>

          <div class="ds-card ds-card-pad jobs-state" *ngIf="error && jobs.length === 0">
            <p class="ds-subtitle">{{ error }}</p>
            <button type="button" class="ds-btn ds-btn-outline" (click)="reload()">Retry</button>
          </div>

          <div class="ds-card ds-card-pad jobs-state" *ngIf="!loading && !error && jobs.length === 0">
            <p class="ds-subtitle">No job posts available yet.</p>
          </div>

          <app-home-job-card
            *ngFor="let job of jobs; trackBy: trackByJobId"
            [job]="job"
            [saved]="savedJobIds.has(job.id)"
            (saveToggled)="toggleSave($event)"
            (applyClicked)="openDetails($event)"
            (detailsClicked)="openDetails($event)"
          />

          <div class="jobs-state" *ngIf="loading && jobs.length > 0">
            <app-spinner />
          </div>

          <div class="ds-card ds-card-pad jobs-state" *ngIf="done && jobs.length > 0 && !loading">
            <p class="ds-subtitle">You have reached the end of available jobs.</p>
          </div>

          <div #sentinel class="jobs-sentinel" aria-hidden="true"></div>

          <button class="ds-btn ds-btn-outline jobs-load-more" type="button" (click)="loadMore()" *ngIf="!done && !loading">
            Load more jobs
          </button>
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

      .jobs-load-more {
        justify-self: center;
      }

      .jobs-sentinel {
        height: 2px;
      }
    `
  ]
})
export class JobsComponent implements AfterViewInit, OnDestroy {
  @ViewChild('sentinel', { static: true }) sentinelRef!: ElementRef<HTMLDivElement>;

  jobs: JobPost[] = [];
  page = 0;
  readonly pageSize = 8;
  loading = false;
  done = false;
  error = '';

  savedJobIds = new Set<number>();
  detailsOpen = false;
  selectedJobId: number | null = null;
  actionError = '';

  private observer?: IntersectionObserver;

  constructor(
    private readonly jobsService: JobService,
    private readonly savedJobs: SavedJobService,
    private readonly auth: AuthService,
    private readonly cdr: ChangeDetectorRef
  ) {
    this.loadSavedJobs();
    this.loadMore();
  }

  ngAfterViewInit(): void {
    this.observer = new IntersectionObserver(
      (entries) => {
        if (entries.some((entry) => entry.isIntersecting)) {
          this.loadMore();
        }
      },
      { rootMargin: '260px' }
    );

    this.observer.observe(this.sentinelRef.nativeElement);
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }

  trackByJobId(_: number, job: JobPost): number {
    return job.id;
  }

  reload(): void {
    this.jobs = [];
    this.page = 0;
    this.done = false;
    this.error = '';
    this.cdr.markForCheck();
    this.loadMore();
  }

  loadMore(): void {
    if (this.loading || this.done) {
      return;
    }

    this.loading = true;
    this.error = '';
    this.cdr.markForCheck();

    this.jobsService.getJobs(this.page, this.pageSize).subscribe({
      next: (nextJobs) => {
        this.loading = false;
        if (nextJobs.length === 0) {
          this.done = true;
          this.cdr.markForCheck();
          return;
        }
        this.jobs = [...this.jobs, ...nextJobs];
        if (nextJobs.length < this.pageSize) {
          this.done = true;
        }
        this.page += 1;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
        this.error = 'Unable to load jobs right now.';
        this.cdr.markForCheck();
      }
    });
  }

  openDetails(job: JobPost): void {
    this.selectedJobId = job.id;
    this.detailsOpen = true;
    this.cdr.markForCheck();
  }

  closeDetails(): void {
    this.detailsOpen = false;
    this.cdr.markForCheck();
  }

  toggleSave(job: JobPost): void {
    const userId = this.auth.getUserId();
    this.actionError = '';

    if (!userId) {
      this.openDetails(job);
      return;
    }

    if (this.savedJobIds.has(job.id)) {
      this.savedJobs.unsaveJob(job.id, userId).subscribe({
        next: () => {
          this.onSavedStateChanged({ jobId: job.id, saved: false });
        },
        error: () => {
          this.actionError = 'Unable to remove this saved job right now.';
          this.cdr.markForCheck();
        }
      });
      return;
    }

    this.savedJobs.saveJob(job.id, userId).subscribe({
      next: () => {
        this.onSavedStateChanged({ jobId: job.id, saved: true });
      },
      error: () => {
        this.actionError = 'Unable to save this job right now.';
        this.cdr.markForCheck();
      }
    });
  }

  onSavedStateChanged(event: { jobId: number; saved: boolean }): void {
    if (event.saved) {
      this.savedJobIds.add(event.jobId);
      this.cdr.markForCheck();
      return;
    }
    this.savedJobIds.delete(event.jobId);
    this.cdr.markForCheck();
  }

  private loadSavedJobs(): void {
    const userId = this.auth.getUserId();
    if (!userId) {
      return;
    }

    this.savedJobs.getSavedJobIdSet(userId).subscribe({
      next: (savedIds) => {
        this.savedJobIds = savedIds;
        this.cdr.markForCheck();
      },
      error: () => {
        // Saved-job failures must not block jobs route rendering.
        this.savedJobIds = new Set<number>();
        this.cdr.markForCheck();
      }
    });
  }
}
