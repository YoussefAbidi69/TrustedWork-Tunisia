import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { OfferService } from '../../../services/offer.service';
import { InterviewService } from '../../../services/interview.service';
import { InterviewSchedule } from '../../../models';

import { ApplicationService } from '../../../services/application.service';
import { RecruitmentApplication } from '../../../models';
@Component({
  selector: 'app-offer-create',
  templateUrl: './offer-create.component.html',
  styleUrls: ['./offer-create.component.css']
})
export class OfferCreateComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  isEdit = false;
  editId: number | null = null;
  interviews: InterviewSchedule[] = [];
  applications: RecruitmentApplication[] = [];
  statusOptions = ['SENT', 'ACCEPTED', 'DECLINED', 'NEGOTIATING'];

  constructor(
    private fb: FormBuilder,
    private offerService: OfferService,
    private router: Router,
    private route: ActivatedRoute,
    private applicationService: ApplicationService
  ) { }

  ngOnInit(): void {

    this.applicationService
      .getEligibleForOffer()
      .subscribe({
        next: (data) => {
          this.applications = data;
          console.log(this.applications);
        }
      });
    this.form = this.fb.group({
      applicationId: [null, [Validators.required, Validators.min(1)]],
      posteExact: ['', Validators.required],
      salairePropose: [0, [Validators.required, Validators.min(0)]],
      periodeEssaiMois: [3, [Validators.required, Validators.min(0)]],
      dateDebutSouhaitee: ['', Validators.required],
      deadlineReponse: ['', Validators.required],
      avantages: [''],
      status: ['SENT']
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.editId = Number(id);
      this.offerService.getById(this.editId).subscribe({
        next: (data) => {
          this.form.patchValue({
            ...data,
            dateDebutSouhaitee: data.dateDebutSouhaitee
              ? new Date(data.dateDebutSouhaitee).toISOString().slice(0, 16) : '',
            deadlineReponse: data.deadlineReponse
              ? new Date(data.deadlineReponse).toISOString().slice(0, 16) : ''
          });
        }
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    const req = this.isEdit
      ? this.offerService.update(this.editId!, this.form.value)
      : this.offerService.create(this.form.value);
    req.subscribe({
      next: () => { this.loading = false; this.router.navigate(['/recruitment/offers']); },
      error: () => { this.loading = false; }
    });
  }

  get title(): string { return this.isEdit ? 'Modifier Offre' : 'Nouvelle Offre'; }
  get submitLabel(): string { return this.isEdit ? 'Mettre à jour' : 'Envoyer Offre'; }
}