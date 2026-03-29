import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import { ApplicationService } from '../../core/services/application.service';
import { AuthService } from '../../core/services/auth.service';
import { JobPost, JobService } from '../../core/services/job.service';
import { SavedJobService } from '../../core/services/saved-job.service';
import { UserDTO, UserService } from '../../core/services/user.service';
import { SpinnerComponent } from '../../shared/components/spinner/spinner.component';

@Component({
  selector: 'app-job-details-side-panel',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SpinnerComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="job-panel__overlay" [class.job-panel__overlay--open]="open" (click)="close.emit()"></div>

    <aside class="job-panel" [class.job-panel--open]="open" aria-label="Job details side panel">
      <header class="job-panel__header">
        <h2 class="job-panel__title">Job Details</h2>
        <button type="button" class="job-panel__close" (click)="close.emit()" aria-label="Close details panel">x</button>
      </header>

      <div class="job-panel__body" *ngIf="open">
        <div class="job-panel__loading" *ngIf="loadingJob">
          <app-spinner />
          <p class="ds-subtitle">Loading job details...</p>
        </div>

        <div class="ds-card ds-card-pad" *ngIf="error && !loadingJob">
          <p class="ds-subtitle">{{ error }}</p>
        </div>

        <ng-container *ngIf="job && !loadingJob && !error">
          <article class="ds-card ds-card-pad job-panel__card">
            <div class="job-panel__meta-row">
              <span class="ds-badge ds-badge--published">{{ job.status || 'PUBLISHED' }}</span>
              <span class="ds-text-muted">{{ job.createdAt | date: 'medium' }}</span>
            </div>

            <h3 class="job-panel__job-title">{{ job.title }}</h3>
            <p class="job-panel__description">{{ job.description }}</p>

            <div class="job-panel__chips">
              <span class="ds-chip" *ngIf="job.type">{{ job.type }}</span>
              <span class="ds-chip" *ngIf="job.category">{{ job.category }}</span>
              <span class="ds-chip" *ngIf="job.budget">{{ job.budget | number: '1.0-0' }} TND</span>
            </div>

            <div class="job-panel__details-grid">
              <p><strong>Location:</strong> {{ job.region || 'Remote' }}</p>
              <p><strong>Deadline:</strong> {{ job.deadline ? (job.deadline | date: 'mediumDate') : 'N/A' }}</p>
              <p><strong>Required skills:</strong> {{ job.requiredSkills || 'Not specified' }}</p>
            </div>

            <div class="job-panel__actions">
              <button type="button" class="ds-btn ds-btn-outline" (click)="toggleSave()" [disabled]="saving || !currentUserId">
                {{ saved ? 'Unsave job' : 'Save job' }}
              </button>
            </div>

            <p class="job-panel__message" *ngIf="saveMessage">{{ saveMessage }}</p>
          </article>

          <article class="ds-card ds-card-pad job-panel__card" *ngIf="job.clientId as clientId">
            <h3 class="job-panel__section-title">Posted by</h3>

            <div class="job-panel__loading" *ngIf="loadingPoster">
              <app-spinner />
            </div>

            <ng-container *ngIf="poster && !loadingPoster">
              <div class="job-panel__poster">
                <span class="job-panel__poster-avatar">{{ posterInitials }}</span>
                <span>
                  <strong>{{ poster.firstName }} {{ poster.lastName }}</strong>
                  <small>{{ poster.bio || 'Add a bio in your profile' }}</small>
                </span>
              </div>
            </ng-container>

            <p *ngIf="!poster && !loadingPoster" class="ds-subtitle">User #{{ clientId }}</p>
          </article>

          <article class="ds-card ds-card-pad job-panel__card">
            <h3 class="job-panel__section-title">Apply to this job</h3>
            <form [formGroup]="applyForm" (ngSubmit)="applyToJob()">
              <label class="job-panel__field">
                <span>Cover letter</span>
                <textarea class="ds-textarea" rows="4" formControlName="coverLetter"></textarea>
              </label>

              <div class="job-panel__form-grid">
                <label class="job-panel__field">
                  <span>Proposed amount</span>
                  <input class="ds-input" type="number" formControlName="proposedAmount" min="1" />
                </label>
                <label class="job-panel__field">
                  <span>Proposed deadline</span>
                  <input class="ds-input" type="date" formControlName="proposedDeadline" />
                </label>
              </div>

              <p class="job-panel__message job-panel__message--error" *ngIf="applyError">{{ applyError }}</p>
              <p class="job-panel__message job-panel__message--ok" *ngIf="applySuccess">{{ applySuccess }}</p>

              <button type="submit" class="ds-btn ds-btn-primary" [disabled]="applying || !currentUserId">
                {{ applying ? 'Submitting...' : 'Apply now' }}
              </button>
            </form>
          </article>
        </ng-container>
      </div>
    </aside>
  `,
  styles: [
    `
      .job-panel__overlay {
        position: fixed;
        inset: 0;
        background: rgba(0, 0, 0, 0.45);
        opacity: 0;
        pointer-events: none;
        transition: opacity 0.25s ease;
        z-index: 90;
      }

      .job-panel__overlay--open {
        opacity: 1;
        pointer-events: auto;
      }

      .job-panel {
        position: fixed;
        right: 0;
        top: 0;
        bottom: 0;
        width: min(680px, 100vw);
        background: color-mix(in srgb, var(--bg) 92%, transparent);
        border-left: 1px solid var(--border);
        backdrop-filter: blur(18px);
        -webkit-backdrop-filter: blur(18px);
        transform: translateX(102%);
        transition: transform 0.3s ease;
        z-index: 100;
        display: flex;
        flex-direction: column;
      }

      .job-panel--open {
        transform: translateX(0);
      }

      .job-panel__header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0.9rem 1rem;
        border-bottom: 1px solid var(--border);
      }

      .job-panel__title {
        margin: 0;
        font-size: 1.25rem;
        font-family: 'Outfit', system-ui, sans-serif;
      }

      .job-panel__close {
        border: 1px solid var(--border);
        background: rgba(255, 255, 255, 0.04);
        color: var(--text);
        border-radius: 10px;
        width: 2rem;
        height: 2rem;
        cursor: pointer;
      }

      .job-panel__body {
        overflow-y: auto;
        padding: 0.9rem;
        display: grid;
        gap: 0.8rem;
      }

      .job-panel__loading {
        display: grid;
        place-items: center;
        gap: 0.6rem;
        text-align: center;
      }

      .job-panel__card {
        display: grid;
        gap: 0.8rem;
      }

      .job-panel__meta-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 0.6rem;
        flex-wrap: wrap;
      }

      .job-panel__job-title,
      .job-panel__section-title {
        margin: 0;
        font-family: 'Outfit', system-ui, sans-serif;
      }

      .job-panel__description {
        margin: 0;
        color: var(--text-muted);
      }

      .job-panel__chips {
        display: flex;
        flex-wrap: wrap;
        gap: 0.5rem;
      }

      .job-panel__details-grid {
        display: grid;
        gap: 0.4rem;
      }

      .job-panel__details-grid p {
        margin: 0;
        color: var(--text-muted);
      }

      .job-panel__actions {
        display: flex;
        gap: 0.6rem;
        flex-wrap: wrap;
      }

      .job-panel__poster {
        display: flex;
        align-items: center;
        gap: 0.7rem;
      }

      .job-panel__poster span {
        display: grid;
      }

      .job-panel__poster small {
        color: var(--text-muted);
      }

      .job-panel__poster-avatar {
        width: 2.4rem;
        height: 2.4rem;
        border-radius: 50%;
        display: grid;
        place-items: center;
        color: #00141a;
        font-weight: 700;
        background: linear-gradient(140deg, var(--accent), var(--accent-dim));
      }

      .job-panel__field {
        display: grid;
        gap: 0.35rem;
      }

      .job-panel__field span {
        color: var(--text-muted);
        font-size: 0.85rem;
      }

      .job-panel__form-grid {
        display: grid;
        grid-template-columns: repeat(2, minmax(0, 1fr));
        gap: 0.7rem;
      }

      .job-panel__message {
        margin: 0;
        font-size: 0.9rem;
        color: var(--text-muted);
      }

      .job-panel__message--error {
        color: var(--error);
      }

      .job-panel__message--ok {
        color: var(--success);
      }

      @media (max-width: 700px) {
        .job-panel {
          width: 100vw;
        }

        .job-panel__form-grid {
          grid-template-columns: 1fr;
        }
      }
    `
  ]
})
export class JobDetailsSidePanelComponent implements OnChanges {
  @Input() open = false;
  @Input() jobId: number | null = null;
  @Output() close = new EventEmitter<void>();
  @Output() savedStateChanged = new EventEmitter<{ jobId: number; saved: boolean }>();
  @Output() applicationSubmitted = new EventEmitter<number>();

  job: JobPost | null = null;
  poster: UserDTO | null = null;
  saved = false;

  loadingJob = false;
  loadingPoster = false;
  saving = false;
  applying = false;

  error = '';
  saveMessage = '';
  applyError = '';
  applySuccess = '';

  readonly applyForm;

  constructor(
    private readonly jobs: JobService,
    private readonly users: UserService,
    private readonly savedJobs: SavedJobService,
    private readonly applications: ApplicationService,
    private readonly auth: AuthService,
    private readonly fb: FormBuilder,
    private readonly cdr: ChangeDetectorRef
  ) {
    this.applyForm = this.fb.group({
      coverLetter: ['', [Validators.required, Validators.minLength(8)]],
      proposedAmount: [null as number | null, [Validators.required, Validators.min(1)]],
      proposedDeadline: ['', Validators.required]
    });
  }

  get currentUserId(): number | null {
    return this.auth.getUserId();
  }

  get posterInitials(): string {
    if (!this.poster) {
      return 'U';
    }
    return `${this.poster.firstName || ''}${this.poster.lastName || ''}`.trim().charAt(0).toUpperCase() || 'U';
  }

  ngOnChanges(changes: SimpleChanges): void {
    const currentJobId = this.jobId;
    const shouldLoad = (changes['open'] || changes['jobId']) && this.open && currentJobId != null;
    if (!shouldLoad || currentJobId == null) {
      return;
    }

    this.loadJob(currentJobId);
  }

  toggleSave(): void {
    if (!this.jobId || !this.currentUserId || this.saving) {
      return;
    }

    this.saving = true;
    this.saveMessage = '';
    this.cdr.markForCheck();

    if (this.saved) {
      this.savedJobs
        .unsaveJob(this.jobId, this.currentUserId)
        .pipe(
          finalize(() => {
            this.saving = false;
            this.cdr.markForCheck();
          })
        )
        .subscribe({
          next: () => {
            this.saved = false;
            this.saveMessage = 'Job removed from saved list.';
            this.savedStateChanged.emit({ jobId: this.jobId as number, saved: false });
            this.cdr.markForCheck();
          },
          error: () => {
            this.saveMessage = 'Unable to update saved state right now.';
            this.cdr.markForCheck();
          }
        });
      return;
    }

    this.savedJobs
      .saveJob(this.jobId, this.currentUserId)
      .pipe(
        finalize(() => {
          this.saving = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe({
        next: () => {
          this.saved = true;
          this.saveMessage = 'Job saved.';
          this.savedStateChanged.emit({ jobId: this.jobId as number, saved: true });
          this.cdr.markForCheck();
        },
        error: () => {
          this.saveMessage = 'Unable to update saved state right now.';
          this.cdr.markForCheck();
        }
      });
  }

  applyToJob(): void {
    this.applyError = '';
    this.applySuccess = '';
    this.cdr.markForCheck();

    if (!this.jobId || !this.currentUserId) {
      this.applyError = 'Sign in to apply to this job.';
      return;
    }

    if (this.applyForm.invalid) {
      this.applyForm.markAllAsTouched();
      this.applyError = 'Please complete all application fields.';
      return;
    }

    const payload = {
      jobPostId: this.jobId,
      freelancerId: this.currentUserId,
      coverLetter: this.applyForm.value.coverLetter ?? '',
      proposedAmount: Number(this.applyForm.value.proposedAmount ?? 0),
      proposedDeadline: this.applyForm.value.proposedDeadline ?? ''
    };

    this.applying = true;
    this.cdr.markForCheck();

    this.applications
      .apply(payload)
      .pipe(
        finalize(() => {
          this.applying = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe({
        next: () => {
          this.applySuccess = 'Application submitted successfully.';
          this.applicationSubmitted.emit(this.jobId as number);
          this.applyForm.reset();
          this.cdr.markForCheck();
        },
        error: (err) => {
          this.applyError = err?.error?.message ?? 'Unable to submit application right now.';
          this.cdr.markForCheck();
        }
      });
  }

  private loadJob(id: number): void {
    this.loadingJob = true;
    this.loadingPoster = false;
    this.error = '';
    this.applyError = '';
    this.applySuccess = '';
    this.saveMessage = '';
    this.job = null;
    this.poster = null;
    this.cdr.markForCheck();

    this.jobs
      .getJobById(id)
      .pipe(
        finalize(() => {
          this.loadingJob = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe({
        next: (job) => {
          this.job = job;
          this.refreshSavedState(id);
          if (job.clientId != null) {
            this.loadPoster(job.clientId);
          }
          this.cdr.markForCheck();
        },
        error: () => {
          this.error = 'Unable to load this job details.';
          this.cdr.markForCheck();
        }
      });
  }

  private refreshSavedState(jobId: number): void {
    if (!this.currentUserId) {
      this.saved = false;
      this.cdr.markForCheck();
      return;
    }

    this.savedJobs.getSavedJobIdSet(this.currentUserId).subscribe({
      next: (savedIds) => {
        this.saved = savedIds.has(jobId);
        this.cdr.markForCheck();
      },
      error: () => {
        this.saved = false;
        this.cdr.markForCheck();
      }
    });
  }

  private loadPoster(clientId: number): void {
    this.loadingPoster = true;
    this.cdr.markForCheck();
    this.users
      .getUserById(clientId)
      .pipe(
        finalize(() => {
          this.loadingPoster = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe({
        next: (user) => {
          this.poster = user;
          this.cdr.markForCheck();
        },
        error: () => {
          this.poster = null;
          this.cdr.markForCheck();
        }
      });
  }
}
