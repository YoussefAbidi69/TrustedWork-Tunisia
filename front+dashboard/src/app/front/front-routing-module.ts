import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FrontLayout } from './front-layout/front-layout';
import { Home } from './home/home';
import { Profile } from './profile/profile';
import { MySkills } from './my-skills/my-skills';
import { TakeExam } from './take-exam/take-exam';
import { Endorsements } from './endorsements/endorsements';
import { Passport } from './passport/passport';

const routes: Routes = [
  {
    path: '',
    component: FrontLayout,
    children: [
      { path: 'home', component: Home },
      { path: 'profile', component: Profile },
      { path: 'my-skills', component: MySkills },
      { path: 'take-exam', component: TakeExam },
      { path: 'endorsements', component: Endorsements },
      { path: 'passport', component: Passport },
      { path: '', redirectTo: 'home', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FrontRoutingModule { }