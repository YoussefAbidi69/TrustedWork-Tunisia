import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RecruitmentOffer } from '../models';

@Injectable({
    providedIn: 'root'
})
export class OfferService {
    private baseUrl = 'http://localhost:8089/recruitment/offers';

    constructor(private http: HttpClient) { }

    getAll(): Observable<RecruitmentOffer[]> {
        return this.http.get<RecruitmentOffer[]>(this.baseUrl);
    }

    getById(id: number): Observable<RecruitmentOffer> {
        return this.http.get<RecruitmentOffer>(`${this.baseUrl}/${id}`);
    }

    create(offer: RecruitmentOffer): Observable<RecruitmentOffer> {
        return this.http.post<RecruitmentOffer>(this.baseUrl, offer);
    }

    update(id: number, offer: RecruitmentOffer): Observable<RecruitmentOffer> {
        return this.http.put<RecruitmentOffer>(`${this.baseUrl}/${id}`, offer);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
