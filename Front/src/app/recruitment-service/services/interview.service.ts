import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InterviewSchedule } from '../models';

@Injectable({
    providedIn: 'root'
})
export class InterviewService {
    private baseUrl = 'http://localhost:8089/recruitment/interviews';

    constructor(private http: HttpClient) { }

    getAll(): Observable<InterviewSchedule[]> {
        return this.http.get<InterviewSchedule[]>(this.baseUrl);
    }

    getById(id: number): Observable<InterviewSchedule> {
        return this.http.get<InterviewSchedule>(`${this.baseUrl}/${id}`);
    }

    create(interview: InterviewSchedule): Observable<InterviewSchedule> {
        return this.http.post<InterviewSchedule>(this.baseUrl, interview);
    }

    update(id: number, interview: InterviewSchedule): Observable<InterviewSchedule> {
        return this.http.put<InterviewSchedule>(`${this.baseUrl}/${id}`, interview);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
