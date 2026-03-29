import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, forkJoin, map, of, switchMap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { JobPost, JobService } from './job.service';

export interface ApplicationResponse {
  id: number;
  jobPostId: number;
  freelancerId: number;
  coverLetter: string;
  proposedAmount: number;
  proposedDeadline: string;
  status: string;
  createdAt: string;
  counterOfferAmount?: number;
  counterOfferNote?: string;
}

export interface ApplicationCreateRequest {
  jobPostId: number;
  freelancerId: number;
  coverLetter: string;
  proposedAmount: number;
  proposedDeadline: string;
}

export interface ApplicationWithJob {
  application: ApplicationResponse;
  job: JobPost | null;
}

@Injectable({ providedIn: 'root' })
export class ApplicationService {
  private readonly apiUrl = environment.api.jobBaseUrl;

  constructor(
    private readonly http: HttpClient,
    private readonly jobs: JobService
  ) {}

  apply(payload: ApplicationCreateRequest): Observable<ApplicationResponse> {
    return this.http.post<ApplicationResponse>(`${this.apiUrl}/applications`, payload);
  }

  getById(id: number): Observable<ApplicationResponse> {
    return this.http.get<ApplicationResponse>(`${this.apiUrl}/applications/${id}`);
  }

  getByJob(jobId: number): Observable<ApplicationResponse[]> {
    return this.http.get<ApplicationResponse[]>(`${this.apiUrl}/jobs/${jobId}/applications`);
  }

  withdraw(applicationId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/applications/${applicationId}`);
  }

  getMyApplications(userId: number): Observable<ApplicationResponse[]> {
    return this.jobs.getAllJobs().pipe(
      switchMap((jobs) => {
        if (jobs.length === 0) {
          return of([]);
        }

        return forkJoin(
          jobs.map((job) => this.getByJob(job.id).pipe(catchError(() => of([]))))
        ).pipe(
          map((items) =>
            items
              .flat()
              .filter((application) => application.freelancerId === userId)
              .sort(
                (a, b) =>
                  new Date(b.createdAt ?? 0).getTime() - new Date(a.createdAt ?? 0).getTime()
              )
          )
        );
      })
    );
  }

  getMyApplicationsWithJobs(userId: number): Observable<ApplicationWithJob[]> {
    return this.getMyApplications(userId).pipe(
      switchMap((applications) => {
        if (applications.length === 0) {
          return of([]);
        }

        return forkJoin(
          applications.map((application) =>
            this.jobs.getJobById(application.jobPostId).pipe(
              map((job) => ({ application, job })),
              catchError(() => of({ application, job: null }))
            )
          )
        );
      })
    );
  }
}
