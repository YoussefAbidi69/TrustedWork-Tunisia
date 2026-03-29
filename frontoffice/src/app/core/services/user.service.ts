import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserDTO {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  bio?: string | null;
  role: string;
  accountStatus: string;
  kycStatus: string;
  twoFactorEnabled: boolean;
  createdAt?: string;
}

export interface UpdateProfileRequest {
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  bio?: string;
}

export interface KycSubmitRequest {
  cinNumber: string;
  cinDocumentPath: string;
  diplomaDocumentPath?: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly apiUrl = environment.api.userBaseUrl;

  constructor(private readonly http: HttpClient) {}

  getMyProfile(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/users/me`);
  }

  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/users/${id}`);
  }

  updateProfile(id: number, payload: UpdateProfileRequest): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.apiUrl}/users/${id}`, payload);
  }

  submitKyc(userId: number, payload: KycSubmitRequest): Observable<UserDTO> {
    return this.http.post<UserDTO>(`${this.apiUrl}/kyc/submit/${userId}`, payload);
  }

  getKycStatus(userId: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/kyc/status/${userId}`);
  }
}
