import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HiringContract } from '../models';

@Injectable({ providedIn: 'root' })
export class ContractService {
    private baseUrl = 'http://localhost:8089/recruitment/contracts';

    constructor(private http: HttpClient) { }

    // GET /contracts
    getAll(): Observable<HiringContract[]> {
        return this.http.get<HiringContract[]>(this.baseUrl);
    }

    // GET /contracts/{id}
    getById(id: number): Observable<HiringContract> {
        return this.http.get<HiringContract>(`${this.baseUrl}/${id}`);
    }

    // POST /contracts
    create(contract: HiringContract): Observable<HiringContract> {
        return this.http.post<HiringContract>(this.baseUrl, contract);
    }

    // PUT /contracts/{id}
    update(id: number, contract: HiringContract): Observable<HiringContract> {
        return this.http.put<HiringContract>(`${this.baseUrl}/${id}`, contract);
    }

    // DELETE /contracts/{id}
    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    // GET /contracts/offer/{offerId}
    getByOffer(offerId: number): Observable<HiringContract> {
        return this.http.get<HiringContract>(`${this.baseUrl}/offer/${offerId}`);
    }

    // GET /contracts/freelancer/{freelancerId}
    getByFreelancer(freelancerId: number): Observable<HiringContract[]> {
        return this.http.get<HiringContract[]>(`${this.baseUrl}/freelancer/${freelancerId}`);
    }

    // GET /contracts/entreprise/{entrepriseId}
    getByEntreprise(entrepriseId: number): Observable<HiringContract[]> {
        return this.http.get<HiringContract[]>(`${this.baseUrl}/entreprise/${entrepriseId}`);
    }

    // PATCH /contracts/{id}/signer
    signerContrat(id: number): Observable<HiringContract> {
        return this.http.patch<HiringContract>(`${this.baseUrl}/${id}/signer`, null);
    }

    // PATCH /contracts/{id}/feedback?feedback=xxx
    addFeedback(id: number, feedback: string): Observable<HiringContract> {
        const params = new HttpParams().set('feedback', feedback);
        return this.http.patch<HiringContract>(`${this.baseUrl}/${id}/feedback`, null, { params });
    }
}