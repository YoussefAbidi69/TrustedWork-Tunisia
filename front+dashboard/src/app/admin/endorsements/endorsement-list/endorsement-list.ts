import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-endorsement-list',
  standalone: false,
  templateUrl: './endorsement-list.html',
  styleUrl: './endorsement-list.scss'
})
export class EndorsementList implements OnInit {
  endorsements: any[] = [];
  allEndorsements: any[] = [];
  loading = true;
  filterProfileId: number | null = null;
  filterStatus: string = 'all';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadAll();
  }

  loadAll() {
    this.loading = true;
    this.endorsements = [];
    this.allEndorsements = [];

    // Charge endorsements pour profils 1 à 20
    let completed = 0;
    const total = 20;

    for (let i = 1; i <= total; i++) {
      // Utilise detail/{id} pour récupérer même non modérés
      this.http.get<any>(
        `http://localhost:8081/api/endorsements/detail/${i}`
      ).subscribe({
        next: (data: any) => {
          if (data) {
            this.allEndorsements.push(data);
            this.endorsements = [...this.allEndorsements];
          }
          completed++;
          if (completed === total) this.loading = false;
        },
        error: () => {
          completed++;
          if (completed === total) this.loading = false;
        }
      });
    }
  }

  filterAll() {
    this.filterStatus = 'all';
    this.endorsements = [...this.allEndorsements];
  }

  filterPending() {
    this.filterStatus = 'pending';
    this.endorsements = this.allEndorsements.filter(e => !e.isModerated);
  }

  filterApproved() {
    this.filterStatus = 'approved';
    this.endorsements = this.allEndorsements.filter(e => e.isModerated);
  }

  searchByProfile() {
    if (!this.filterProfileId) {
      this.endorsements = [...this.allEndorsements];
      return;
    }
    this.endorsements = this.allEndorsements.filter(
      e => e.toProfileId === this.filterProfileId ||
           e.fromProfileId === this.filterProfileId
    );
  }

  moderate(id: number) {
    this.http.put(
      `http://localhost:8081/api/endorsements/${id}/moderate`, {}
    ).subscribe({
      next: () => {
        const e = this.allEndorsements.find(e => e.id === id);
        if (e) e.isModerated = true;
        const e2 = this.endorsements.find(e => e.id === id);
        if (e2) e2.isModerated = true;
      }
    });
  }

  delete(id: number) {
    if (confirm('Supprimer cet endorsement ?')) {
      this.http.delete(
        `http://localhost:8081/api/endorsements/${id}`
      ).subscribe({
        next: () => {
          this.allEndorsements = this.allEndorsements.filter(e => e.id !== id);
          this.endorsements = this.endorsements.filter(e => e.id !== id);
        }
      });
    }
  }

  getPending(): number {
    return this.allEndorsements.filter(e => !e.isModerated).length;
  }

  getApproved(): number {
    return this.allEndorsements.filter(e => e.isModerated).length;
  }
}