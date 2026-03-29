import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProfileService } from '../../../services/profile.service';
import { FreelancerProfile } from '../../../models/profile.model';

@Component({
  selector: 'app-profile-list',
  standalone: false,
  templateUrl: './profile-list.html',
  styleUrl: './profile-list.scss'
})
export class ProfileList implements OnInit {
  profiles: FreelancerProfile[] = [];
  loading = true;

  constructor(
    private profileService: ProfileService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadProfiles();
  }

  loadProfiles() {
    this.loading = true;
    this.profileService.getAll().subscribe({
      next: data => { this.profiles = data; this.loading = false; },
      error: () => this.loading = false
    });
  }

  edit(id: number) {
    this.router.navigate(['/admin/profiles/edit', id]);
  }

  delete(id: number) {
    if (confirm('Supprimer ce profil ?')) {
      this.profileService.delete(id).subscribe(() => this.loadProfiles());
    }
  }

  getBadgeClass(dispo: string): string {
    if (dispo === 'AVAILABLE') return 'badge-available';
    if (dispo === 'BUSY') return 'badge-busy';
    return 'badge-not-available';
  }
}