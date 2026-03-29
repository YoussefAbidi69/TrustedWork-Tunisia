import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'recruitment',
    pathMatch: 'full'
  },
  {
    path: 'recruitment',
    loadChildren: () =>
      import('./recruitment-service/recruitment-service.module').then(
        (m) => m.RecruitmentServiceModule
      )
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
