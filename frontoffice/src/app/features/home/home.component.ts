import { ChangeDetectionStrategy, ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { JobPost, JobService } from '../../core/services/job.service';
import { LeftUserCardComponent } from './components/left-user-card/left-user-card.component';
import { FeedComponent } from './components/feed/feed.component';
import { RightWidgetsComponent } from './components/right-widgets/right-widgets.component';
import { JobCardComponent } from './components/job-card/job-card.component';
import { SpinnerComponent } from '../../shared/components/spinner/spinner.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    LeftUserCardComponent,
    FeedComponent,
    RightWidgetsComponent,
    JobCardComponent,
    SpinnerComponent
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  previewItems: JobPost[] = [];
  previewLoading = false;
  previewError = '';

  constructor(
    public readonly auth: AuthService,
    private readonly jobs: JobService,
    private readonly cdr: ChangeDetectorRef
  ) {
    if (!this.auth.isLoggedIn()) {
      this.loadGuestPreview();
    }
  }

  trackById(_: number, item: JobPost): number {
    return item.id;
  }

  loadGuestPreview(): void {
    this.previewLoading = true;
    this.previewError = '';
    this.cdr.markForCheck();

    this.jobs.getJobs(0, 4).subscribe({
      next: (items) => {
        this.previewItems = items;
        this.previewLoading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.previewItems = [];
        this.previewLoading = false;
        this.previewError = 'Unable to load preview content right now.';
        this.cdr.markForCheck();
      }
    });
  }
}
