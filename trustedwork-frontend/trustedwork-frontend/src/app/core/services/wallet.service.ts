import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Wallet {
  id: number;
  userId: number;
  balance: number;
  totalEarned: number;
  totalSpent: number;
  totalCommissionPaid: number;
  stripeAccountId: string;
  stripeAccountStatus: string;
}

@Injectable({
  providedIn: 'root'
})
export class WalletService {
  private apiUrl = 'http://localhost:8083/api/v1/wallets';

  constructor(private http: HttpClient) {}

  getWallet(userId: number): Observable<Wallet> {
    return this.http.get<Wallet>(`${this.apiUrl}/user/${userId}`);
  }

  credit(userId: number, amount: number): Observable<Wallet> {
    return this.http.post<Wallet>(`${this.apiUrl}/user/${userId}/credit`, null, {
      params: { amount: amount.toString() }
    });
  }

  createStripeAccount(userId: number, email: string, country: string = 'FR'): Observable<any> {
    return this.http.post(`${this.apiUrl}/stripe/connect/${userId}`, null, {
      params: { email, country }
    });
  }

  getStripeStatus(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/stripe/status/${userId}`);
  }

  getOnboardingLink(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/stripe/onboarding/${userId}`);
  }
}