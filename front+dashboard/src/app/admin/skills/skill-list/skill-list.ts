import { Component, OnInit } from '@angular/core';
import { SkillService } from '../../../services/skill.service';
import { ProfileService } from '../../../services/profile.service';
import { SkillBadge, FreelancerProfile } from '../../../models/profile.model';

@Component({
  selector: 'app-skill-list',
  standalone: false,
  templateUrl: './skill-list.html',
  styleUrl: './skill-list.scss'
})
export class SkillList implements OnInit {
  skills: SkillBadge[] = [];
  profiles: FreelancerProfile[] = [];
  loading = true;
  showForm = false;
  isEditMode = false;
  editingId?: number;

  newSkill: SkillBadge = {
    profileId: 0,
    nomSkill: '',
    niveau: 'JUNIOR',
    dateValidation: '',
    certificatHash: ''
  };

  constructor(
    private skillService: SkillService,
    private profileService: ProfileService
  ) {}

  ngOnInit() {
    this.loadSkills();
    this.profileService.getAll().subscribe(data => this.profiles = data);
  }

  loadSkills() {
    this.loading = true;
    this.skillService.getAll().subscribe({
      next: data => { this.skills = data; this.loading = false; },
      error: () => this.loading = false
    });
  }

  openAddForm() {
    this.isEditMode = false;
    this.editingId = undefined;
    this.newSkill = { profileId: 0, nomSkill: '', niveau: 'JUNIOR', dateValidation: '', certificatHash: '' };
    this.showForm = true;
  }

  openEditForm(s: SkillBadge) {
    this.isEditMode = true;
    this.editingId = s.id;
    this.newSkill = { ...s };
    this.showForm = true;
  }

  save() {
    if (this.isEditMode && this.editingId) {
      this.skillService.update(this.editingId, this.newSkill).subscribe({
        next: () => { this.loadSkills(); this.showForm = false; }
      });
    } else {
      this.skillService.create(this.newSkill).subscribe({
        next: () => { this.loadSkills(); this.showForm = false; }
      });
    }
  }

  delete(id: number) {
    if (confirm('Supprimer ce skill ?')) {
      this.skillService.delete(id).subscribe(() => this.loadSkills());
    }
  }

  getNiveauClass(niveau: string): string {
    if (niveau === 'JUNIOR') return 'badge-junior';
    if (niveau === 'CONFIRMED') return 'badge-confirmed';
    return 'badge-expert';
  }
}