import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { RecruitmentServiceRoutingModule } from './recruitment-service-routing.module';

// ── Layout ───────────────────────────────────────────────────────────────────
import { RecruitmentSidebarComponent } from './components/recruitment-sidebar/recruitment-sidebar.component';
import { RecruitmentNavbarComponent } from './components/recruitment-navbar/recruitment-navbar.component';
import { RecruitmentLayoutComponent } from './components/recruitment-layout/recruitment-layout.component';

// ── Shared ───────────────────────────────────────────────────────────────────
import { RecruitmentCardComponent } from './shared/recruitment-card/recruitment-card.component';
import { RecruitmentTableComponent } from './shared/recruitment-table/recruitment-table.component';
import { StatusBadgeComponent } from './shared/status-badge/status-badge.component';
import { ButtonPrimaryComponent } from './shared/button-primary/button-primary.component';

// ── Dashboard ─────────────────────────────────────────────────────────────────
import { RecruitmentDashboardComponent } from './pages/dashboard/recruitment-dashboard.component';

// ── Job Positions ─────────────────────────────────────────────────────────────
import { JobPositionListComponent } from './pages/job-positions/job-position-list/job-position-list.component';
import { JobPositionCreateComponent } from './pages/job-positions/job-position-create/job-position-create.component';
import { JobPositionDetailComponent } from './pages/job-positions/job-position-detail/job-position-detail.component';
import { JobPositionPublishedComponent } from './pages/job-positions/job-position-published/job-position-published.component';
import { JobPositionByEntrepriseComponent } from './pages/job-positions/job-position-by-entreprise/job-position-by-entreprise.component';

// ── Applications ──────────────────────────────────────────────────────────────
import { ApplicationListComponent } from './pages/applications/application-list/application-list.component';
import { ApplicationCreateComponent } from './pages/applications/application-create/application-create.component';
import { ApplicationDetailComponent } from './pages/applications/application-detail/application-detail.component';
import { ApplicationByJobComponent } from './pages/applications/application-by-job/application-by-job.component';
import { InterviewListComponent } from './pages/interviews/interview-list/interview-list.component';
import { InterviewCreateComponent } from './pages/interviews/interview-create/interview-create.component';
import { InterviewDetailComponent } from './pages/interviews/interview-detail/interview-detail.component';
import { OfferListComponent } from './pages/offers/offer-list/offer-list.component';
import { OfferCreateComponent } from './pages/offers/offer-create/offer-create.component';
import { OfferDetailComponent } from './pages/offers/offer-detail/offer-detail.component';
import { OfferByApplicationComponent } from './pages/offers/offer-by-application/offer-by-application.component';
import { OfferByEntrepriseComponent } from './pages/offers/offer-by-entreprise/offer-by-entreprise.component';

import { ContractListComponent } from './pages/contracts/contract-list/contract-list.component';
import { ContractCreateComponent } from './pages/contracts/contract-create/contract-create.component';
import { ContractDetailComponent } from './pages/contracts/contract-detail/contract-detail.component';
import { ContractByOfferComponent } from './pages/contracts/contract-by-offer/contract-by-offer.component';
import { ContractByFreelancerComponent } from './pages/contracts/contract-by-freelancer/contract-by-freelancer.component';
import { ContractByEntrepriseComponent } from './pages/contracts/contract-by-entreprise/contract-by-entreprise.component';
import { TalentPoolListComponent } from './pages/talent-pool/talent-pool-list/talent-pool-list.component';
import { TalentPoolCreateComponent } from './pages/talent-pool/talent-pool-create/talent-pool-create.component';
import { TalentPoolDetailComponent } from './pages/talent-pool/talent-pool-detail/talent-pool-detail.component';


@NgModule({
    declarations: [

        // Layout
        RecruitmentSidebarComponent,
        RecruitmentNavbarComponent,
        RecruitmentLayoutComponent,

        // Shared
        RecruitmentCardComponent,
        RecruitmentTableComponent,
        StatusBadgeComponent,
        ButtonPrimaryComponent,

        // Dashboard
        RecruitmentDashboardComponent,

        // Job Positions
        JobPositionListComponent,
        JobPositionCreateComponent,
        JobPositionDetailComponent,
        JobPositionPublishedComponent,
        JobPositionByEntrepriseComponent,

        // Applications
        ApplicationListComponent,
        ApplicationCreateComponent,
        ApplicationDetailComponent,
        ApplicationByJobComponent,



        //Intervieew
        InterviewListComponent,
        InterviewCreateComponent,
        InterviewDetailComponent,
        OfferListComponent,
        OfferCreateComponent,
        OfferDetailComponent,
        OfferByApplicationComponent,
        OfferByEntrepriseComponent,
        ContractListComponent,
        ContractCreateComponent,
        ContractDetailComponent,
        ContractByOfferComponent,
        ContractByFreelancerComponent,
        ContractByEntrepriseComponent,


        // Talent Pool
        TalentPoolListComponent,
        TalentPoolCreateComponent,
        TalentPoolDetailComponent,


    ],
    imports: [
        CommonModule,
        FormsModule,          // ← requis pour [(ngModel)] dans les modals + search bars
        ReactiveFormsModule,
        HttpClientModule,
        RecruitmentServiceRoutingModule
    ]
})
export class RecruitmentServiceModule { }