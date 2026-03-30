import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TalentPoolService } from '../../../services/talent-pool.service';
import { TalentPool } from '../../../models';
import { TableColumn } from '../../../shared/recruitment-table/recruitment-table.component';

@Component({
  selector: 'app-talent-pool-list',
  templateUrl: './talent-pool-list.component.html',
  styleUrls: ['./talent-pool-list.component.css']
})
export class TalentPoolListComponent implements OnInit {
  talents: TalentPool[] = [];
  filtered: TalentPool[] = [];
  loading = false;
  entrepriseId: number | null = null;
  selectedTag = '';
  searched = false;

  tagOptions: Array<{ value: string; label: string; icon: string }> = [
    { value: '', label: 'Tous les tags', icon: '' },
    { value: 'WATCHLIST', label: 'Watchlist', icon: '👀' },
    { value: 'FAVORITE', label: 'Favoris', icon: '⭐' },
    { value: 'CONTACTED', label: 'Contactés', icon: '📨' }
  ];

  columns: TableColumn[] = [
    { key: 'id', label: 'ID' },
    { key: 'freelancerId', label: 'Freelancer ID' },
    { key: 'tag', label: 'Tag', type: 'badge' },
    { key: 'sourceOrigine', label: 'Source', type: 'badge' },
    { key: 'alerteDisponibilite', label: 'Alerte Dispo' },
    { key: 'dateAjout', label: 'Ajouté le', type: 'date' }
  ];

  constructor(private talentPoolService: TalentPoolService, private router: Router) { }

  ngOnInit(): void { }

  search(): void {
    if (!this.entrepriseId) return;
    this.loading = true;
    this.searched = true;

    const req = this.selectedTag
      ? this.talentPoolService.getByTag(this.entrepriseId, this.selectedTag)
      : this.talentPoolService.getByEntreprise(this.entrepriseId);

    req.subscribe({
      next: (data) => { this.talents = data; this.filtered = data; this.loading = false; },
      error: () => { this.loading = false; }
    });
  }

  onFilterTag(tag: string): void {
    this.selectedTag = tag;
    if (this.searched && this.entrepriseId) this.search();
  }

  countByTag(tag: string): number {
    return this.talents.filter(t => t.tag === tag).length;
  }

  onCreate(): void { this.router.navigate(['/recruitment/talent-pool/create']); }
  onView(row: TalentPool): void { this.router.navigate(['/recruitment/talent-pool', row.id]); }
  onEdit(row: TalentPool): void { this.router.navigate(['/recruitment/talent-pool', row.id, 'edit']); }
  onDelete(row: TalentPool): void {
    if (confirm('Retirer ce freelancer du vivier ?')) {
      this.talentPoolService.delete(row.id!).subscribe({ next: () => this.search() });
    }
  }
}