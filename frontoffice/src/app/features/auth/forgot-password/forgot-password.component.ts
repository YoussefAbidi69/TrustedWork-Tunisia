import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <section class="auth-page">
      <div class="auth-card">
        <span class="auth-badge">Account recovery</span>
        <h1>Forgot password</h1>
        <p class="subtitle">Enter your email to generate a reset password token.</p>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <div class="field">
            <label for="email">Email</label>
            <input id="email" type="email" formControlName="email" placeholder="you@example.com" autocomplete="email" />
          </div>
          <p class="error-msg" *ngIf="error">{{ error }}</p>
          <p *ngIf="success" style="margin-bottom: .75rem; color: var(--success);">{{ success }}</p>
          <p *ngIf="token" style="margin-bottom: .75rem; color: var(--text-muted); word-break: break-all;">
            Reset token: {{ token }}
          </p>
          <button type="submit" [disabled]="loading">{{ loading ? 'Sending...' : 'Send request' }}</button>
        </form>
        <p class="footer"><a routerLink="/auth/login">Back to sign in</a></p>
      </div>
    </section>
  `
})
export class ForgotPasswordComponent {
  readonly form;
  loading = false;
  error = '';
  success = '';
  token = '';

  constructor(private readonly fb: FormBuilder, private readonly auth: AuthService) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    this.error = '';
    this.success = '';
    this.token = '';
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    const email = this.form.value.email ?? '';

    this.auth.forgotPassword(email).subscribe({
      next: (res) => {
        this.loading = false;
        this.success = res.message ?? 'Reset request sent successfully.';
        this.token = res.token ?? '';
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Unable to send reset request.';
      }
    });
  }
}
