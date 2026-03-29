import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SkillBadge } from '../models/profile.model';

@Injectable({ providedIn: 'root' })
export class SkillService {
  private api = 'http://localhost:8081/api/skills';
  private profileApi = 'http://localhost:8081/api/profiles';

  constructor(private http: HttpClient) {}

  getAll(): Observable<SkillBadge[]> {
    return this.http.get<SkillBadge[]>(this.api);
  }

  getById(id: number): Observable<SkillBadge> {
    return this.http.get<SkillBadge>(`${this.api}/${id}`);
  }

  getByProfile(profileId: number): Observable<SkillBadge[]> {
    return this.http.get<SkillBadge[]>(
      `${this.profileApi}/${profileId}/skills`
    );
  }

  create(s: SkillBadge): Observable<SkillBadge> {
    return this.http.post<SkillBadge>(this.api, s);
  }

  update(id: number, s: SkillBadge): Observable<SkillBadge> {
    return this.http.put<SkillBadge>(`${this.api}/${id}`, s);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}