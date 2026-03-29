import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JobPost } from '../../../../core/services/job.service';

@Component({
  selector: 'app-home-job-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <article class="ds-card ds-card-pad feed-card">
      <header class="feed-card__header">
        <span class="ds-badge ds-badge--published">Job</span>
        <span class="feed-card__date">{{ job.createdAt | date: 'mediumDate' }}</span>
      </header>

      <h3 class="feed-card__title">{{ job.title }}</h3>
      <p class="feed-card__poster" *ngIf="job.clientId">
        Posted by User #{{ job.clientId }}
      </p>
      <p class="feed-card__desc ds-clamp-3">{{ job.description }}</p>

      <div class="feed-card__chips">
        <span class="ds-chip" *ngIf="job.type">{{ job.type }}</span>
        <span class="ds-chip" *ngIf="job.category">{{ job.category }}</span>
        <span class="ds-chip" *ngIf="job.budget">{{ job.budget | number: '1.0-0' }} TND</span>
        <span class="ds-chip ds-chip--solid" *ngIf="job.status">{{ job.status }}</span>
      </div>

      <footer class="feed-card__meta">
        <span>{{ job.region || 'Remote' }}</span>
        <span *ngIf="job.deadline">Deadline: {{ job.deadline | date: 'MMM d' }}</span>
      </footer>

      <p class="feed-card__skills" *ngIf="job.requiredSkills">
        Skills: {{ job.requiredSkills }}
      </p>

      <div class="feed-card__actions" *ngIf="interactive">
        <button type="button" class="ds-btn ds-btn-outline" (click)="saveToggled.emit(job)">
          {{ saved ? 'Saved' : 'Save' }}
        </button>
        <button type="button" class="ds-btn ds-btn-outline" (click)="applyClicked.emit(job)">Apply</button>
        <button type="button" class="ds-btn ds-btn-primary" (click)="detailsClicked.emit(job)">View details</button>
      </div>
    </article>
  `,
  styles: [
    `
      .feed-card {
        display: grid;
        gap: 0.85rem;
      }

      .feed-card__header,
      .feed-card__meta {
        display: flex;
        align-items: center;
        justify-content: space-between;
        gap: 0.65rem;
        flex-wrap: wrap;
      }

      .feed-card__date,
      .feed-card__meta {
        color: var(--text-muted);
        font-size: 0.82rem;
      }

      .feed-card__title {
        margin: 0;
        font-family: 'Outfit', system-ui, sans-serif;
        font-size: 1.12rem;
      }

      .feed-card__poster {
        width: fit-content;
        color: var(--accent);
        font-size: 0.87rem;
        margin: 0;
      }

      .feed-card__desc {
        margin: 0;
        color: var(--text-muted);
      }

      .feed-card__chips {
        display: flex;
        gap: 0.45rem;
        flex-wrap: wrap;
      }

      .feed-card__skills {
        margin: 0;
        color: var(--text-muted);
        font-size: 0.85rem;
      }

      .feed-card__actions {
        display: flex;
        gap: 0.5rem;
        flex-wrap: wrap;
      }
    `
  ]
})
export class JobCardComponent {
  @Input({ required: true }) job!: JobPost;
  @Input() interactive = true;
  @Input() saved = false;

  @Output() saveToggled = new EventEmitter<JobPost>();
  @Output() applyClicked = new EventEmitter<JobPost>();
  @Output() detailsClicked = new EventEmitter<JobPost>();
}
