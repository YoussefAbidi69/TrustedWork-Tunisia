import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TalentPool } from '../models';

@Injectable({
    providedIn: 'root'
})
export class TalentPoolService {
    private baseUrl = 'http://localhost:8089/recruitment/talent-pool';

    constructor(private http: HttpClient) { }

    getAll(): Observable<TalentPool[]> {
        return this.http.get<TalentPool[]>(this.baseUrl);
    }

    getById(id: number): Observable<TalentPool> {
        return this.http.get<TalentPool>(`${this.baseUrl}/${id}`);
    }

    create(talent: TalentPool): Observable<TalentPool> {
        return this.http.post<TalentPool>(this.baseUrl, talent);
    }

    update(id: number, talent: TalentPool): Observable<TalentPool> {
        return this.http.put<TalentPool>(`${this.baseUrl}/${id}`, talent);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
