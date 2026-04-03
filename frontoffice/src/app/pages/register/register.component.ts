import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  form: FormGroup;
  error = '';
  loading = false;

  // Gestion de la photo
  selectedPhotoFile: File | null = null;
  photoPreviewUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      cin:         [null, [Validators.required, Validators.min(10000000), Validators.max(99999999)]],
      firstName:   ['', [Validators.required, Validators.minLength(2)]],
      lastName:    ['', [Validators.required, Validators.minLength(2)]],
      email:       ['', [Validators.required, Validators.email]],
      password:    ['', [Validators.required, Validators.minLength(8)]],
      phoneNumber: [''],
      photo:       [null],      // ← nom du fichier uniquement
      role:        ['FREELANCER', Validators.required]
    });
  }

  // ─── Gestion de la photo ──────────────────────────────────────────────────

  onPhotoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    this.selectedPhotoFile = file;

    // Prévisualisation locale avant envoi
    const reader = new FileReader();
    reader.onload = (e) => {
      this.photoPreviewUrl = e.target?.result as string;
    };
    reader.readAsDataURL(file);

    // Seul le nom du fichier est stocké en base de données
    this.form.patchValue({ photo: file.name });
  }

  removePhoto(): void {
    this.selectedPhotoFile = null;
    this.photoPreviewUrl = null;
    this.form.patchValue({ photo: null });
  }

  // ─── Soumission ───────────────────────────────────────────────────────────

  submit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    this.error = '';

    this.auth.register(this.form.value).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.error || err.error?.message || 'Erreur lors de l\'inscription';
      }
    });
  }
}