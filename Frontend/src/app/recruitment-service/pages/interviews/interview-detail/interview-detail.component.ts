import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { InterviewService } from '../../../services/interview.service';
import { InterviewSchedule } from '../../../models';

@Component({
  selector: 'app-interview-detail',
  templateUrl: './interview-detail.component.html',
  styleUrls: ['./interview-detail.component.css']
})
export class InterviewDetailComponent implements OnInit {
  interview: InterviewSchedule | null = null;
  loading = true;
  statusLoading = false;
  showStatusMenu = false;
  showFeedbackModal = false;
  feedbackText = '';
  feedbackNote: number = 5;

  // Vrais statuts backend
  statusOptions = ['PROPOSED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'RESCHEDULED'];

  typeIcons: Record<string, string> = {
    PHONE: '📞',
    VIDEO: '🎥',
    ONSITE: '🏢',
    TECHNICAL_TEST: '💻'
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private interviewService: InterviewService
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.interviewService.getById(id).subscribe({
        next: (data) => { this.interview = data; this.loading = false; },
        error: () => { this.loading = false; }
      });
    }
  }

  // PATCH /interviews/{id}/status
  changeStatus(newStatus: string): void {
    if (!this.interview || newStatus === this.interview.status) {
      this.showStatusMenu = false;
      return;
    }
    this.statusLoading = true;
    this.showStatusMenu = false;
    this.interviewService.updateStatus(this.interview.id!, newStatus).subscribe({
      next: (updated) => { this.interview = updated; this.statusLoading = false; },
      error: () => { this.statusLoading = false; }
    });
  }

  // PATCH /interviews/{id}/feedback
  submitFeedback(): void {
    if (!this.interview || !this.feedbackText.trim()) return;
    this.showFeedbackModal = false;
    this.interviewService.addFeedback(this.interview.id!, this.feedbackText, this.feedbackNote).subscribe({
      next: (updated) => {
        this.interview = updated;
        this.feedbackText = '';
        this.feedbackNote = 5;
      }
    });
  }

  cancelFeedback(): void {
    this.showFeedbackModal = false;
    this.feedbackText = '';
    this.feedbackNote = 5;
  }

  toggleStatusMenu(): void { this.showStatusMenu = !this.showStatusMenu; }
  getTypeIcon(type: string): string { return this.typeIcons[type] || '🗓️'; }

  getStars(note: number): string[] {
    return Array(10).fill('').map((_, i) => i < note ? '★' : '☆');
  }

  goBack(): void { this.router.navigate(['/recruitment/interviews']); }

  onEdit(): void {
    if (this.interview) {
      this.router.navigate(['/recruitment/interviews', this.interview.id, 'edit']);
    }
  }

  onDelete(): void {
    if (this.interview && confirm('Supprimer cet entretien ?')) {
      this.interviewService.delete(this.interview.id!).subscribe({ next: () => this.goBack() });
    }
  }
}