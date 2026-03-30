import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RecruitmentLayoutComponent } from './components/recruitment-layout/recruitment-layout.component';
import { RecruitmentDashboardComponent } from './pages/dashboard/recruitment-dashboard.component';
import { JobPositionListComponent } from './pages/job-positions/job-position-list/job-position-list.component';
import { JobPositionCreateComponent } from './pages/job-positions/job-position-create/job-position-create.component';
import { JobPositionDetailComponent } from './pages/job-positions/job-position-detail/job-position-detail.component';
import { JobPositionPublishedComponent } from './pages/job-positions/job-position-published/job-position-published.component';
import { JobPositionByEntrepriseComponent } from './pages/job-positions/job-position-by-entreprise/job-position-by-entreprise.component';
import { ApplicationListComponent } from './pages/applications/application-list/application-list.component';
import { ApplicationDetailComponent } from './pages/applications/application-detail/application-detail.component';
import { ApplicationCreateComponent } from './pages/applications/application-create/application-create.component';
import { ApplicationByJobComponent } from './pages/applications/application-by-job/application-by-job.component';
import { InterviewListComponent } from './pages/interviews/interview-list/interview-list.component';
import { InterviewCreateComponent } from './pages/interviews/interview-create/interview-create.component';
import { InterviewDetailComponent } from './pages/interviews/interview-detail/interview-detail.component';

import { OfferListComponent } from './pages/offers/offer-list/offer-list.component';
import { OfferDetailComponent } from './pages/offers/offer-detail/offer-detail.component';
import { OfferByApplicationComponent } from './pages/offers/offer-by-application/offer-by-application.component';
import { OfferByEntrepriseComponent } from './pages/offers/offer-by-entreprise/offer-by-entreprise.component';
import { OfferCreateComponent } from './pages/offers/offer-create/offer-create.component';
import { ContractListComponent } from './pages/contracts/contract-list/contract-list.component';
import { ContractDetailComponent } from './pages/contracts/contract-detail/contract-detail.component';
import { ContractCreateComponent } from './pages/contracts/contract-create/contract-create.component';
import { ContractByOfferComponent } from './pages/contracts/contract-by-offer/contract-by-offer.component';
import { ContractByFreelancerComponent } from './pages/contracts/contract-by-freelancer/contract-by-freelancer.component';
import { ContractByEntrepriseComponent } from './pages/contracts/contract-by-entreprise/contract-by-entreprise.component';
import { TalentPoolListComponent } from './pages/talent-pool/talent-pool-list/talent-pool-list.component';
import { TalentPoolCreateComponent } from './pages/talent-pool/talent-pool-create/talent-pool-create.component';
import { TalentPoolDetailComponent } from './pages/talent-pool/talent-pool-detail/talent-pool-detail.component';


const routes: Routes = [
  {
    path: '',
    component: RecruitmentLayoutComponent,
    children: [
      { path: '', component: RecruitmentDashboardComponent },
      { path: 'job-positions', component: JobPositionListComponent },
      { path: 'job-positions/create', component: JobPositionCreateComponent },
      { path: 'job-positions/published', component: JobPositionPublishedComponent },
      {
        path: 'job-positions/entreprise',
        component: JobPositionByEntrepriseComponent
      },
      { path: 'job-positions/:id/edit', component: JobPositionCreateComponent },
      { path: 'job-positions/:id', component: JobPositionDetailComponent },

      { path: 'applications', component: ApplicationListComponent },
      { path: 'applications/create', component: ApplicationCreateComponent },
      { path: 'applications/by-job', component: ApplicationByJobComponent },
      { path: 'applications/:id/edit', component: ApplicationCreateComponent },
      { path: 'applications/:id', component: ApplicationDetailComponent },


      { path: 'interviews', component: InterviewListComponent },
      { path: 'interviews/create', component: InterviewCreateComponent },
      { path: 'interviews/:id/edit', component: InterviewCreateComponent },
      { path: 'interviews/:id', component: InterviewDetailComponent },

      { path: 'offers', component: OfferListComponent },
      { path: 'offers/create', component: OfferCreateComponent },
      { path: 'offers/by-application', component: OfferByApplicationComponent },
      { path: 'offers/by-entreprise', component: OfferByEntrepriseComponent },
      { path: 'offers/:id/edit', component: OfferCreateComponent },
      { path: 'offers/:id', component: OfferDetailComponent },


      { path: 'contracts', component: ContractListComponent },
      { path: 'contracts/create', component: ContractCreateComponent },
      { path: 'contracts/by-offer', component: ContractByOfferComponent },
      { path: 'contracts/by-freelancer', component: ContractByFreelancerComponent },
      { path: 'contracts/by-entreprise', component: ContractByEntrepriseComponent },
      { path: 'contracts/:id/edit', component: ContractCreateComponent },
      { path: 'contracts/:id', component: ContractDetailComponent },


      { path: 'talent-pool', component: TalentPoolListComponent },
      { path: 'talent-pool/create', component: TalentPoolCreateComponent },
      { path: 'talent-pool/:id/edit', component: TalentPoolCreateComponent },
      { path: 'talent-pool/:id', component: TalentPoolDetailComponent },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RecruitmentServiceRoutingModule { }
