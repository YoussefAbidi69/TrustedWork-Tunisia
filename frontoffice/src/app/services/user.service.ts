import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// URL vers le dossier htdocs/images (XAMPP/WAMP)
export const IMAGES_BASE_URL = 'http://localhost/images';

export interface UserDTO {
  id: number;
  cin: number;              // ← AJOUT
  firstName: string;
  lastName: string;
  email: string;
  phone: string;            // ← AJOUT (backend renvoie "phone")
  photo: string | null;     // ← AJOUT : nom du fichier uniquement
  role: string;
  accountStatus: string;
  kycStatus: string;
  twoFactorEnabled: boolean;
  createdAt: string;
}

export interface UpdateProfileRequest {
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  photo?: string | null;    // ← AJOUT : nom du fichier image
}

@Injectable({ providedIn: 'root' })
export class UserService {

  private apiUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  // ================= HELPERS =================

  /**
   * Convertit le nom de fichier stocké en DB en URL complète vers htdocs/images.
   * Retourne null si la photo est absente (le template affichera les initiales).
   */
  getPhotoUrl(photoName: string | null | undefined): string | null {
    if (!photoName || photoName.trim() === '') return null;
    return `${IMAGES_BASE_URL}/${photoName}`;
  }

  // ================= PROFILE =================

  getMyProfile(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/users/me`);
  }

  /**
   * Le backend attend le CIN comme identifiant dans l'URL (PUT /users/{cin}).
   */
  updateProfile(cin: number, data: UpdateProfileRequest): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.apiUrl}/users/${cin}`, data);
  }

  // ================= KYC =================

  submitKyc(cin: number, data: any): Observable<UserDTO> {
    return this.http.post<UserDTO>(`${this.apiUrl}/kyc/submit/${cin}`, data);
  }

  getKycStatus(cin: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/kyc/status/${cin}`);
  }
}