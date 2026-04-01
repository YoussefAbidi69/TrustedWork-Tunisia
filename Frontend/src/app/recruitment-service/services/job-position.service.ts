import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobPosition } from '../models';

@Injectable({
    providedIn: 'root'
})
export class JobPositionService {
    private baseUrl = 'http://localhost:8089/recruitment/job-positions';

    constructor(private http: HttpClient) { }

    getAll(): Observable<JobPosition[]> {
        return this.http.get<JobPosition[]>(this.baseUrl);
    }

    getById(id: number): Observable<JobPosition> {
        return this.http.get<JobPosition>(`${this.baseUrl}/${id}`);
    }

    create(jobPosition: JobPosition): Observable<JobPosition> {
        return this.http.post<JobPosition>(this.baseUrl, jobPosition);
    }

    update(id: number, jobPosition: JobPosition): Observable<JobPosition> {
        return this.http.put<JobPosition>(`${this.baseUrl}/${id}`, jobPosition);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
    getPublished(): Observable<JobPosition[]> {
        return this.http.get<JobPosition[]>(`${this.baseUrl}/published`);
    }



    getByEntreprise(entrepriseId: number): Observable<JobPosition[]> {
        return this.http.get<JobPosition[]>(`${this.baseUrl}/entreprise/${entrepriseId}`);
    }

    generateDescription(id: number) {
        return this.http.post(
            `${this.baseUrl}/${id}/generate-description`,
            {}
        );
    }

}
