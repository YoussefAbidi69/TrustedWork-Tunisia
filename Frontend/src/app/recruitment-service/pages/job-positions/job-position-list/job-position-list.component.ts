import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobPositionService } from '../../../services/job-position.service';
import { JobPosition } from '../../../models';
import { TableColumn } from '../../../shared/recruitment-table/recruitment-table.component';

@Component({
    selector: 'app-job-position-list',
    templateUrl: './job-position-list.component.html',
    styleUrls: ['./job-position-list.component.css']
})
export class JobPositionListComponent implements OnInit {
    jobPositions: JobPosition[] = [];
    loading = true;

    columns: TableColumn[] = [
        { key: 'titre', label: 'Title' },
        { key: 'typeContrat', label: 'Contract' },
        { key: 'localisation', label: 'Location' },
        { key: 'salaireMin', label: 'Min Salary', type: 'currency' },
        { key: 'salaireMax', label: 'Max Salary', type: 'currency' },
        { key: 'deadline', label: 'Deadline', type: 'date' },
        { key: 'status', label: 'Status', type: 'badge' }
    ];

    constructor(
        private jobPositionService: JobPositionService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadData();
    }

    loadData(): void {
        this.loading = true;
        this.jobPositionService.getAll().subscribe({
            next: (data) => {
                this.jobPositions = data;
                this.loading = false;
            },
            error: () => {
                this.loading = false;
            }
        });
    }

    onCreate(): void {
        this.router.navigate(['/recruitment/job-positions/create']);
    }




    onView(row: JobPosition): void {
        this.router.navigate(['/recruitment/job-positions', row.id]);
    }


}
