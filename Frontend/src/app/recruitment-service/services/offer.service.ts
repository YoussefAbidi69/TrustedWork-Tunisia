import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecruitmentOffer } from '../models';

@Injectable({ providedIn: 'root' })
export class OfferService {
    private baseUrl = 'http://localhost:8089/recruitment/offers';

    constructor(private http: HttpClient) { }

    // GET /offers
    getAll(): Observable<RecruitmentOffer[]> {
        return this.http.get<RecruitmentOffer[]>(this.baseUrl);
    }

    // GET /offers/{id}
    getById(id: number): Observable<RecruitmentOffer> {
        return this.http.get<RecruitmentOffer>(`${this.baseUrl}/${id}`);
    }

    // POST /offers
    create(offer: RecruitmentOffer): Observable<RecruitmentOffer> {
        return this.http.post<RecruitmentOffer>(this.baseUrl, offer);
    }

    // PUT /offers/{id}
    update(id: number, offer: RecruitmentOffer): Observable<RecruitmentOffer> {
        return this.http.put<RecruitmentOffer>(`${this.baseUrl}/${id}`, offer);
    }

    // DELETE /offers/{id}
    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    // GET /offers/application/{applicationId}
    getByApplication(applicationId: number): Observable<RecruitmentOffer> {
        return this.http.get<RecruitmentOffer>(`${this.baseUrl}/application/${applicationId}`);
    }

    // GET /offers/entreprise/{entrepriseId}
    getByEntreprise(entrepriseId: number): Observable<RecruitmentOffer[]> {
        return this.http.get<RecruitmentOffer[]>(`${this.baseUrl}/entreprise/${entrepriseId}`);
    }

    // PATCH /offers/{id}/status?status=ACCEPTED
    updateStatus(id: number, status: string): Observable<RecruitmentOffer> {
        const params = new HttpParams().set('status', status);
        return this.http.patch<RecruitmentOffer>(`${this.baseUrl}/${id}/status`, null, { params });
    }

    // PATCH /offers/{id}/contre-offre?contreOffre=xxx
    addContreOffre(id: number, contreOffre: string): Observable<RecruitmentOffer> {
        const params = new HttpParams().set('contreOffre', contreOffre);
        return this.http.patch<RecruitmentOffer>(`${this.baseUrl}/${id}/contre-offre`, null, { params });
    }

    getByStatus(status: string): Observable<RecruitmentOffer[]> {
        return this.http.get<RecruitmentOffer[]>(
            `${this.baseUrl}/status/${status}`
        );
    }
}