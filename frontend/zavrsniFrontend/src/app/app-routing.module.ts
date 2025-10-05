import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {Oauth2RedirectComponent} from './components/oauth2-redirect/oauth2-redirect.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {HomeComponent} from './components/home/home.component';
import {AuthGuard} from './auth.guard';
import {ProfileComponent} from './components/profile/profile.component';
import {FeedComponent} from './components/feed/feed.component';

const routes: Routes = [
  {path:'oauth2-redirect', component:Oauth2RedirectComponent},
  {path:'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent },
  {path: '', component: HomeComponent, canActivate: [AuthGuard]},
  { path: 'profile/:username', component: ProfileComponent, canActivate: [AuthGuard] },
  {path: 'feed', component: FeedComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
