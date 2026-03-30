import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TalentPoolService } from '../../../services/talent-pool.service';
import { TalentPool } from '../../../models';

@Component({
  selector: 'app-talent-pool-detail',
  templateUrl: './talent-pool-detail.component.html',
  styleUrls: ['./talent-pool-detail.component.css']
})
export class TalentPoolDetailComponent implements OnInit {
  talent: TalentPool | null = null;
  loading = true;

  tagConfig: Record<string, { icon: string; color: string }> = {
    WATCHLIST: { icon: '👀', color: '#fbbf24' },
    FAVORITE: { icon: '⭐', color: '#f59e0b' },
    CONTACTED: { icon: '📨', color: '#22d3ee' }
  };

  sourceConfig: Record<string, { icon: string }> = {
    SEARCH: { icon: '🔍' },
    HACKATHON: { icon: '🏆' },
    RECOMMENDATION: { icon: '🤝' }
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private talentPoolService: TalentPoolService
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.talentPoolService.getById(id).subscribe({
        next: (data) => { this.talent = data; this.loading = false; },
        error: () => { this.loading = false; }
      });
    }
  }

  getTagIcon(tag: string): string { return this.tagConfig[tag]?.icon || '🏷️'; }
  getTagColor(tag: string): string { return this.tagConfig[tag]?.color || '#6b7280'; }
  getSourceIcon(src: string): string { return this.sourceConfig[src]?.icon || '📌'; }

  goBack(): void { this.router.navigate(['/recruitment/talent-pool']); }
  onEdit(): void { this.router.navigate(['/recruitment/talent-pool', this.talent!.id, 'edit']); }
  onDelete(): void {
    if (this.talent && confirm('Retirer ce freelancer du vivier ?')) {
      this.talentPoolService.delete(this.talent.id!).subscribe({ next: () => this.goBack() });
    }
  }
}