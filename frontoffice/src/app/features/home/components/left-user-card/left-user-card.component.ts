import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { UserDTO, UserService } from '../../../../core/services/user.service';
import { UserCardComponent } from '../user-card/user-card.component';
import { SpinnerComponent } from '../../../../shared/components/spinner/spinner.component';

@Component({
  selector: 'app-left-user-card',
  standalone: true,
  imports: [CommonModule, RouterLink, UserCardComponent, SpinnerComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="left-user">
      <div class="left-user__sticky">
        <div class="ds-card ds-card-pad" *ngIf="loading">
          <app-spinner />
          <p class="left-user__state">Loading your profile...</p>
        </div>

        <div class="ds-card ds-card-pad" *ngIf="error && !loading">
          <p class="left-user__state">{{ error }}</p>
          <button type="button" class="ds-btn ds-btn-outline" (click)="loadProfile()">Retry</button>
        </div>

        <ng-container *ngIf="user && !loading && !error">
          <a routerLink="/profile" class="left-user__link" aria-label="Open profile details">
            <app-user-card
              [firstName]="user.firstName"
              [lastName]="user.lastName"
              [email]="user.email"
              [bio]="user.bio"
            />
          </a>

          <div class="ds-card ds-card-pad left-user__actions">
            <a routerLink="/profile">View profile</a>
            <a routerLink="/dashboard">Go to dashboard</a>
          </div>
        </ng-container>
      </div>
    </div>
  `,
  styles: [
    `
      .left-user {
        min-width: 0;
      }

      .left-user__sticky {
        position: sticky;
        top: calc(var(--header-h) + 1rem);
        display: grid;
        gap: 0.8rem;
      }

      .left-user__link {
        display: block;
        text-decoration: none;
      }

      .left-user__link:hover {
        text-decoration: none;
      }

      .left-user__actions {
        display: grid;
        gap: 0.55rem;
      }

      .left-user__actions a {
        color: var(--text-muted);
        text-decoration: none;
        font-weight: 600;
      }

      .left-user__actions a:hover {
        color: var(--accent);
        text-decoration: underline;
      }

      .left-user__state {
        margin: 0.65rem 0 0;
        color: var(--text-muted);
      }

      @media (max-width: 1024px) {
        .left-user__sticky {
          position: static;
        }
      }
    `
  ]
})
export class LeftUserCardComponent implements OnInit {
  user: UserDTO | null = null;
  loading = false;
  error = '';

  constructor(
    private readonly users: UserService,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.loading = true;
    this.error = '';
    this.cdr.markForCheck();

    this.users.getMyProfile().subscribe({
      next: (profile) => {
        this.user = profile;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.user = null;
        this.loading = false;
        this.error = 'Unable to load your profile card right now.';
        this.cdr.markForCheck();
      }
    });
  }
}
