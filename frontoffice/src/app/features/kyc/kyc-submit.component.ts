import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { UserDTO, UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-kyc-submit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <section class="ds-page">
      <div class="ds-container">
        <div class="ds-card ds-card-pad" *ngIf="loading">
          <p class="ds-subtitle">Loading KYC status...</p>
        </div>

        <div class="ds-card ds-card-pad" *ngIf="!loading && user">
          <h1 class="ds-title">KYC Verification</h1>
          <p class="ds-subtitle">Submit your identification documents to unlock all account features.</p>

          <p style="margin-bottom: 1rem;">
            <strong>Current status:</strong>
            <span [style.color]="statusColor(user.kycStatus)">{{ user.kycStatus || 'UNKNOWN' }}</span>
          </p>

          <form [formGroup]="form" (ngSubmit)="submit()" *ngIf="canSubmit()">
            <label style="display:block; margin-bottom: .85rem;">
              <span style="color: var(--text-muted);">CIN number</span>
              <input class="ds-input" type="text" formControlName="cinNumber" placeholder="Enter CIN number" />
            </label>

            <label style="display:block; margin-bottom: .85rem;">
              <span style="color: var(--text-muted);">CIN document path</span>
              <input class="ds-input" type="text" formControlName="cinDocumentPath" placeholder="/uploads/cin.pdf" />
            </label>

            <label style="display:block; margin-bottom: .85rem;">
              <span style="color: var(--text-muted);">Diploma document path (optional)</span>
              <input class="ds-input" type="text" formControlName="diplomaDocumentPath" placeholder="/uploads/diploma.pdf" />
            </label>

            <p *ngIf="error" style="margin: 0 0 .85rem; color: var(--error);">{{ error }}</p>
            <p *ngIf="success" style="margin: 0 0 .85rem; color: var(--success);">{{ success }}</p>

            <button class="ds-btn ds-btn-primary" type="submit" [disabled]="submitting">
              {{ submitting ? 'Submitting...' : 'Submit KYC' }}
            </button>
          </form>

          <div *ngIf="!canSubmit()" style="padding: .75rem; border: 1px solid var(--border); border-radius: 12px;">
            <p style="margin: 0; color: var(--text-muted);">
              KYC cannot be resubmitted while your status is {{ user.kycStatus || 'UNKNOWN' }}.
            </p>
          </div>
        </div>

        <div class="ds-card ds-card-pad" *ngIf="!loading && !user">
          <h1 class="ds-title">KYC Unavailable</h1>
          <p class="ds-subtitle">Unable to load your profile and KYC status.</p>
        </div>
      </div>
    </section>
  `
})
export class KycSubmitComponent {
  user: UserDTO | null = null;
  loading = true;
  submitting = false;
  success = '';
  error = '';

  readonly form;

  constructor(
    private readonly fb: FormBuilder,
    private readonly users: UserService,
    private readonly auth: AuthService
  ) {
    this.form = this.fb.group({
      cinNumber: ['', [Validators.required, Validators.minLength(8)]],
      cinDocumentPath: ['', Validators.required],
      diplomaDocumentPath: ['']
    });

    this.loadStatus();
  }

  canSubmit(): boolean {
    const status = this.user?.kycStatus;
    return !status || status === 'PENDING' || status === 'REJECTED' || status === 'NOT_SUBMITTED';
  }

  statusColor(status: string): string {
    if (status === 'APPROVED') {
      return 'var(--success)';
    }
    if (status === 'REJECTED') {
      return 'var(--error)';
    }
    if (status === 'PENDING') {
      return 'var(--warning)';
    }
    return 'var(--text-muted)';
  }

  submit(): void {
    if (this.form.invalid || !this.user) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.success = '';
    this.error = '';

    const payload = {
      cinNumber: this.form.value.cinNumber ?? '',
      cinDocumentPath: this.form.value.cinDocumentPath ?? '',
      diplomaDocumentPath: this.form.value.diplomaDocumentPath ?? ''
    };

    this.users.submitKyc(this.user.id, payload).subscribe({
      next: () => {
        this.submitting = false;
        this.success = 'KYC submitted successfully. Your documents are under review.';
        this.loadStatus();
      },
      error: (err) => {
        this.submitting = false;
        this.error = err?.error?.message ?? err?.error?.error ?? 'KYC submission failed.';
      }
    });
  }

  private loadStatus(): void {
    const userId = this.auth.getUserId();
    if (userId != null) {
      this.users.getKycStatus(userId).subscribe({
        next: (data) => {
          this.user = data;
          this.loading = false;
        },
        error: () => {
          this.loadFromProfile();
        }
      });
      return;
    }

    this.loadFromProfile();
  }

  private loadFromProfile(): void {
    this.users.getMyProfile().subscribe({
      next: (data) => {
        this.user = data;
        this.loading = false;
      },
      error: () => {
        this.user = null;
        this.loading = false;
      }
    });
  }
}
