import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { UserDTO, UserService } from '../../core/services/user.service';
import { ThemeService } from '../../core/services/theme.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <section class="ds-page">
      <div class="ds-container" style="display: grid; gap: 1rem;">
        <div class="ds-card ds-card-pad" *ngIf="loading">
          <p class="ds-subtitle">Loading account settings...</p>
        </div>

        <ng-container *ngIf="!loading && user">
          <div class="ds-card ds-card-pad">
            <h1 class="ds-title">Settings</h1>
            <p class="ds-subtitle">
              Manage account preferences, security and verification from one place.
            </p>
            <div style="display: flex; gap: .75rem; flex-wrap: wrap; margin-top: .75rem;">
              <a class="ds-btn ds-btn-outline" routerLink="/dashboard">Dashboard</a>
              <a class="ds-btn ds-btn-outline" routerLink="/profile">Profile</a>
              <a class="ds-btn ds-btn-outline" routerLink="/kyc">KYC</a>
            </div>
          </div>

          <div class="ds-card ds-card-pad">
            <h2 class="ds-title" style="font-size: 1.15rem;">Account Information</h2>
            <p class="ds-subtitle" style="margin-bottom: .9rem;">
              Update your personal details used across TrustedWork.
            </p>

            <form [formGroup]="profileForm" (ngSubmit)="saveProfile()">
              <div class="ds-grid-2" style="gap: 1rem;">
                <label>
                  <span style="color: var(--text-muted);">First name</span>
                  <input class="ds-input" type="text" formControlName="firstName" />
                </label>
                <label>
                  <span style="color: var(--text-muted);">Last name</span>
                  <input class="ds-input" type="text" formControlName="lastName" />
                </label>
              </div>
              <label style="display: block; margin-top: .9rem;">
                <span style="color: var(--text-muted);">Phone number</span>
                <input class="ds-input" type="text" formControlName="phoneNumber" />
              </label>
              <label style="display: block; margin-top: .9rem;">
                <span style="color: var(--text-muted);">Bio</span>
                <textarea
                  class="ds-textarea"
                  formControlName="bio"
                  rows="3"
                  placeholder="Tell people what you do"
                ></textarea>
              </label>

              <p *ngIf="profileError" style="margin-top: .75rem; color: var(--error);">{{ profileError }}</p>
              <p *ngIf="profileSuccess" style="margin-top: .75rem; color: var(--success);">{{ profileSuccess }}</p>
              <button class="ds-btn ds-btn-primary" type="submit" style="margin-top: .8rem;">Save changes</button>
            </form>
          </div>

          <div class="ds-card ds-card-pad">
            <h2 class="ds-title" style="font-size: 1.15rem;">Security</h2>
            <p class="ds-subtitle" style="margin-bottom: .9rem;">Change password and monitor security state.</p>

            <div style="display: grid; gap: .25rem; margin-bottom: .9rem; color: var(--text-muted);">
              <p><strong>Email:</strong> {{ user.email }}</p>
              <p><strong>2FA:</strong> {{ user.twoFactorEnabled ? 'Enabled' : 'Disabled' }}</p>
              <p><strong>Bio:</strong> {{ user.bio || 'Add a bio in your profile' }}</p>
              <p><strong>Role:</strong> {{ user.role || '-' }}</p>
              <p><strong>Account status:</strong> {{ user.accountStatus || '-' }}</p>
            </div>

            <form [formGroup]="passwordForm" (ngSubmit)="updatePassword()">
              <div class="ds-grid-2" style="gap: 1rem;">
                <label>
                  <span style="color: var(--text-muted);">Current password</span>
                  <input class="ds-input" type="password" formControlName="currentPassword" />
                </label>
                <label>
                  <span style="color: var(--text-muted);">New password</span>
                  <input class="ds-input" type="password" formControlName="newPassword" />
                </label>
              </div>

              <p *ngIf="passwordError" style="margin-top: .75rem; color: var(--error);">{{ passwordError }}</p>
              <p *ngIf="passwordSuccess" style="margin-top: .75rem; color: var(--success);">{{ passwordSuccess }}</p>
              <button class="ds-btn ds-btn-primary" type="submit" [disabled]="passwordLoading" style="margin-top: .8rem;">
                {{ passwordLoading ? 'Updating...' : 'Update password' }}
              </button>
            </form>
          </div>

          <div class="ds-card ds-card-pad" style="display: grid; gap: .65rem;">
            <h2 class="ds-title" style="font-size: 1.15rem;">Experience</h2>
            <p class="ds-subtitle">Choose your preferred visual mode.</p>
            <button
              class="ds-btn ds-btn-outline"
              type="button"
              (click)="theme.toggle()"
              [attr.aria-label]="theme.isDarkMode ? 'Switch to light mode' : 'Switch to dark mode'"
              style="width: fit-content;"
            >
              {{ theme.isDarkMode ? 'Switch to light mode' : 'Switch to dark mode' }}
            </button>
          </div>
        </ng-container>

        <div class="ds-card ds-card-pad" *ngIf="!loading && !user">
          <h1 class="ds-title">Settings unavailable</h1>
          <p class="ds-subtitle">Unable to load your account settings right now.</p>
        </div>
      </div>
    </section>
  `
})
export class SettingsComponent {
  user: UserDTO | null = null;
  loading = true;

  profileSuccess = '';
  profileError = '';
  passwordSuccess = '';
  passwordError = '';
  passwordLoading = false;

  readonly profileForm;
  readonly passwordForm;

  constructor(
    public readonly auth: AuthService,
    public readonly theme: ThemeService,
    private readonly users: UserService,
    private readonly fb: FormBuilder
  ) {
    this.profileForm = this.fb.group({
      firstName: ['', [Validators.minLength(2)]],
      lastName: ['', [Validators.minLength(2)]],
      phoneNumber: [''],
      bio: ['']
    });

    this.passwordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(8)]]
    });

    this.loadProfile();
  }

  saveProfile(): void {
    if (!this.user) {
      return;
    }

    this.profileSuccess = '';
    this.profileError = '';

    const payload = {
      firstName: this.profileForm.value.firstName ?? undefined,
      lastName: this.profileForm.value.lastName ?? undefined,
      phoneNumber: this.profileForm.value.phoneNumber ?? undefined,
      bio: this.profileForm.value.bio ?? undefined
    };

    this.users.updateProfile(this.user.id, payload).subscribe({
      next: (updated) => {
        this.user = updated;
        this.profileSuccess = 'Profile updated successfully.';
      },
      error: (err) => {
        this.profileError = err?.error?.message ?? 'Failed to update profile.';
      }
    });
  }

  updatePassword(): void {
    this.passwordSuccess = '';
    this.passwordError = '';

    if (this.passwordForm.invalid) {
      this.passwordForm.markAllAsTouched();
      this.passwordError = 'Both password fields are required.';
      return;
    }

    this.passwordLoading = true;

    const currentPassword = this.passwordForm.value.currentPassword ?? '';
    const newPassword = this.passwordForm.value.newPassword ?? '';

    this.auth.changePassword(currentPassword, newPassword).subscribe({
      next: (res) => {
        this.passwordLoading = false;
        this.passwordSuccess = res.message ?? 'Password updated successfully.';
        this.passwordForm.reset();
      },
      error: (err) => {
        this.passwordLoading = false;
        this.passwordError = err?.error?.message ?? 'Failed to update password.';
      }
    });
  }

  private loadProfile(): void {
    this.loading = true;
    this.users.getMyProfile().subscribe({
      next: (profile) => {
        this.user = profile;
        this.profileForm.patchValue({
          firstName: profile.firstName ?? '',
          lastName: profile.lastName ?? '',
          phoneNumber: profile.phoneNumber ?? '',
          bio: profile.bio ?? ''
        });
        this.loading = false;
      },
      error: () => {
        this.user = null;
        this.loading = false;
      }
    });
  }
}
