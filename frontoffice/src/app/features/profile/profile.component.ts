import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { UserDTO, UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <section class="ds-page">
      <div class="ds-container">
        <div class="ds-card ds-card-pad" *ngIf="loading">
          <p class="ds-subtitle">Loading profile...</p>
        </div>

        <div class="ds-card ds-card-pad" *ngIf="!loading && user">
          <h1 class="ds-title">{{ isOwnProfile ? 'Profile' : 'User Profile' }}</h1>
          <p class="ds-subtitle">Signed in as {{ user.email || auth.getEmail() || 'user' }}</p>

          <div style="margin-top: 1rem; gap: 1rem; display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));">
            <div>
              <p><strong>First name:</strong> {{ user.firstName || '-' }}</p>
              <p><strong>Last name:</strong> {{ user.lastName || '-' }}</p>
              <p><strong>Phone:</strong> {{ user.phoneNumber || '-' }}</p>
              <p><strong>Bio:</strong> {{ user.bio || 'Add a bio in your profile' }}</p>
              <p><strong>Role:</strong> {{ user.role || '-' }}</p>
            </div>
            <div>
              <p><strong>Account status:</strong> {{ user.accountStatus || '-' }}</p>
              <p><strong>KYC status:</strong> {{ user.kycStatus || '-' }}</p>
              <p><strong>2FA:</strong> {{ user.twoFactorEnabled ? 'Enabled' : 'Disabled' }}</p>
            </div>
          </div>

          <div style="display: flex; gap: .75rem; flex-wrap: wrap; margin: 1rem 0 1.25rem;" *ngIf="isOwnProfile">
            <button class="ds-btn ds-btn-outline" type="button" (click)="toggleEdit()">
              {{ editing ? 'Cancel edit' : 'Edit profile' }}
            </button>
            <a class="ds-btn" routerLink="/kyc">Manage KYC</a>
            <a class="ds-btn ds-btn-outline" routerLink="/settings">Account settings</a>
          </div>

          <p class="ds-subtitle" *ngIf="!isOwnProfile">Public profile view from job poster linking.</p>

          <form *ngIf="editing" [formGroup]="editForm" (ngSubmit)="saveProfile()" style="margin-bottom: 1.25rem;">
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
            <label style="display:block; margin-top: 1rem;">
              <span style="color: var(--text-muted);">Phone number</span>
              <input class="ds-input" type="text" formControlName="phoneNumber" />
            </label>
            <label style="display:block; margin-top: 1rem;">
              <span style="color: var(--text-muted);">Bio</span>
              <textarea class="ds-textarea" formControlName="bio" rows="3" placeholder="Tell people what you do"></textarea>
            </label>
            <p *ngIf="saveError" style="margin-top: .75rem; color: var(--error);">{{ saveError }}</p>
            <p *ngIf="saveSuccess" style="margin-top: .75rem; color: var(--success);">{{ saveSuccess }}</p>
            <button class="ds-btn ds-btn-primary" type="submit" style="margin-top: .75rem;">Save profile</button>
          </form>

          <hr style="border-color: var(--border); margin: 1.25rem 0;" *ngIf="isOwnProfile" />
          <h2 class="ds-title" style="font-size: 1.1rem; margin-bottom: .75rem;" *ngIf="isOwnProfile">Change password</h2>
          <form [formGroup]="passwordForm" (ngSubmit)="changePassword()" *ngIf="isOwnProfile">
            <div style="gap: 1rem; display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));">
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
            <button class="ds-btn ds-btn-primary" type="submit" [disabled]="passwordLoading" style="margin-top: .75rem;">
              {{ passwordLoading ? 'Updating...' : 'Update password' }}
            </button>
          </form>
        </div>

        <div class="ds-card ds-card-pad" *ngIf="!loading && !user">
          <h1 class="ds-title">Profile unavailable</h1>
          <p class="ds-subtitle">Unable to load your profile right now.</p>
        </div>
      </div>
    </section>
  `
})
export class ProfileComponent {
  user: UserDTO | null = null;
  isOwnProfile = true;
  viewedUserId: number | null = null;
  loading = true;
  editing = false;
  saveSuccess = '';
  saveError = '';
  passwordSuccess = '';
  passwordError = '';
  passwordLoading = false;

  readonly editForm;
  readonly passwordForm;

  constructor(
    public readonly auth: AuthService,
    private readonly users: UserService,
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute
  ) {
    this.editForm = this.fb.group({
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

  toggleEdit(): void {
    this.editing = !this.editing;
    this.saveSuccess = '';
    this.saveError = '';
  }

  saveProfile(): void {
    if (!this.user) {
      return;
    }

    this.saveSuccess = '';
    this.saveError = '';

    const payload = {
      firstName: this.editForm.value.firstName ?? undefined,
      lastName: this.editForm.value.lastName ?? undefined,
      phoneNumber: this.editForm.value.phoneNumber ?? undefined,
      bio: this.editForm.value.bio ?? undefined
    };

    this.users.updateProfile(this.user.id, payload).subscribe({
      next: (updated) => {
        this.user = updated;
        this.editing = false;
        this.saveSuccess = 'Profile updated successfully.';
      },
      error: (err) => {
        this.saveError = err?.error?.message ?? 'Failed to update profile.';
      }
    });
  }

  changePassword(): void {
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

    const idParam = this.route.snapshot.paramMap.get('id');
    const parsedId = idParam ? Number(idParam) : null;
    const hasValidIdParam = parsedId != null && Number.isFinite(parsedId) && parsedId > 0;

    this.viewedUserId = hasValidIdParam ? parsedId : null;
    this.isOwnProfile = !hasValidIdParam || parsedId === this.auth.getUserId();

    const source$ = this.viewedUserId ? this.users.getUserById(this.viewedUserId) : this.users.getMyProfile();

    source$.subscribe({
      next: (profile) => {
        this.user = profile;
        this.editForm.patchValue({
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
