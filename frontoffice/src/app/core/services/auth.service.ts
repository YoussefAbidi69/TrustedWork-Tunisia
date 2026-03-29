import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface LoginRequest {
  email: string;
  password: string;
  twoFactorCode?: string;
  rememberMe?: boolean;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  phoneNumber: string;
  email: string;
  password: string;
  role: 'CLIENT' | 'FREELANCER';
}

export interface AuthResponse {
  accessToken?: string;
  refreshToken?: string;
  tokenType?: string;
  userId?: number;
  email?: string;
  role?: string;
  twoFactorRequired?: boolean;
  message?: string;
}

export interface ForgotPasswordResponse {
  message: string;
  token?: string;
}

export interface BasicMessageResponse {
  message: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = environment.api.userBaseUrl;

  constructor(private readonly http: HttpClient) {}

  login(payload: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/login`, payload)
      .pipe(tap((res) => this.storeSession(res, false)));
  }

  register(payload: RegisterRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/register`, payload)
      .pipe(tap((res) => this.storeSession(res, true)));
  }

  forgotPassword(email: string): Observable<ForgotPasswordResponse> {
    return this.http.post<ForgotPasswordResponse>(`${this.apiUrl}/auth/forgot-password`, { email });
  }

  resetPassword(token: string, newPassword: string): Observable<BasicMessageResponse> {
    return this.http.post<BasicMessageResponse>(`${this.apiUrl}/auth/reset-password`, {
      token,
      newPassword
    });
  }

  refreshToken(refreshToken: string): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.apiUrl}/auth/refresh`, { refreshToken })
      .pipe(tap((res) => this.storeSession(res, false)));
  }

  changePassword(currentPassword: string, newPassword: string): Observable<BasicMessageResponse> {
    return this.http.put<BasicMessageResponse>(`${this.apiUrl}/auth/change-password`, {
      currentPassword,
      newPassword
    });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userId');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getEmail(): string {
    return localStorage.getItem('userEmail') ?? '';
  }

  getUserId(): number | null {
    const raw = localStorage.getItem('userId');
    if (!raw) {
      return null;
    }
    const parsed = Number(raw);
    return Number.isFinite(parsed) ? parsed : null;
  }

  getRole(): string {
    return localStorage.getItem('userRole') ?? '';
  }

  private storeSession(res: AuthResponse, clearOnChallenge: boolean): void {
    if (res.twoFactorRequired || !res.accessToken) {
      if (clearOnChallenge) {
        this.logout();
      }
      return;
    }

    localStorage.setItem('token', res.accessToken);
    localStorage.setItem('userEmail', res.email ?? '');
    localStorage.setItem('userRole', res.role ?? '');
    if (res.userId != null) {
      localStorage.setItem('userId', String(res.userId));
    }
    if (res.refreshToken) {
      localStorage.setItem('refreshToken', res.refreshToken);
    }
  }
}
