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

    goBack(): void { this.router.navigate(['/recruitment/applications']); }

    onEdit(): void {
        if (this.application) {
            this.router.navigate(['/recruitment/applications', this.application.id, 'edit']);
        }
    }

    onDelete(): void {
        if (this.application && confirm('Delete this application?')) {
            this.applicationService.delete(this.application.id!).subscribe({ next: () => this.goBack() });
        }
    }

    getScoreColor(score: number): string {
        if (score >= 75) return '#22d3ee';
        if (score >= 50) return '#fbbf24';
        return '#f87171';
    }
}