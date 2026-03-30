import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecruitmentApplication } from '../models';

@Injectable({ providedIn: 'root' })
export class ApplicationService {
    private baseUrl = 'http://localhost:8089/recruitment/applications';

    constructor(private http: HttpClient) { }

    // GET /applications
    getAll(): Observable<RecruitmentApplication[]> {
        return this.http.get<RecruitmentApplication[]>(this.baseUrl);
    }

    // GET /applications/{id}
    getById(id: number): Observable<RecruitmentApplication> {
        return this.http.get<RecruitmentApplication>(`${this.baseUrl}/${id}`);
    }

    // POST /applications
    create(application: RecruitmentApplication): Observable<RecruitmentApplication> {
        return this.http.post<RecruitmentApplication>(this.baseUrl, application);
    }

    // PUT /applications/{id}  (si besoin d'un update complet)
    update(id: number, application: RecruitmentApplication): Observable<RecruitmentApplication> {
        return this.http.put<RecruitmentApplication>(`${this.baseUrl}/${id}`, application);
    }

    // DELETE /applications/{id}
    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    // PATCH /applications/{id}/status?status=SHORTLISTED&motifRejet=xxx
    updateStatus(id: number, status: string, motifRejet?: string): Observable<RecruitmentApplication> {
        let params = new HttpParams().set('status', status);
        if (motifRejet) params = params.set('motifRejet', motifRejet);
        return this.http.patch<RecruitmentApplication>(`${this.baseUrl}/${id}/status`, null, { params });
    }

    // GET /applications/job-position/{jobPositionId}
    getByJobPosition(jobPositionId: number): Observable<RecruitmentApplication[]> {
        return this.http.get<RecruitmentApplication[]>(`${this.baseUrl}/job-position/${jobPositionId}`);
    }

    // GET /applications/job-position/{jobPositionId}/ranked
    getByJobPositionRanked(jobPositionId: number): Observable<RecruitmentApplication[]> {
        return this.http.get<RecruitmentApplication[]>(`${this.baseUrl}/job-position/${jobPositionId}/ranked`);
    }

    // GET /applications/freelancer/{freelancerId}
    getByFreelancer(freelancerId: number): Observable<RecruitmentApplication[]> {
        return this.http.get<RecruitmentApplication[]>(`${this.baseUrl}/freelancer/${freelancerId}`);
    }

    // GET /applications/entreprise/{entrepriseId}
    getByEntreprise(entrepriseId: number): Observable<RecruitmentApplication[]> {
        return this.http.get<RecruitmentApplication[]>(`${this.baseUrl}/entreprise/${entrepriseId}`);
    }

    getByStatuses(statuses: string[]): Observable<RecruitmentApplication[]> {
        const params = { statuses: statuses.join(',') };

        return this.http.get<RecruitmentApplication[]>(
            `${this.baseUrl}/statuses`,
            { params }
        );
    }
}