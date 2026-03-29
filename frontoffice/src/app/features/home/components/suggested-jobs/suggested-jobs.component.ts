import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JobPost, JobService } from '../../../../core/services/job.service';
import { SpinnerComponent } from '../../../../shared/components/spinner/spinner.component';

@Component({
  selector: 'app-suggested-jobs',
  standalone: true,
  imports: [CommonModule, SpinnerComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <article class="ds-card ds-card-pad widget">
      <h3 class="widget__title">Suggested Jobs</h3>

      <div class="widget__state" *ngIf="loading">
        <app-spinner />
      </div>

      <p class="widget__state ds-subtitle" *ngIf="error">{{ error }}</p>
      <p class="widget__state ds-subtitle" *ngIf="!loading && !error && jobs.length === 0">No suggestions right now.</p>

      <div class="widget__list" *ngIf="!loading && jobs.length > 0">
        <div class="widget__item" *ngFor="let job of jobs">
          <p class="widget__item-title ds-clamp-2">{{ job.title }}</p>
          <p class="widget__item-subtitle">{{ job.region || 'Remote' }}</p>
        </div>
      </div>
    </article>
  `,
  styles: [
    `
      .widget {
        display: grid;
        gap: 0.75rem;
      }

      .widget__title {
        margin: 0;
        font-family: 'Outfit', system-ui, sans-serif;
        font-size: 1rem;
      }

      .widget__state {
        text-align: center;
      }

      .widget__list {
        display: grid;
        gap: 0.6rem;
      }

      .widget__item {
        border: 1px solid var(--border);
        border-radius: 10px;
        padding: 0.55rem 0.65rem;
        background: rgba(255, 255, 255, 0.04);
      }

      .widget__item-title {
        margin: 0;
        font-weight: 600;
      }

      .widget__item-subtitle {
        margin: 0.2rem 0 0;
        color: var(--text-muted);
        font-size: 0.82rem;
      }
    `
  ]
})
export class SuggestedJobsComponent implements OnInit {
  jobs: JobPost[] = [];
  loading = false;
  error = '';

  constructor(
    private readonly jobsService: JobService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.cdr.markForCheck();

    this.jobsService.getSuggestedJobs(4).subscribe({
      next: (jobs) => {
        this.jobs = jobs;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
        this.error = 'Unable to load suggestions.';
        this.cdr.markForCheck();
      }
    });
  }
}
