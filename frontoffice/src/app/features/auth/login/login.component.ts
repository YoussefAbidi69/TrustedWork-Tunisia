import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  errorMessage = '';
  loading = false;
  failedAttempts = 0;

  readonly form;

  constructor(
    private readonly fb: FormBuilder,
    private readonly auth: AuthService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      rememberMe: [false]
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.auth
      .login({
        email: this.form.value.email ?? '',
        password: this.form.value.password ?? '',
        rememberMe: !!this.form.value.rememberMe
      })
      .subscribe({
        next: (response) => {
          if (response.twoFactorRequired) {
            this.loading = false;
            this.errorMessage = response.message ?? 'Two-factor code required for this account.';
            return;
          }
          this.loading = false;
          const returnUrl = this.route.snapshot.queryParams['returnUrl'] ?? '/dashboard';
          this.router.navigateByUrl(returnUrl);
        },
        error: (err) => {
          this.loading = false;
          this.failedAttempts++;
          this.errorMessage = err?.error?.message ?? 'Invalid email or password.';
        }
      });
  }
}
