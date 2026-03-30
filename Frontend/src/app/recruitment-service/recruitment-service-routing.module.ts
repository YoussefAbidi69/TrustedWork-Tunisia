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
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RecruitmentServiceRoutingModule { }
