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

    // Vrais statuts du backend
    statusOptions = ['', 'SUBMITTED', 'REVIEWED', 'SHORTLISTED', 'INTERVIEW', 'OFFERED', 'HIRED', 'REJECTED', 'PENDING'];

    columns: TableColumn[] = [
        { key: 'id', label: 'ID' },
        { key: 'jobPositionId', label: 'Job ID' },
        { key: 'jobPositionTitre', label: 'Poste' },
        { key: 'freelancerId', label: 'Freelancer ID' },
        { key: 'pretentionSalariale', label: 'Salaire', type: 'currency' },
        { key: 'matchingScore', label: 'Score %' },
        { key: 'status', label: 'Statut', type: 'badge' }
    ];

    constructor(
        private applicationService: ApplicationService,
        private router: Router
    ) { }

    ngOnInit(): void { this.loadData(); }

    loadData(): void {
        this.loading = true;
        this.applicationService.getAll().subscribe({
            next: (data) => { this.applications = data; this.applyFilter(); this.loading = false; },
            error: () => { this.loading = false; }
        });
    }

    applyFilter(): void {
        this.filtered = this.applications.filter(a => {
            const term = this.searchTerm.toLowerCase();
            const matchSearch = !term
                || a.jobPositionTitre?.toLowerCase().includes(term)
                || a.disponibilite?.toLowerCase().includes(term)
                || String(a.jobPositionId).includes(term)
                || String(a.freelancerId).includes(term);
            const matchStatus = !this.selectedStatus || a.status === this.selectedStatus;
            return matchSearch && matchStatus;
        });
    }

    onSearch(term: string): void { this.searchTerm = term; this.applyFilter(); }
    onFilterStatus(s: string): void { this.selectedStatus = s; this.applyFilter(); }

    onCreate(): void { this.router.navigate(['/recruitment/applications/create']); }
    onView(row: RecruitmentApplication): void { this.router.navigate(['/recruitment/applications', row.id]); }
    onEdit(row: RecruitmentApplication): void { this.router.navigate(['/recruitment/applications', row.id, 'edit']); }
    onDelete(row: RecruitmentApplication): void {
        if (confirm('Supprimer cette candidature ?')) {
            this.applicationService.delete(row.id!).subscribe({ next: () => this.loadData() });
        }
    }
}