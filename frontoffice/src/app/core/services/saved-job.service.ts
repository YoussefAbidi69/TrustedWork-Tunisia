import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { JobPost } from './job.service';

export interface SavedJobResponse {
  id: number;
  freelancerId: number;
  jobPostId: number;
  savedAt: string;
}

@Injectable({ providedIn: 'root' })
export class SavedJobService {
  private readonly jobsBaseUrl = `${environment.api.jobBaseUrl}/jobs`;
  private readonly freelancersBaseUrl = `${environment.api.jobBaseUrl}/freelancers`;

  constructor(private readonly http: HttpClient) {}

  saveJob(jobId: number, freelancerId: number): Observable<SavedJobResponse> {
    return this.http.post<SavedJobResponse>(`${this.jobsBaseUrl}/${jobId}/save`, { freelancerId });
  }

  unsaveJob(jobId: number, freelancerId: number): Observable<void> {
    return this.http.delete<void>(`${this.jobsBaseUrl}/${jobId}/save`, {
      params: { freelancerId }
    });
  }

  getSavedJobs(freelancerId: number): Observable<JobPost[]> {
    return this.http.get<JobPost[]>(`${this.freelancersBaseUrl}/${freelancerId}/saved-jobs`);
  }

  getSavedJobIdSet(freelancerId: number): Observable<Set<number>> {
    return this.getSavedJobs(freelancerId).pipe(
      map((jobs) => new Set(jobs.map((job) => job.id || 0).filter((id) => id > 0)))
    );
  }
}
