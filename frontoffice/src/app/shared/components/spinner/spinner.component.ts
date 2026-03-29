import { Component } from '@angular/core';

@Component({
  selector: 'app-spinner',
  standalone: true,
  template: `<div class="spinner" aria-label="loading"></div>`,
  styles: [
    `
      .spinner {
        width: 24px;
        height: 24px;
        border-radius: 50%;
        border: 3px solid color-mix(in srgb, var(--accent) 30%, transparent);
        border-top-color: var(--accent);
        animation: spin 0.8s linear infinite;
      }
      @keyframes spin { to { transform: rotate(360deg); } }
    `
  ]
})
export class SpinnerComponent {}
