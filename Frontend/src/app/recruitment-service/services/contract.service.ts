import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HiringContract } from '../models';

@Injectable({
    providedIn: 'root'
})
export class ContractService {
    private baseUrl = 'http://localhost:8089/recruitment/contracts';

    constructor(private http: HttpClient) { }

    getAll(): Observable<HiringContract[]> {
        return this.http.get<HiringContract[]>(this.baseUrl);
    }

    getById(id: number): Observable<HiringContract> {
        return this.http.get<HiringContract>(`${this.baseUrl}/${id}`);
    }

    create(contract: HiringContract): Observable<HiringContract> {
        return this.http.post<HiringContract>(this.baseUrl, contract);
    }

    update(id: number, contract: HiringContract): Observable<HiringContract> {
        return this.http.put<HiringContract>(`${this.baseUrl}/${id}`, contract);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
