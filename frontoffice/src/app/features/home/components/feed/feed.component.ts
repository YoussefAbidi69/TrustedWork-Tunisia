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
import { AuthService } from '../../../../core/services/auth.service';
import { JobPost, JobService } from '../../../../core/services/job.service';
import { SavedJobService } from '../../../../core/services/saved-job.service';
import { JobCardComponent } from '../job-card/job-card.component';
import { SpinnerComponent } from '../../../../shared/components/spinner/spinner.component';
import { JobDetailsSidePanelComponent } from '../../../jobs/job-details-side-panel.component';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, JobCardComponent, SpinnerComponent, JobDetailsSidePanelComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <section class="feed" aria-label="Home feed">
      <div class="feed__state ds-card ds-card-pad" *ngIf="jobs.length === 0 && loading">
        <app-spinner />
        <p class="ds-subtitle">Loading your personalized feed...</p>
      </div>

      <div class="feed__state ds-card ds-card-pad" *ngIf="error && jobs.length === 0">
        <p class="ds-subtitle">{{ error }}</p>
        <button type="button" class="ds-btn ds-btn-outline" (click)="reload()">Retry loading</button>
      </div>

      <div class="feed__state ds-card ds-card-pad" *ngIf="!loading && !error && jobs.length === 0">
        <h3 class="ds-title feed__title">No posts yet</h3>
        <p class="ds-subtitle">Your feed is empty right now. Check back in a few minutes.</p>
      </div>

      <div class="feed__state ds-card ds-card-pad" *ngIf="actionError">
        <p class="ds-subtitle">{{ actionError }}</p>
      </div>

      <ng-container *ngFor="let item of jobs; trackBy: trackById">
        <app-home-job-card
          [job]="item"
          [saved]="savedJobIds.has(item.id)"
          (saveToggled)="toggleSave($event)"
          (applyClicked)="openDetails($event)"
          (detailsClicked)="openDetails($event)"
        />
      </ng-container>

      <div class="feed__state" *ngIf="loading && jobs.length > 0">
        <app-spinner />
      </div>

      <div class="feed__state" *ngIf="error && jobs.length > 0">
        <p class="ds-subtitle">{{ error }}</p>
        <button type="button" class="ds-btn ds-btn-outline" (click)="loadMore()">Try again</button>
      </div>

      <div class="feed__state ds-card ds-card-pad" *ngIf="done && jobs.length > 0 && !loading">
        <p class="ds-subtitle">You have reached the end of the jobs feed.</p>
      </div>

      <div #sentinel class="feed__sentinel" aria-hidden="true"></div>

      <button
        class="ds-btn ds-btn-outline feed__load-more"
        type="button"
        (click)="loadMore()"
        *ngIf="!done && !loading"
      >
        Load more
      </button>

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
      .feed {
        display: grid;
        gap: 0.85rem;
        min-width: 0;
      }

      .feed__state {
        display: grid;
        justify-items: center;
        gap: 0.7rem;
        text-align: center;
      }

      .feed__title {
        font-size: 1.2rem;
      }

      .feed__sentinel {
        height: 2px;
      }

      .feed__load-more {
        justify-self: center;
      }
    `
  ]
})
export class FeedComponent implements AfterViewInit, OnDestroy {
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

  trackById(_: number, item: JobPost): number {
    return item.id;
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
      next: (pageJobs) => {
        this.loading = false;
        if (pageJobs.length === 0) {
          this.done = true;
          this.cdr.markForCheck();
          return;
        }

        this.jobs = [...this.jobs, ...pageJobs];
        if (pageJobs.length < this.pageSize) {
          this.done = true;
        }
        this.page += 1;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
        this.error = 'Failed to load more jobs.';
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
        // Saved-job failures must not block jobs feed rendering.
        this.savedJobIds = new Set<number>();
        this.cdr.markForCheck();
      }
    });
  }
}
