import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TalentPoolService } from '../../../services/talent-pool.service';

@Component({
  selector: 'app-talent-pool-create',
  templateUrl: './talent-pool-create.component.html',
  styleUrls: ['./talent-pool-create.component.css']
})
export class TalentPoolCreateComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  isEdit = false;
  editId: number | null = null;

  tagOptions = ['WATCHLIST', 'FAVORITE', 'CONTACTED'];
  sourceOptions = ['SEARCH', 'HACKATHON', 'RECOMMENDATION'];

  constructor(
    private fb: FormBuilder,
    private talentPoolService: TalentPoolService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      freelancerId: [null, [Validators.required, Validators.min(1)]],
      entrepriseId: [null, [Validators.required, Validators.min(1)]],
      tag: ['WATCHLIST', Validators.required],
      sourceOrigine: ['SEARCH', Validators.required],
      alerteDisponibilite: [false],
      notesPrivees: ['']
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.editId = Number(id);
      this.talentPoolService.getById(this.editId).subscribe({
        next: (data) => this.form.patchValue(data)
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    const payload = this.form.value;

    const req = this.isEdit
      ? this.talentPoolService.update(this.editId!, payload)
      : this.talentPoolService.add(payload);

    req.subscribe({
      next: () => { this.loading = false; this.router.navigate(['/recruitment/talent-pool']); },
      error: () => { this.loading = false; }
    });
  }

  get title(): string { return this.isEdit ? 'Modifier Talent' : 'Ajouter au Vivier'; }
  get submitLabel(): string { return this.isEdit ? 'Mettre à jour' : 'Ajouter au Vivier'; }
}