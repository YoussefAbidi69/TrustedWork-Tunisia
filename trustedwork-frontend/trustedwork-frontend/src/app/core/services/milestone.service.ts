import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Milestone } from '../models/milestone.model';

@Injectable({
  providedIn: 'root'
})
export class MilestoneService {

  private endpoint = 'milestones';

  constructor(private api: ApiService) {}

  getAll(page: number = 0, size: number = 10): Observable<any> {
    return this.api.get<any>(this.endpoint, { page, size });
  }

  getById(id: number): Observable<Milestone> {
    return this.api.get<Milestone>(`${this.endpoint}/${id}`);
  }

  getByContractId(contractId: number): Observable<Milestone[]> {
    return this.api.get<Milestone[]>(`${this.endpoint}/contract/${contractId}`);
  }

  create(milestone: Milestone): Observable<Milestone> {
    return this.api.post<Milestone>(this.endpoint, milestone);
  }

  update(id: number, milestone: Milestone): Observable<Milestone> {
    return this.api.put<Milestone>(`${this.endpoint}/${id}`, milestone);
  }

  updateStatus(id: number, status: string): Observable<Milestone> {
    return this.api.patch<Milestone>(`${this.endpoint}/${id}/status?status=${status}`);
  }

  start(id: number): Observable<Milestone> {
    return this.api.post<Milestone>(`${this.endpoint}/${id}/start`, {});
  }

  submit(id: number): Observable<Milestone> {
    return this.api.post<Milestone>(`${this.endpoint}/${id}/submit`, {});
  }

  approve(id: number): Observable<Milestone> {
    return this.api.post<Milestone>(`${this.endpoint}/${id}/approve`, {});
  }

  reject(id: number, reason: string): Observable<Milestone> {
    return this.api.post<Milestone>(`${this.endpoint}/${id}/reject?reason=${encodeURIComponent(reason)}`, {});
  }

  delete(id: number): Observable<void> {
    return this.api.delete<void>(`${this.endpoint}/${id}`);
  }
}