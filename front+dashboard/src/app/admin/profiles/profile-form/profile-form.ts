import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfileService } from '../../../services/profile.service';
import { FreelancerProfile } from '../../../models/profile.model';

@Component({
  selector: 'app-profile-form',
  standalone: false,
  templateUrl: './profile-form.html',
  styleUrl: './profile-form.scss'
})
export class ProfileForm implements OnInit {
  isEditMode = false;
  profileId?: number;
  loading = false;
  success = false;

  profile: FreelancerProfile = {
    userId: 1,
    bio: '',
    titre: '',
    tauxHoraire: 0,
    localisation: '',
    disponibilite: 'AVAILABLE',
    domaineExpertise: ''
  };

  domaines = ['Développement Web', 'Design', 'Rédaction', 'Traduction', 'Marketing', 'Data Science'];
  disponibilites = ['AVAILABLE', 'BUSY', 'NOT_AVAILABLE'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private profileService: ProfileService
  ) {}

  ngOnInit() {
    this.profileId = this.route.snapshot.params['id'];
    if (this.profileId) {
      this.isEditMode = true;
      this.profileService.getById(this.profileId).subscribe(data => {
        this.profile = data;
      });
    }
  }

  save() {
    this.loading = true;
    if (this.isEditMode && this.profileId) {
      this.profileService.update(this.profileId, this.profile).subscribe({
        next: () => { this.success = true; this.loading = false;
          setTimeout(() => this.router.navigate(['/admin/profiles']), 1500);
        },
        error: () => this.loading = false
      });
    } else {
      this.profileService.create(this.profile).subscribe({
        next: () => { this.success = true; this.loading = false;
          setTimeout(() => this.router.navigate(['/admin/profiles']), 1500);
        },
        error: () => this.loading = false
      });
    }
  }

  cancel() {
    this.router.navigate(['/admin/profiles']);
  }
}