import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';
import { SkillService } from '../../services/skill.service';
import { FreelancerProfile, SkillBadge } from '../../models/profile.model';

@Component({
  selector: 'app-passport',
  standalone: false,
  templateUrl: './passport.html',
  styleUrl: './passport.scss'
})
export class Passport implements OnInit {
  profile: FreelancerProfile | null = null;
  skills: SkillBadge[] = [];
  loading = true;
  profileId = 1;

  constructor(
    private profileService: ProfileService,
    private skillService: SkillService
  ) {}

  ngOnInit() {
    this.profileService.getById(this.profileId).subscribe({
      next: (data: FreelancerProfile) => {
        this.profile = data;
        this.loading = false;
      }
    });
    this.skillService.getByProfile(this.profileId).subscribe({
      next: (data: SkillBadge[]) => this.skills = data
    });
  }

  getNiveauClass(niveau: string): string {
    if (niveau === 'JUNIOR') return 'badge-junior';
    if (niveau === 'CONFIRMED') return 'badge-confirmed';
    return 'badge-expert';
  }

  print() {
    window.print();
  }
}