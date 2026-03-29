import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ProfileService } from '../../services/profile.service';
import { FreelancerProfile } from '../../models/profile.model';

@Component({
  selector: 'app-endorsements',
  standalone: false,
  templateUrl: './endorsements.html',
  styleUrl: './endorsements.scss'
})
export class Endorsements implements OnInit {
  profiles: FreelancerProfile[] = [];
  endorsements: any[] = [];
  loading = true;
  showForm = false;
  success = false;
  myProfileId = 1;

  newEndorsement = {
    fromProfileId: 1,
    toProfileId: 0,
    skillId: 1,
    commentaire: ''
  };

  constructor(
    private profileService: ProfileService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.profileService.getAll().subscribe(data => {
      this.profiles = data.filter(p => p.id !== this.myProfileId);
    });

    this.http.get<any[]>(
      `http://localhost:8081/api/endorsements/${this.myProfileId}`
    ).subscribe({
      next: data => { this.endorsements = data; this.loading = false; },
      error: () => this.loading = false
    });
  }

  sendEndorsement() {
    this.http.post('http://localhost:8081/api/endorsements',
      this.newEndorsement
    ).subscribe({
      next: () => {
        this.success = true;
        this.showForm = false;
        setTimeout(() => this.success = false, 3000);
        this.newEndorsement = {
          fromProfileId: 1, toProfileId: 0,
          skillId: 1, commentaire: ''
        };
      }
    });
  }
}