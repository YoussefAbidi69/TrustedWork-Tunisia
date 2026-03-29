import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <article class="ds-card ds-card-pad user-card">
      <div class="user-card__head">
        <div class="user-card__avatar" [attr.aria-label]="fullName || 'User avatar'">{{ initials }}</div>
        <div>
          <h3 class="user-card__name">{{ fullName || 'TrustedWork User' }}</h3>
          <p class="user-card__email">{{ email || 'No email available' }}</p>
        </div>
      </div>

      <p class="user-card__bio">{{ bio || 'Add a bio in your profile' }}</p>
    </article>
  `,
  styles: [
    `
      .user-card {
        display: grid;
        gap: 0.85rem;
        transition: border-color 0.2s ease, background 0.2s ease, transform 0.2s ease;
      }

      .user-card:hover {
        border-color: rgba(34, 211, 238, 0.3);
        background: var(--surface-hover);
        transform: translateY(-1px);
      }

      .user-card__head {
        display: flex;
        gap: 0.75rem;
        align-items: center;
      }

      .user-card__avatar {
        width: 2.8rem;
        height: 2.8rem;
        border-radius: 50%;
        display: grid;
        place-items: center;
        font-family: 'Outfit', system-ui, sans-serif;
        font-weight: 700;
        color: #00141a;
        background: linear-gradient(145deg, var(--accent), var(--accent-dim));
        box-shadow: 0 10px 24px -12px rgba(34, 211, 238, 0.55);
      }

      .user-card__name {
        margin: 0;
        font-size: 1rem;
        font-family: 'Outfit', system-ui, sans-serif;
        letter-spacing: -0.02em;
      }

      .user-card__email {
        margin: 0;
        color: var(--text-muted);
        font-size: 0.86rem;
        word-break: break-word;
      }

      .user-card__bio {
        margin: 0;
        color: var(--text-muted);
        font-size: 0.9rem;
        line-height: 1.45;
      }
    `
  ]
})
export class UserCardComponent {
  @Input() firstName = '';
  @Input() lastName = '';
  @Input() email = '';
  @Input() bio: string | null | undefined = '';

  get fullName(): string {
    const name = `${this.firstName || ''} ${this.lastName || ''}`.trim();
    return name;
  }

  get initials(): string {
    const source = this.fullName || this.email || 'U';
    return source.charAt(0).toUpperCase();
  }
}
