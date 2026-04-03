import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService, UserDTO } from '../../services/user.service';

@Component({
  selector: 'app-kyc-submit',
  templateUrl: './kyc-submit.component.html',
  styleUrls: ['./kyc-submit.component.css']
})
export class KycSubmitComponent implements OnInit {

  form: FormGroup;
  user: UserDTO | null = null;
  loading = true;
  submitting = false;
  success = '';
  error = '';

  constructor(
    private fb: FormBuilder,
    private userService: UserService
  ) {
    this.form = this.fb.group({
      // Le CIN est pré-rempli depuis le profil (lecture seule dans le template)
      cinNumber:          ['', [Validators.required, Validators.minLength(8)]],
      cinDocumentPath:    ['', Validators.required],
      diplomaDocumentPath: ['']
    });
  }

  ngOnInit(): void {
    this.loadStatus();
  }

  loadStatus(): void {
    this.loading = true;
    this.userService.getMyProfile().subscribe({
      next: (data) => {
        this.user = data;
        // Pré-remplir le champ cinNumber avec le CIN du profil
        this.form.patchValue({ cinNumber: String(data.cin) });
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });
  }

  submit(): void {
    if (this.form.invalid || !this.user) return;

    this.submitting = true;
    this.error = '';
    this.success = '';

    // Le backend attend le CIN dans l'URL (POST /kyc/submit/{cin})
    this.userService.submitKyc(this.user.cin, this.form.value).subscribe({
      next: () => {
        this.submitting = false;
        this.success = 'KYC soumis avec succès ! En attente de validation.';
        this.loadStatus();
      },
      error: (err) => {
        this.submitting = false;
        this.error = err.error?.error || err.error?.message || 'Erreur lors de la soumission';
      }
    });
  }

  canSubmit(): boolean {
    return this.user?.kycStatus === 'PENDING' || this.user?.kycStatus === 'REJECTED';
  }
}