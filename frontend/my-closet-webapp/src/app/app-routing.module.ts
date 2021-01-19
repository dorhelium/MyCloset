import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomepageComponent} from "./homepage/homepage.component";
import {UserPageComponent} from "./user-page/user-page.component";
import {AuthGuardService} from "./Service/auth-guard.service";


const routes: Routes = [
  {path:'',redirectTo:'homepage', pathMatch: 'full'},
  {path: 'homepage', component: HomepageComponent},
  {path: 'userpage', component: UserPageComponent, canActivate: [AuthGuardService]},
  {path: '**', redirectTo:'homepage'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
