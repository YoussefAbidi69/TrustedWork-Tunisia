import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApplicationService } from '../../../services/application.service';
import { RecruitmentApplication } from '../../../models';
import { TableColumn } from '../../../shared/recruitment-table/recruitment-table.component';

@Component({
    selector: 'app-application-list',
    templateUrl: './application-list.component.html',
    styleUrls: ['./application-list.component.css']
})
export class ApplicationListComponent implements OnInit {
    applications: RecruitmentApplication[] = [];
    filtered: RecruitmentApplication[] = [];
    loading = true;
    searchTerm = '';
    selectedStatus = '';

    statusOptions = ['', 'PENDING', 'SHORTLISTED', 'IN_PROGRESS', 'ACCEPTED', 'REJECTED'];

    columns: TableColumn[] = [
        { key: 'id', label: 'ID' },
        { key: 'jobPositionId', label: 'Job ID' },
        { key: 'freelancerId', label: 'Freelancer ID' },
        { key: 'pretentionSalariale', label: 'Salary', type: 'currency' },
        { key: 'disponibilite', label: 'Availability' },
        { key: 'matchingScore', label: 'Score %' },
        { key: 'status', label: 'Status', type: 'badge' }
    ];

    constructor(
        private applicationService: ApplicationService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.loadData();
    }

    loadData(): void {
        this.loading = true;
        this.applicationService.getAll().subscribe({
            next: (data) => {
                this.applications = data;
                this.applyFilter();
                this.loading = false;
            },
            error: () => { this.loading = false; }
        });
    }

    applyFilter(): void {
        this.filtered = this.applications.filter(a => {
            const matchSearch = !this.searchTerm
                || a.disponibilite?.toLowerCase().includes(this.searchTerm.toLowerCase())
                || String(a.jobPositionId).includes(this.searchTerm)
                || String(a.freelancerId).includes(this.searchTerm);
            const matchStatus = !this.selectedStatus || a.status === this.selectedStatus;
            return matchSearch && matchStatus;
        });
    }

    onSearch(term: string): void {
        this.searchTerm = term;
        this.applyFilter();
    }

    onFilterStatus(status: string): void {
        this.selectedStatus = status;
        this.applyFilter();
    }

    onCreate(): void {
        this.router.navigate(['/recruitment/applications/create']);
    }

    onView(row: RecruitmentApplication): void {
        this.router.navigate(['/recruitment/applications', row.id]);
    }

    onEdit(row: RecruitmentApplication): void {
        this.router.navigate(['/recruitment/applications', row.id, 'edit']);
    }

    onDelete(row: RecruitmentApplication): void {
        if (confirm('Delete this application?')) {
            this.applicationService.delete(row.id!).subscribe({ next: () => this.loadData() });
        }
    }
}