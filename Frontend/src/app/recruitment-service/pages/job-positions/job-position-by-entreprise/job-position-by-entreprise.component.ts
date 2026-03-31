import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobPositionService } from '../../../services/job-position.service';
import { JobPosition } from '../../../models';

@Component({
  selector: 'app-job-position-by-entreprise',
  templateUrl: './job-position-by-entreprise.component.html',
  styleUrls: ['./job-position-by-entreprise.component.css']
})
export class JobPositionByEntrepriseComponent implements OnInit {

  allJobs: JobPosition[] = [];
  filtered: JobPosition[] = [];
  loading = true;

  // Mock entrepriseId — Phase 2 : récupérer depuis AuthService ou token JWT
  entrepriseId = 2;

  // Filtres
  searchText = '';
  selectedContract = '';
  selectedStatus = '';

  contractTypes = ['CDI', 'CDD', 'CIVP', 'STAGE', 'ALTERNANCE'];
  statuses = ['DRAFT', 'PUBLISHED', 'PAUSED', 'CLOSED', 'FILLED'];

  constructor(
    private jobPositionService: JobPositionService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.jobPositionService.getByEntreprise(this.entrepriseId).subscribe({
      next: (data) => {
        this.allJobs = data;
        this.filtered = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    this.filtered = this.allJobs.filter(job => {
      const matchSearch = !this.searchText ||
        job.titre?.toLowerCase().includes(this.searchText.toLowerCase()) ||
        job.localisation?.toLowerCase().includes(this.searchText.toLowerCase());

      const matchContract = !this.selectedContract || job.typeContrat === this.selectedContract;
      const matchStatus = !this.selectedStatus || job.status === this.selectedStatus;

      return matchSearch && matchContract && matchStatus;
    });
  }

  onSearchChange(value: string): void { this.searchText = value; this.applyFilters(); }
  onContractChange(value: string): void { this.selectedContract = value; this.applyFilters(); }
  onStatusChange(value: string): void { this.selectedStatus = value; this.applyFilters(); }

  clearFilters(): void {
    this.searchText = '';
    this.selectedContract = '';
    this.selectedStatus = '';
    this.filtered = [...this.allJobs];
  }

  onCreate(): void {
    this.router.navigate(['/recruitment/job-positions/create']);
  }

  onView(job: JobPosition): void {
    this.router.navigate(['/recruitment/job-positions', job.id]);
  }

  onEdit(job: JobPosition): void {
    this.router.navigate(['/recruitment/job-positions', job.id, 'edit']);
  }

  onDelete(job: JobPosition): void {
    if (confirm('Supprimer cette offre ?')) {
      this.jobPositionService.delete(job.id!).subscribe({
        next: () => this.loadData()
      });
    }
  }

  getSkillsArray(skillsJson: string): string[] {
    if (!skillsJson) return [];
    try {
      const parsed = JSON.parse(skillsJson);
      return Array.isArray(parsed) ? parsed : [skillsJson];
    } catch {
      return skillsJson.split(',').map(s => s.trim()).filter(Boolean);
    }
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      PUBLISHED: 'status-published',
      DRAFT: 'status-draft',
      PAUSED: 'status-paused',
      CLOSED: 'status-closed',
      FILLED: 'status-filled'
    };
    return map[status] ?? '';
  }

  isDeadlineSoon(deadline: string): boolean {
    if (!deadline) return false;
    const diff = new Date(deadline).getTime() - Date.now();
    return diff > 0 && diff < 7 * 24 * 60 * 60 * 1000;
  }

  isExpired(deadline: string): boolean {
    if (!deadline) return false;
    return new Date(deadline).getTime() < Date.now();
  }

  // Stats rapides
  get totalPublished(): number { return this.allJobs.filter(j => j.status === 'PUBLISHED').length; }
  get totalDraft(): number { return this.allJobs.filter(j => j.status === 'DRAFT').length; }
  get totalFilled(): number { return this.allJobs.filter(j => j.status === 'FILLED').length; }
  get totalCandidatures(): number {
    return this.allJobs.reduce((sum, j) => sum + (j.nombreCandidatures ?? 0), 0);
  }
}