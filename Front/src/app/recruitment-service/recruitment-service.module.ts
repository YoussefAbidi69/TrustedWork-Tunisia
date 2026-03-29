import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { RecruitmentServiceRoutingModule } from './recruitment-service-routing.module';

// Layout Components
import { RecruitmentSidebarComponent } from './components/recruitment-sidebar/recruitment-sidebar.component';
import { RecruitmentNavbarComponent } from './components/recruitment-navbar/recruitment-navbar.component';
import { RecruitmentLayoutComponent } from './components/recruitment-layout/recruitment-layout.component';

// Shared Components
import { RecruitmentCardComponent } from './shared/recruitment-card/recruitment-card.component';
import { RecruitmentTableComponent } from './shared/recruitment-table/recruitment-table.component';
import { StatusBadgeComponent } from './shared/status-badge/status-badge.component';
import { ButtonPrimaryComponent } from './shared/button-primary/button-primary.component';

// Pages
import { RecruitmentDashboardComponent } from './pages/dashboard/recruitment-dashboard.component';
import { JobPositionListComponent } from './pages/job-positions/job-position-list/job-position-list.component';
import { JobPositionCreateComponent } from './pages/job-positions/job-position-create/job-position-create.component';
import { JobPositionDetailComponent } from './pages/job-positions/job-position-detail/job-position-detail.component';
import { JobPositionPublishedComponent } from './pages/job-positions/job-position-published/job-position-published.component';
import { JobPositionByEntrepriseComponent } from './pages/job-positions/job-position-by-entreprise/job-position-by-entreprise.component';
import { ApplicationListComponent } from './pages/applications/application-list/application-list.component';
import { ApplicationDetailComponent } from './pages/applications/application-detail/application-detail.component';
import { ApplicationCreateComponent } from './pages/applications/application-create/application-create.component';

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

        // Pages
        RecruitmentDashboardComponent,
        JobPositionListComponent,
        JobPositionCreateComponent,
        JobPositionDetailComponent,
        JobPositionPublishedComponent,
        JobPositionByEntrepriseComponent,
        ApplicationListComponent,
        ApplicationDetailComponent,
        ApplicationCreateComponent
    ],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        HttpClientModule,
        RecruitmentServiceRoutingModule
    ]
})
export class RecruitmentServiceModule { }
