import { CommonModule } from '@angular/common';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-jobs-api-test',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <section class="ds-page">
      <div class="ds-container">
        <div class="ds-card ds-card-pad test-card">
          <h1 class="ds-title">Jobs API Test</h1>
          <p class="ds-subtitle">
            Calls <strong>{{ endpoint }}</strong> and prints raw response.
          </p>

          <div class="actions">
            <button type="button" class="ds-btn ds-btn-primary" (click)="callApi()" [disabled]="loading">
              {{ loading ? 'Calling...' : 'Call Jobs API' }}
            </button>
          </div>

          <div class="status" *ngIf="loading">Loading...</div>

          <div class="status" *ngIf="!loading && success">
            <span class="ds-chip ds-chip--solid">Success</span>
            <span>HTTP {{ statusCode }}</span>
            <span>Items: {{ itemCount }}</span>
          </div>

          <div class="status error" *ngIf="!loading && error">
            <span class="ds-chip ds-badge--rejected">Error</span>
            <span>HTTP {{ statusCode ?? 'n/a' }}</span>
          </div>

          <div class="panel" *ngIf="errorMessage">
            <h3>Error message</h3>
            <pre>{{ errorMessage }}</pre>
          </div>

          <div class="panel" *ngIf="responseBody !== null">
            <h3>Raw response</h3>
            <pre>{{ responseBody | json }}</pre>
          </div>
        </div>
      </div>
    </section>
  `,
  styles: [
    `
      .test-card {
        display: grid;
        gap: 1rem;
      }

      .actions {
        display: flex;
        gap: 0.5rem;
      }

      .status {
        display: flex;
        gap: 0.75rem;
        align-items: center;
        color: var(--text-muted);
      }

      .status.error {
        color: var(--error);
      }

      .panel {
        border: 1px solid var(--border);
        border-radius: 12px;
        padding: 0.75rem;
        background: rgba(255, 255, 255, 0.03);
      }

      .panel h3 {
        margin: 0 0 0.5rem;
        font-size: 0.95rem;
      }

      .panel pre {
        margin: 0;
        white-space: pre-wrap;
        word-break: break-word;
        font-size: 0.82rem;
        color: var(--text-muted);
      }
    `
  ]
})
export class JobsApiTestComponent {
  readonly endpoint = `${environment.api.jobBaseUrl}/jobs`;

  loading = false;
  success = false;
  error = false;
  statusCode: number | null = null;
  responseBody: unknown = null;
  errorMessage = '';

  constructor(
    private readonly http: HttpClient,
    private readonly cdr: ChangeDetectorRef
  ) {}

  get itemCount(): number {
    return Array.isArray(this.responseBody) ? this.responseBody.length : 0;
  }

  callApi(): void {
    this.loading = true;
    this.success = false;
    this.error = false;
    this.statusCode = null;
    this.errorMessage = '';
    this.responseBody = null;
    this.cdr.markForCheck();

    this.http.get<unknown>(this.endpoint, { observe: 'response' }).subscribe({
      next: (response: HttpResponse<unknown>) => {
        this.loading = false;
        this.success = true;
        this.statusCode = response.status;
        this.responseBody = response.body;
        this.cdr.markForCheck();
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        this.error = true;
        this.statusCode = err.status || null;
        this.responseBody = err.error ?? null;
        this.errorMessage = err.message;
        this.cdr.markForCheck();
      }
    });
  }
}
