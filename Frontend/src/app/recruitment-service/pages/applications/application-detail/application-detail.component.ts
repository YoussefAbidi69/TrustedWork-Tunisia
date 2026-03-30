import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationService } from '../../../services/application.service';
import { RecruitmentApplication } from '../../../models';

@Component({
    selector: 'app-application-detail',
    templateUrl: './application-detail.component.html',
    styleUrls: ['./application-detail.component.css']
})
export class ApplicationDetailComponent implements OnInit {
    application: RecruitmentApplication | null = null;
    loading = true;
    statusLoading = false;
    showStatusMenu = false;
    showRejectModal = false;
    motifRejet = '';
    pendingStatus = '';

    // Vrais statuts backend
    statusOptions = ['SUBMITTED', 'REVIEWED', 'SHORTLISTED', 'INTERVIEW', 'OFFERED', 'HIRED', 'REJECTED', 'PENDING'];

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private applicationService: ApplicationService
    ) { }

    ngOnInit(): void {
        const id = Number(this.route.snapshot.paramMap.get('id'));
        if (id) {
            this.applicationService.getById(id).subscribe({
                next: (data) => { this.application = data; this.loading = false; },
                error: () => { this.loading = false; }
            });
        }
    }

    // PATCH /applications/{id}/status
    requestChangeStatus(newStatus: string): void {
        this.showStatusMenu = false;
        if (!this.application || newStatus === this.application.status) return;
        if (newStatus === 'REJECTED') {
            // ouvre le modal pour saisir le motif
            this.pendingStatus = newStatus;
            this.showRejectModal = true;
        } else {
            this.doChangeStatus(newStatus, undefined);
        }
    }

    confirmReject(): void {
        this.showRejectModal = false;
        this.doChangeStatus(this.pendingStatus, this.motifRejet || undefined);
        this.motifRejet = '';
        this.pendingStatus = '';
    }

    cancelReject(): void {
        this.showRejectModal = false;
        this.motifRejet = '';
        this.pendingStatus = '';
    }

    private doChangeStatus(status: string, motifRejet?: string): void {
        this.statusLoading = true;
        this.applicationService.updateStatus(this.application!.id!, status, motifRejet).subscribe({
            next: (updated) => { this.application = updated; this.statusLoading = false; },
            error: () => { this.statusLoading = false; }
        });
    }

    toggleStatusMenu(): void { this.showStatusMenu = !this.showStatusMenu; }

    getScoreColor(score: number): string {
        if (score >= 75) return '#22d3ee';
        if (score >= 50) return '#fbbf24';
        return '#f87171';
    }

    goBack(): void { this.router.navigate(['/recruitment/applications']); }
    onEdit(): void { this.router.navigate(['/recruitment/applications', this.application!.id, 'edit']); }
    onDelete(): void {
        if (this.application && confirm('Supprimer cette candidature ?')) {
            this.applicationService.delete(this.application.id!).subscribe({ next: () => this.goBack() });
        }
    }
}