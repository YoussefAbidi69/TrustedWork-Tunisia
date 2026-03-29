import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { FrontRoutingModule } from './front-routing-module';
import { FrontLayout } from './front-layout/front-layout';
import { Home } from './home/home';
import { Profile } from './profile/profile';
import { MySkills } from './my-skills/my-skills';
import { TakeExam } from './take-exam/take-exam';
import { Endorsements } from './endorsements/endorsements';
import { Passport } from './passport/passport';

@NgModule({
  declarations: [
    FrontLayout,
    Home,
    Profile,
    MySkills,
    TakeExam,
    Endorsements,
    Passport
  ],
  imports: [
    CommonModule,
    FrontRoutingModule,
    FormsModule,
    RouterModule
  ]
})
export class FrontModule { }