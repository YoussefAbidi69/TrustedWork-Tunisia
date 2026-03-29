import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecruitmentApplication } from '../models';

@Injectable({
    providedIn: 'root'
})
export class ApplicationService {
    private baseUrl = 'http://localhost:8089/recruitment/applications';

    constructor(private http: HttpClient) { }

    getAll(): Observable<RecruitmentApplication[]> {
        return this.http.get<RecruitmentApplication[]>(this.baseUrl);
    }

    getById(id: number): Observable<RecruitmentApplication> {
        return this.http.get<RecruitmentApplication>(`${this.baseUrl}/${id}`);
    }

    create(application: RecruitmentApplication): Observable<RecruitmentApplication> {
        return this.http.post<RecruitmentApplication>(this.baseUrl, application);
    }

    updateStatus(id: number, status: string, motifRejet?: string): Observable<RecruitmentApplication> {
        let params: any = { status };

        if (motifRejet) {
            params.motifRejet = motifRejet;
        }

        return this.http.patch<RecruitmentApplication>(
            `${this.baseUrl}/${id}/status`,
            {},
            { params }
        );
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
