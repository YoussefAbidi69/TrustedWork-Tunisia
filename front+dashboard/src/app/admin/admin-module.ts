import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AdminRoutingModule } from './admin-routing-module';
import { AdminLayout } from './admin-layout/admin-layout';
import { Dashboard } from './dashboard/dashboard';
import { ProfileList } from './profiles/profile-list/profile-list';
import { ProfileForm } from './profiles/profile-form/profile-form';
import { SkillList } from './skills/skill-list/skill-list';
import { ExamList } from './exams/exam-list/exam-list';
import { EndorsementList } from './endorsements/endorsement-list/endorsement-list';

@NgModule({
  declarations: [
    AdminLayout,
    Dashboard,
    ProfileList,
    ProfileForm,
    SkillList,
    ExamList,
    EndorsementList
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule
  ]
})
export class AdminModule { }

//donneee