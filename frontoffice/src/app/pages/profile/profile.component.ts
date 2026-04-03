import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { UserService, UserDTO } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  user: UserDTO | null = null;
  editForm: FormGroup;
  passwordForm: FormGroup;

  editing = false;
  loading = true;

  saveSuccess = '';
  saveError = '';

  passwordSuccess = '';
  passwordError = '';
  passwordLoading = false;

  // Gestion de la photo
  selectedPhotoFile: File | null = null;
  photoPreviewUrl: string | null = null;

  constructor(
    private userService: UserService,
    private auth: AuthService,
    private fb: FormBuilder
  ) {
    this.editForm = this.fb.group({
      firstName:   [''],
      lastName:    [''],
      phoneNumber: [''],
      photo:       [null]   // ← AJOUT : nom du fichier
    });

    this.passwordForm = this.fb.group({
      currentPassword: [''],
      newPassword:     ['']
    });
  }

  ngOnInit(): void {
    this.loadProfile();
  }

  // ─── Chargement du profil ─────────────────────────────────────────────────

  loadProfile(): void {
    this.loading = true;
    this.userService.getMyProfile().subscribe({
      next: (data) => {
        this.user = data;
        this.editForm.patchValue({
          firstName:   data.firstName,
          lastName:    data.lastName,
          phoneNumber: data.phone || '',   // backend renvoie "phone"
          photo:       data.photo || null
        });
        // Prévisualisation de la photo existante
        this.photoPreviewUrl = this.userService.getPhotoUrl(data.photo);
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  // ─── Helpers photo ────────────────────────────────────────────────────────

  getPhotoUrl(photoName: string | null | undefined): string | null {
    return this.userService.getPhotoUrl(photoName);
  }

  onPhotoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    this.selectedPhotoFile = file;

    const reader = new FileReader();
    reader.onload = (e) => {
      this.photoPreviewUrl = e.target?.result as string;
    };
    reader.readAsDataURL(file);

    // Seul le nom du fichier est envoyé au backend
    this.editForm.patchValue({ photo: file.name });
  }

  removePhoto(): void {
    this.selectedPhotoFile = null;
    this.photoPreviewUrl = null;
    this.editForm.patchValue({ photo: null });
  }

  // ─── Édition ─────────────────────────────────────────────────────────────

  toggleEdit(): void {
    this.editing = !this.editing;
    this.saveSuccess = '';
    this.saveError = '';
    if (!this.editing) {
      // Annulation : on réinitialise la prévisualisation
      this.photoPreviewUrl = this.userService.getPhotoUrl(this.user?.photo);
      this.selectedPhotoFile = null;
    }
  }

  saveProfile(): void {
    if (!this.user) return;
    this.saveSuccess = '';
    this.saveError = '';

    // Le backend attend le CIN dans l'URL
    this.userService.updateProfile(this.user.cin, this.editForm.value).subscribe({
      next: (updated) => {
        this.user = updated;
        this.editing = false;
        this.photoPreviewUrl = this.userService.getPhotoUrl(updated.photo);
        this.saveSuccess = 'Profil mis à jour avec succès';
        setTimeout(() => this.saveSuccess = '', 3000);
      },
      error: (err: any) => {
        this.saveError = err?.error?.message || 'Erreur lors de la mise à jour du profil.';
      }
    });
  }

  // ─── Mot de passe ─────────────────────────────────────────────────────────

  changePassword(): void {
    this.passwordSuccess = '';
    this.passwordError = '';

    const { currentPassword, newPassword } = this.passwordForm.value;
    if (!currentPassword || !newPassword) {
      this.passwordError = 'Veuillez remplir tous les champs du mot de passe.';
      return;
    }

    this.passwordLoading = true;
    this.auth.changePassword(currentPassword, newPassword).subscribe({
      next: (res: any) => {
        this.passwordLoading = false;
        this.passwordSuccess = res?.message || 'Mot de passe modifié avec succès.';
        this.passwordForm.reset();
        setTimeout(() => this.passwordSuccess = '', 3000);
      },
      error: (err: any) => {
        this.passwordLoading = false;
        this.passwordError = err?.error?.message || 'Erreur lors du changement du mot de passe.';
      }
    });
  }

  // ─── Helpers couleurs ─────────────────────────────────────────────────────

  getStatusColor(status: string): string {
    const map: Record<string, string> = {
      'ACTIVE': '#34D399', 'SUSPENDED': '#F87171', 'DELETED': '#9496a6',
      'PENDING': '#FBBF24', 'IN_REVIEW': '#A78BFA',
      'APPROVED': '#34D399', 'REJECTED': '#F87171'
    };
    return map[status] || '#9496a6';
  }
}