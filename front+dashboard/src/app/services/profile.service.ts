import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FreelancerProfile } from '../models/profile.model';

@Injectable({ providedIn: 'root' })
export class ProfileService {
  private api = 'http://localhost:8081/api/profiles';
  constructor(private http: HttpClient) {}

  getAll(): Observable<FreelancerProfile[]> {
    return this.http.get<FreelancerProfile[]>(this.api);
  }
  getById(id: number): Observable<FreelancerProfile> {
    return this.http.get<FreelancerProfile>(`${this.api}/${id}`);
  }
  create(p: FreelancerProfile): Observable<FreelancerProfile> {
    return this.http.post<FreelancerProfile>(this.api, p);
  }
  update(id: number, p: FreelancerProfile): Observable<FreelancerProfile> {
    return this.http.put<FreelancerProfile>(`${this.api}/${id}`, p);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}