import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { JobPositionService } from '../../../services/job-position.service';
import { JobPosition } from '../../../models';

@Component({
  selector: 'app-job-position-published',
  templateUrl: './job-position-published.component.html',
  styleUrls: ['./job-position-published.component.css']
})
export class JobPositionPublishedComponent implements OnInit {

  allPublished: JobPosition[] = [];
  filtered: JobPosition[] = [];
  loading = true;

  // Filtres
  searchText = '';
  selectedContract = '';
  selectedRemote = '';

  contractTypes = ['CDI', 'CDD', 'CIVP', 'STAGE', 'ALTERNANCE'];

  constructor(
    private jobPositionService: JobPositionService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.jobPositionService.getPublished().subscribe({
      next: (data) => {
        this.allPublished = data;
        this.filtered = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    this.filtered = this.allPublished.filter(job => {
      const matchSearch = !this.searchText ||
        job.titre?.toLowerCase().includes(this.searchText.toLowerCase()) ||
        job.localisation?.toLowerCase().includes(this.searchText.toLowerCase()) ||
        job.description?.toLowerCase().includes(this.searchText.toLowerCase());

      const matchContract = !this.selectedContract || job.typeContrat === this.selectedContract;

      const matchRemote = this.selectedRemote === '' ? true :
        this.selectedRemote === 'true' ? job.remote === true : job.remote === false;

      return matchSearch && matchContract && matchRemote;
    });
  }

  onSearchChange(value: string): void {
    this.searchText = value;
    this.applyFilters();
  }

  onContractChange(value: string): void {
    this.selectedContract = value;
    this.applyFilters();
  }

  onRemoteChange(value: string): void {
    this.selectedRemote = value;
    this.applyFilters();
  }

  clearFilters(): void {
    this.searchText = '';
    this.selectedContract = '';
    this.selectedRemote = '';
    this.filtered = [...this.allPublished];
  }

  onViewDetail(job: JobPosition): void {
    this.router.navigate(['/recruitment/job-positions', job.id]);
  }

  getSkillsArray(skillsJson: string): string[] {
    if (!skillsJson) return [];
    try {
      // Supporte JSON array ["a","b"] ou string "a,b"
      const parsed = JSON.parse(skillsJson);
      return Array.isArray(parsed) ? parsed : [skillsJson];
    } catch {
      return skillsJson.split(',').map(s => s.trim()).filter(Boolean);
    }
  }

  isDeadlineSoon(deadline: string): boolean {
    if (!deadline) return false;
    const diff = new Date(deadline).getTime() - Date.now();
    return diff > 0 && diff < 7 * 24 * 60 * 60 * 1000; // moins de 7 jours
  }

  isExpired(deadline: string): boolean {
    if (!deadline) return false;
    return new Date(deadline).getTime() < Date.now();
  }
}