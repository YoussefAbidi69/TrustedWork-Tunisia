import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { InterviewService } from '../../../services/interview.service';
import { ApplicationService } from '../../../services/application.service';
import { RecruitmentApplication } from '../../../models';
@Component({
  selector: 'app-interview-create',
  templateUrl: './interview-create.component.html',
  styleUrls: ['./interview-create.component.css']
})
export class InterviewCreateComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  isEdit = false;
  editId: number | null = null;
  applications: RecruitmentApplication[] = [];
  // Vrais types et statuts backend
  typeOptions = ['PHONE', 'VIDEO', 'ONSITE', 'TECHNICAL_TEST'];
  statusOptions = ['PROPOSED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'RESCHEDULED'];

  constructor(
    private fb: FormBuilder,
    private interviewService: InterviewService,
    private applicationService: ApplicationService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.applicationService
      .getByStatuses(['SUBMITTED', 'SHORTLISTED'])
      .subscribe({
        next: data => {
          this.applications = data;
        }
      });
    this.form = this.fb.group({
      applicationId: [null, [Validators.required, Validators.min(1)]],
      type: ['VIDEO', Validators.required],
      ordreEntretien: [1, [Validators.required, Validators.min(1)]],
      dateFinalConfirmee: ['', Validators.required],
      dureePrevueMinutes: [60, [Validators.required, Validators.min(15)]],
      lienVisio: [''],
      status: ['PROPOSED', Validators.required]
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.editId = Number(id);
      this.interviewService.getById(this.editId).subscribe({
        next: (data) => {
          const formatted = {
            ...data,
            dateFinalConfirmee: data.dateFinalConfirmee
              ? new Date(data.dateFinalConfirmee).toISOString().slice(0, 16)
              : ''
          };
          this.form.patchValue(formatted);
        }
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    const payload = this.form.value;

    const req = this.isEdit
      ? this.interviewService.update(this.editId!, payload)
      : this.interviewService.create(payload);

    req.subscribe({
      next: () => { this.loading = false; this.router.navigate(['/recruitment/interviews']); },
      error: () => { this.loading = false; }
    });
  }

  get title(): string { return this.isEdit ? 'Modifier Entretien' : 'Planifier Entretien'; }
  get subtitle(): string { return this.isEdit ? 'Mettre à jour les détails' : 'Nouvelle session d\'entretien'; }
  get submitLabel(): string { return this.isEdit ? 'Mettre à jour' : 'Planifier'; }
}