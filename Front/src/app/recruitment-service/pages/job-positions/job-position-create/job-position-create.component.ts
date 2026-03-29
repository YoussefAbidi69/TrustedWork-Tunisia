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
            status: ['DRAFT'],
            entrepriseId: [101],
            nombreCandidatures: [0]  // ← champ ajouté
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

    // ✅ Bouton "Générer avec IA" — appelle POST /job-positions/{id}/generate-description
    generateDescription(): void {
        if (!this.editId) {
            alert('Sauvegardez d\'abord l\'offre pour pouvoir générer une description avec l\'IA.');
            return;
        }
        this.aiLoading = true;
        this.jobPositionService.generateDescription(this.editId).subscribe({
            next: (result: any) => {
                // Le backend renvoie la description générée
                const desc = typeof result === 'string' ? result : result.description ?? result;
                this.form.patchValue({ description: desc });
                this.aiLoading = false;
            },
            error: () => {
                this.aiLoading = false;
                alert('Erreur lors de la génération IA. Vérifiez que le backend est actif.');
            }
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