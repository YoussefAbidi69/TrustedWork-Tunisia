import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JobPositionService } from '../../../services/job-position.service';
import { JobPosition } from '../../../models';

@Component({
    selector: 'app-job-position-detail',
    templateUrl: './job-position-detail.component.html',
    styleUrls: ['./job-position-detail.component.css']
})
export class JobPositionDetailComponent implements OnInit {
    jobPosition: JobPosition | null = null;
    loading = true;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private jobPositionService: JobPositionService
    ) { }

    ngOnInit(): void {
        const id = Number(this.route.snapshot.paramMap.get('id'));
        if (id) {
            this.jobPositionService.getById(id).subscribe({
                next: (data) => {
                    this.jobPosition = data;
                    this.loading = false;
                },
                error: () => {
                    this.loading = false;
                }
            });
        }
    }

    goBack(): void {
        this.router.navigate(['/recruitment/job-positions']);
    }

    onDelete(): void {
        if (this.jobPosition && confirm('Delete this job position?')) {
            this.jobPositionService.delete(this.jobPosition.id!).subscribe({
                next: () => this.goBack()
            });
        }
    }
}
