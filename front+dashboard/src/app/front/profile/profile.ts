import { Component, OnInit } from '@angular/core';
import { ProfileService } from '../../services/profile.service';
import { FreelancerProfile } from '../../models/profile.model';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.html',
  styleUrl: './profile.scss'
})
export class Profile implements OnInit {
  profile: FreelancerProfile | null = null;
  loading = true;

  constructor(private profileService: ProfileService) {}

  ngOnInit() {
    this.profileService.getById(1).subscribe({
      next: data => { this.profile = data; this.loading = false; },
      error: () => this.loading = false
    });
  }
}