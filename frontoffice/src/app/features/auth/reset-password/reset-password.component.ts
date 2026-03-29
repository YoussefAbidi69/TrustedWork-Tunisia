import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <section class="auth-page">
      <div class="auth-card">
        <span class="auth-badge">Account recovery</span>
        <h1>Reset password</h1>
        <p class="subtitle">Use your token and set a new secure password.</p>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <div class="field">
            <label for="token">Token</label>
            <input id="token" type="text" formControlName="token" placeholder="Reset token" />
          </div>
          <div class="field">
            <label for="newPassword">New password</label>
            <input id="newPassword" type="password" formControlName="newPassword" placeholder="At least 8 characters" />
          </div>
          <p class="error-msg" *ngIf="error">{{ error }}</p>
          <p *ngIf="success" style="margin-bottom: .75rem; color: var(--success);">{{ success }}</p>
          <button type="submit" [disabled]="loading">{{ loading ? 'Resetting...' : 'Reset password' }}</button>
        </form>
        <p class="footer"><a routerLink="/auth/login">Back to sign in</a></p>
      </div>
    </section>
  `
})
export class ResetPasswordComponent {
  readonly form;
  loading = false;
  error = '';
  success = '';

  constructor(
    private readonly fb: FormBuilder,
    private readonly auth: AuthService,
    private readonly route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      token: [this.route.snapshot.queryParamMap.get('token') ?? '', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  onSubmit(): void {
    this.error = '';
    this.success = '';
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const token = this.form.value.token ?? '';
    const newPassword = this.form.value.newPassword ?? '';

    this.auth.resetPassword(token, newPassword).subscribe({
      next: (res) => {
        this.loading = false;
        this.success = res.message ?? 'Password reset completed.';
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Unable to reset password.';
      }
    });
  }
}
