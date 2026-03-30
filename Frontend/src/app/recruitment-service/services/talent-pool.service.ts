import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TalentPool } from '../models';

@Injectable({ providedIn: 'root' })
export class TalentPoolService {
    private baseUrl = 'http://localhost:8089/recruitment/talent-pool';

    constructor(private http: HttpClient) { }

    // POST /talent-pool
    add(talent: TalentPool): Observable<TalentPool> {
        return this.http.post<TalentPool>(this.baseUrl, talent);
    }

    // PUT /talent-pool/{id}
    update(id: number, talent: TalentPool): Observable<TalentPool> {
        return this.http.put<TalentPool>(`${this.baseUrl}/${id}`, talent);
    }

    // DELETE /talent-pool/{id}
    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    // GET /talent-pool/{id}
    getById(id: number): Observable<TalentPool> {
        return this.http.get<TalentPool>(`${this.baseUrl}/${id}`);
    }

    // GET /talent-pool/entreprise/{entrepriseId}
    getByEntreprise(entrepriseId: number): Observable<TalentPool[]> {
        return this.http.get<TalentPool[]>(`${this.baseUrl}/entreprise/${entrepriseId}`);
    }

    // GET /talent-pool/entreprise/{entrepriseId}/tag/{tag}
    getByTag(entrepriseId: number, tag: string): Observable<TalentPool[]> {
        return this.http.get<TalentPool[]>(`${this.baseUrl}/entreprise/${entrepriseId}/tag/${tag}`);
    }
}