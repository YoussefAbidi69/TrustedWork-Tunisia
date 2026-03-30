import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InterviewSchedule } from '../models';

@Injectable({ providedIn: 'root' })
export class InterviewService {
    private baseUrl = 'http://localhost:8089/recruitment/interviews';

    constructor(private http: HttpClient) { }

    // GET /interviews
    getAll(): Observable<InterviewSchedule[]> {
        return this.http.get<InterviewSchedule[]>(this.baseUrl);
    }

    // GET /interviews/{id}
    getById(id: number): Observable<InterviewSchedule> {
        return this.http.get<InterviewSchedule>(`${this.baseUrl}/${id}`);
    }

    // POST /interviews
    create(interview: InterviewSchedule): Observable<InterviewSchedule> {
        return this.http.post<InterviewSchedule>(this.baseUrl, interview);
    }

    // PUT /interviews/{id}
    update(id: number, interview: InterviewSchedule): Observable<InterviewSchedule> {
        return this.http.put<InterviewSchedule>(`${this.baseUrl}/${id}`, interview);
    }

    // DELETE /interviews/{id}
    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    // GET /interviews/application/{applicationId}
    getByApplication(applicationId: number): Observable<InterviewSchedule[]> {
        return this.http.get<InterviewSchedule[]>(`${this.baseUrl}/application/${applicationId}`);
    }

    // PATCH /interviews/{id}/status?status=CONFIRMED
    updateStatus(id: number, status: string): Observable<InterviewSchedule> {
        const params = new HttpParams().set('status', status);
        return this.http.patch<InterviewSchedule>(`${this.baseUrl}/${id}/status`, null, { params });
    }

    // PATCH /interviews/{id}/feedback?feedback=xxx&note=8
    addFeedback(id: number, feedback: string, note: number): Observable<InterviewSchedule> {
        const params = new HttpParams().set('feedback', feedback).set('note', note.toString());
        return this.http.patch<InterviewSchedule>(`${this.baseUrl}/${id}/feedback`, null, { params });
    }
}