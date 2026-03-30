import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ContractService } from '../../../services/contract.service';
import { OfferService } from '../../../services/offer.service';
import { RecruitmentOffer } from '../../../models';
@Component({
  selector: 'app-contract-create',
  templateUrl: './contract-create.component.html',
  styleUrls: ['./contract-create.component.css']
})
export class ContractCreateComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  isEdit = false;
  editId: number | null = null;
  offers: RecruitmentOffer[] = [];
  typeOptions = ['CDI', 'CDD', 'CIVP', 'STAGE', 'ALTERNANCE'];
  statusOptions = ['DRAFT', 'SIGNED', 'ACTIVE', 'TERMINATED'];

  constructor(
    private fb: FormBuilder,
    private contractService: ContractService,
    private router: Router,
    private route: ActivatedRoute,
    private offerService: OfferService
  ) { }

  ngOnInit(): void {
    this.offerService.getByStatus('ACCEPTED').subscribe({
      next: (data) => {
        this.offers = data;
      }
    });
    this.form = this.fb.group({
      offerId: [null, [Validators.required, Validators.min(1)]],
      typeContrat: ['CDI', Validators.required],
      salaireFinal: [0, [Validators.required, Validators.min(0)]],
      dateDebutEffective: ['', Validators.required],
      periodeEssai: [3, [Validators.required, Validators.min(0)]],
      commissionPlateforme: [10, [Validators.required, Validators.min(0), Validators.max(100)]],
      status: ['DRAFT']
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.editId = Number(id);
      this.contractService.getById(this.editId).subscribe({
        next: (data) => {
          this.form.patchValue({
            ...data,
            dateDebutEffective: data.dateDebutEffective
              ? new Date(data.dateDebutEffective).toISOString().slice(0, 16) : ''
          });
        }
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    const payload = {
      ...this.form.value,
      freelancerId: 1,   // Phase 1 mock
      entrepriseId: 2    // Phase 1 mock
    };
    const req = this.isEdit
      ? this.contractService.update(this.editId!, payload)
      : this.contractService.create(payload);
    req.subscribe({
      next: () => { this.loading = false; this.router.navigate(['/recruitment/contracts']); },
      error: () => { this.loading = false; }
    });
  }

  get title(): string { return this.isEdit ? 'Modifier Contrat' : 'Nouveau Contrat'; }
  get submitLabel(): string { return this.isEdit ? 'Mettre à jour' : 'Créer Contrat'; }
}