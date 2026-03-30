import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationService } from '../../../services/application.service';
import { JobPositionService } from '../../../services/job-position.service';
import { JobPosition } from '../../../models';
@Component({
    selector: 'app-application-create',
    templateUrl: './application-create.component.html',
    styleUrls: ['./application-create.component.css']
})
export class ApplicationCreateComponent implements OnInit {
    form!: FormGroup;
    loading = false;
    isEdit = false;
    editId: number | null = null;
    jobPositions: any[] = [];
    // Vrais statuts du backend ApplicationStatus enum
    statusOptions = ['SUBMITTED', 'REVIEWED', 'SHORTLISTED', 'INTERVIEW', 'OFFERED', 'HIRED', 'REJECTED', 'PENDING'];

    constructor(
        private fb: FormBuilder,
        private applicationService: ApplicationService,
        private router: Router,
        private route: ActivatedRoute,
        private jobPositionService: JobPositionService
    ) { }

    ngOnInit(): void {
        this.form = this.fb.group({
            jobPositionId: [null, [Validators.required, Validators.min(1)]],
            lettreMotivation: ['', [Validators.required, Validators.minLength(50)]],
            pretentionSalariale: [0, [Validators.required, Validators.min(0)]],
            disponibilite: ['', Validators.required],
            status: ['SUBMITTED']
        });

        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.isEdit = true;
            this.editId = Number(id);
            this.applicationService.getById(this.editId).subscribe({
                next: (data) => this.form.patchValue(data)
            });
        }
        this.jobPositionService.getPublished().subscribe({
            next: (data) => {
                this.jobPositions = data;
            }
        });
    }

    onSubmit(): void {
        if (this.form.invalid) return;
        this.loading = true;

        const payload = {
            ...this.form.value,
            freelancerId: 1,    // Phase 1 mock
            entrepriseId: 2,    // Phase 1 mock
            matchingScore: 75   // Phase 1 mock
        };

        const req = this.isEdit
            ? this.applicationService.update(this.editId!, payload)
            : this.applicationService.create(payload);

        req.subscribe({
            next: () => { this.loading = false; this.router.navigate(['/recruitment/applications']); },
            error: () => { this.loading = false; }
        });
    }

    get title(): string { return this.isEdit ? 'Modifier Candidature' : 'Nouvelle Candidature'; }
    get subtitle(): string { return this.isEdit ? 'Mettre à jour les détails' : 'Postuler à un poste'; }
    get submitLabel(): string { return this.isEdit ? 'Mettre à jour' : 'Soumettre'; }
}