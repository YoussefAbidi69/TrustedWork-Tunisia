import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CertificationExam } from '../models/profile.model';

@Injectable({ providedIn: 'root' })
export class ExamService {
  private api = 'http://localhost:8081/api/exams';
  constructor(private http: HttpClient) {}

  getAll(): Observable<CertificationExam[]> {
    return this.http.get<CertificationExam[]>(this.api);
  }
  create(e: CertificationExam): Observable<CertificationExam> {
    return this.http.post<CertificationExam>(this.api, e);
  }
  update(id: number, e: CertificationExam): Observable<CertificationExam> {
    return this.http.put<CertificationExam>(`${this.api}/${id}`, e);
  }
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }
}