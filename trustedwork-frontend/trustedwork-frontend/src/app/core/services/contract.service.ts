import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Contract } from '../models/contract.model';

@Injectable({
  providedIn: 'root'
})
export class ContractService {

  private endpoint = 'contracts';

  constructor(private api: ApiService) {}

  getAll(
    page: number = 0,
    size: number = 10,
    filters?: { userId?: number; freelancerId?: number }
  ): Observable<any> {
    // Backend filters results based on authenticated role; optional filters are for ADMIN usage.
    const params: any = { page, size, ...(filters || {}) };
    return this.api.get<any>(this.endpoint, params);
  }

  getById(id: number): Observable<Contract> {
    return this.api.get<Contract>(`${this.endpoint}/${id}`);
  }

  create(contract: Contract): Observable<Contract> {
    return this.api.post<Contract>(this.endpoint, contract);
  }

  update(id: number, contract: Contract): Observable<Contract> {
    return this.api.put<Contract>(`${this.endpoint}/${id}`, contract);
  }

  updateStatus(id: number, status: string): Observable<Contract> {
    return this.api.patch<Contract>(`${this.endpoint}/${id}/status?status=${status}`);
  }

  delete(id: number): Observable<void> {
    return this.api.delete<void>(`${this.endpoint}/${id}`);
  }
}
