import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminLayout } from './admin-layout/admin-layout';
import { Dashboard } from './dashboard/dashboard';
import { ProfileList } from './profiles/profile-list/profile-list';
import { ProfileForm } from './profiles/profile-form/profile-form';
import { SkillList } from './skills/skill-list/skill-list';
import { ExamList } from './exams/exam-list/exam-list';
import { EndorsementList } from './endorsements/endorsement-list/endorsement-list';

const routes: Routes = [
  {
    path: '',
    component: AdminLayout,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'profiles', component: ProfileList },
      { path: 'profiles/new', component: ProfileForm },
      { path: 'profiles/edit/:id', component: ProfileForm },
      { path: 'skills', component: SkillList },
      { path: 'exams', component: ExamList },
      { path: 'endorsements', component: EndorsementList },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }