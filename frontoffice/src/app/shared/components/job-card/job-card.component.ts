import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-job-card',
  standalone: true,
  template: `
    <article class="ds-card ds-card-pad feed-card">
      <span class="ds-badge ds-badge--published">Job</span>
      <h3 class="title">{{ title }}</h3>
      <p class="ds-text-muted ds-clamp-2">{{ subtitle }}</p>
      <div class="meta">{{ location }}</div>
    </article>
  `,
  styles: [
    `
      .feed-card { display: grid; gap: 0.7rem; }
      .title { margin: 0; font-size: 1.1rem; }
      .meta { color: var(--text-muted); font-size: 0.9rem; }
    `
  ]
})
export class JobCardComponent {
  @Input({ required: true }) title = '';
  @Input({ required: true }) subtitle = '';
  @Input({ required: true }) location = '';
}
