import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import {
  passwordStrength,
  strongPasswordValidator,
  PASSWORD_MIN_LENGTH
} from '../../../shared/validators/password.validators';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  phase: 1 | 2 = 1;
  errorMessage = '';
  loading = false;

  readonly form;

  get strength(): 0 | 1 | 2 | 3 | 4 {
    return passwordStrength(this.form.get('password')?.value ?? '');
  }
  get hasLower(): boolean {
    return /[a-z]/.test(this.form.get('password')?.value ?? '');
  }
  get hasUpper(): boolean {
    return /[A-Z]/.test(this.form.get('password')?.value ?? '');
  }
  get hasDigit(): boolean {
    return /\d/.test(this.form.get('password')?.value ?? '');
  }
  get hasSpecial(): boolean {
    return /[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(this.form.get('password')?.value ?? '');
  }

  constructor(
    private readonly fb: FormBuilder,
    private readonly auth: AuthService,
    private readonly router: Router
  ) {
    this.form = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9+\s()\-]{8,20}$/)]],
      email: ['', [Validators.required, Validators.email]],
      role: ['CLIENT', Validators.required],
      password: [
        '',
        [Validators.required, Validators.minLength(PASSWORD_MIN_LENGTH), strongPasswordValidator()]
      ]
    });
  }

  goToPhase2(): void {
    this.errorMessage = '';
    const firstName = this.form.get('firstName');
    const lastName = this.form.get('lastName');
    const phoneNumber = this.form.get('phoneNumber');
    firstName?.markAsTouched();
    lastName?.markAsTouched();
    phoneNumber?.markAsTouched();
    if (firstName?.invalid || lastName?.invalid || phoneNumber?.invalid) return;
    this.phase = 2;
  }

  backToPhase1(): void {
    this.phase = 1;
    this.errorMessage = '';
  }

  onSubmit(): void {
    this.errorMessage = '';
    if (this.phase === 1) {
      this.goToPhase2();
      return;
    }
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.auth
      .register({
        firstName: this.form.value.firstName ?? '',
        lastName: this.form.value.lastName ?? '',
        phoneNumber: this.form.value.phoneNumber ?? '',
        email: this.form.value.email ?? '',
        password: this.form.value.password ?? '',
        role: (this.form.value.role === 'FREELANCER' ? 'FREELANCER' : 'CLIENT')
      })
      .subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          this.loading = false;
          this.errorMessage = err?.error?.message ?? 'Registration failed.';
        }
      });
  }
}
