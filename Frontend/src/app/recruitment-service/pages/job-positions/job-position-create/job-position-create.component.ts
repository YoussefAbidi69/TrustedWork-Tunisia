import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { JobPositionService } from '../../../services/job-position.service';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-job-position-create',
    templateUrl: './job-position-create.component.html',
    styleUrls: ['./job-position-create.component.css']
})
export class JobPositionCreateComponent implements OnInit {
    form!: FormGroup;
    loading = false;
    aiLoading = false;       // ← loading pour le bouton IA
    isEditMode = false;      // ← savoir si on est en mode édition
    editId: number | null = null;

    // ✅ CIVP remplace FREELANCE (correct selon le cahier des charges)
    contractTypes = ['CDI', 'CDD', 'CIVP', 'STAGE', 'ALTERNANCE'];

    statuses = ['DRAFT', 'PUBLISHED', 'PAUSED', 'CLOSED', 'FILLED'];

    constructor(
        private fb: FormBuilder,
        private jobPositionService: JobPositionService,
        private router: Router,
        private route: ActivatedRoute
    ) { }

    ngOnInit(): void {
        this.form = this.fb.group({
            titre: ['', Validators.required],
            description: ['', Validators.required],
            typeContrat: ['CDI', Validators.required],
            salaireMin: [0],
            salaireMax: [0],
            localisation: [''],
            remote: [false],
            experienceRequiseAns: [0],
            skillsRequis: [''],
            deadline: [''],
            status: ['PUBLISHED'],
            entrepriseId: [2],
            nombreCandidatures: [0]  // ← champ ajouté
        });

        this.form.get('deadline')?.valueChanges.subscribe(value => {

            if (value) {
                this.generateDescription();
            }

        });

        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.isEditMode = true;
            this.editId = Number(id);
            this.jobPositionService.getById(this.editId).subscribe(data => {
                this.form.patchValue(data);
            });
        }
    }

    callAI(): void {

        if (!this.editId) return;

        this.aiLoading = true;

        this.jobPositionService.generateDescription(this.editId).subscribe({
            next: (result: any) => {

                const desc = typeof result === 'string'
                    ? result
                    : result.description ?? result;

                this.form.patchValue({
                    description: desc
                });

                this.aiLoading = false;
            },
            error: () => {
                this.aiLoading = false;
                alert("Erreur génération IA");
            }
        });

    }

    // ✅ Bouton "Générer avec IA" — appelle POST /job-positions/{id}/generate-description
    generateDescription(): void {

        // si le job existe déjà
        if (this.editId) {
            this.callAI();
            return;
        }

        // créer le job automatiquement
        this.jobPositionService.create(this.form.value).subscribe((job: any) => {

            this.editId = job.id;
            this.isEditMode = true;

            this.callAI();

        });

    }

    onSubmit(): void {
        if (this.form.invalid) return;
        this.loading = true;

        if (this.isEditMode && this.editId) {
            this.jobPositionService.update(this.editId, this.form.value)
                .subscribe(() => {
                    this.loading = false;
                    this.router.navigate(['/recruitment/job-positions']);
                });
        } else {
            this.jobPositionService.create(this.form.value)
                .subscribe(() => {
                    this.loading = false;
                    this.router.navigate(['/recruitment/job-positions']);
                });
        }
    }
}