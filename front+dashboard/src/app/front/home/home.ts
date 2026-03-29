import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';
import { FreelancerProfile } from '../../models/profile.model';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.html',
  styleUrl: './home.scss'
})
export class Home implements OnInit {
  profiles: FreelancerProfile[] = [];
  loading = true;

  constructor(private profileService: ProfileService) {}

  ngOnInit() {
    this.profileService.getAll().subscribe({
      next: data => { this.profiles = data; this.loading = false; },
      error: () => this.loading = false
    });
  }

  getBadgeClass(dispo: string): string {
    if (dispo === 'AVAILABLE') return 'badge-available';
    if (dispo === 'BUSY') return 'badge-busy';
    return 'badge-not-available';
  }
}