import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';

export type JobType = 'SERVICE_OFFER' | 'PROJECT_REQUEST';
export type JobStatus = 'DRAFT' | 'PUBLISHED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

export interface JobPost {
  id: number;
  clientId: number;
  title: string;
  description: string;
  type: JobType;
  category: string;
  budget: number;
  requiredSkills: string;
  region: string;
  deadline: string;
  status: JobStatus;
  createdAt: string;
  isClientFlagged: boolean;
  aiEnhanced: boolean;
}

@Injectable({ providedIn: 'root' })
export class JobService {
  private readonly baseUrl = `${environment.api.jobBaseUrl}/jobs`;

  constructor(private readonly http: HttpClient) {}

  getJobs(): Observable<JobPost[]>;
  getJobs(page: number, pageSize: number): Observable<JobPost[]>;
  getJobs(page?: number, pageSize?: number): Observable<JobPost[]> {
    return this.http.get<JobPost[]>(this.baseUrl).pipe(
      map((jobs) => {
        const ordered = this.sortByDateDesc(jobs ?? []);
        if (page == null || pageSize == null) {
          return ordered;
        }
        const start = page * pageSize;
        return ordered.slice(start, start + pageSize);
      })
    );
  }

  getAllJobs(): Observable<JobPost[]> {
    return this.getJobs();
  }

  getJobById(id: number): Observable<JobPost> {
    return this.http.get<JobPost>(`${this.baseUrl}/${id}`);
  }

  getSuggestedJobs(limit = 4): Observable<JobPost[]> {
    return this.getJobs().pipe(map((jobs) => jobs.slice(0, limit)));
  }

  private sortByDateDesc(items: JobPost[]): JobPost[] {
    return [...items].sort((a, b) => {
      const aDate = new Date(a.createdAt ?? 0).getTime();
      const bDate = new Date(b.createdAt ?? 0).getTime();
      return bDate - aDate;
    });
  }
}
