import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApplicationService } from '../../../services/application.service';
import { RecruitmentApplication } from '../../../models';
import { TableColumn } from '../../../shared/recruitment-table/recruitment-table.component';

@Component({
  selector: 'app-application-by-job',
  templateUrl: './application-by-job.component.html',
  styleUrls: ['./application-by-job.component.css']
})
export class ApplicationByJobComponent implements OnInit {
  applications: RecruitmentApplication[] = [];
  loading = false;
  jobPositionId: number | null = null;
  ranked = false;

  columns: TableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'freelancerId', label: 'Freelancer ID' },
    { key: 'matchingScore', label: 'Score %' },
    { key: 'pretentionSalariale', label: 'Salaire', type: 'currency' },
    { key: 'disponibilite', label: 'Disponibilité' },
    { key: 'status', label: 'Statut', type: 'badge' }
  ];

  constructor(private applicationService: ApplicationService, private router: Router) { }

  ngOnInit(): void { }

  search(): void {
    if (!this.jobPositionId) return;
    this.loading = true;
    const req = this.ranked
      ? this.applicationService.getByJobPositionRanked(this.jobPositionId)
      : this.applicationService.getByJobPosition(this.jobPositionId);
    req.subscribe({
      next: (data) => { this.applications = data; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  onView(row: RecruitmentApplication): void {
    this.router.navigate(['/recruitment/applications', row.id]);
  }
}