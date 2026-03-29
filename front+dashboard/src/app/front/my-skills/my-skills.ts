import { Component, OnInit } from '@angular/core';
import { SkillService } from '../../services/skill.service';
import { SkillBadge } from '../../models/profile.model';

@Component({
  selector: 'app-my-skills',
  standalone: false,
  templateUrl: './my-skills.html',
  styleUrl: './my-skills.scss'
})
export class MySkills implements OnInit {
  skills: SkillBadge[] = [];
  loading = true;
  profileId = 1;

  constructor(private skillService: SkillService) {}

  ngOnInit() {
    this.skillService.getByProfile(this.profileId).subscribe({
      next: (data: SkillBadge[]) => {
        this.skills = data;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  getNiveauClass(niveau: string): string {
    if (niveau === 'JUNIOR') return 'badge-junior';
    if (niveau === 'CONFIRMED') return 'badge-confirmed';
    return 'badge-expert';
  }

  getNiveauIcon(niveau: string): string {
    if (niveau === 'JUNIOR') return 'fas fa-seedling';
    if (niveau === 'CONFIRMED') return 'fas fa-star-half-alt';
    return 'fas fa-star';
  }
}