import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {DashboardComponent} from './dashboard/dashboard.component';
import {RecordEntryComponent} from './record-entry/record-entry.component';
import {StudentRecordsComponent} from './student-records/student-records.component';
import {ProfileComponent} from './profile/profile.component';
import {LoginComponent} from './login/login.component';
import {ProtectedGuard, PublicGuard} from 'ngx-auth';

const routes: Routes = [
  { path: 'login', component: LoginComponent, data: { title: 'Login' } },
  { path: 'dashboard', component: DashboardComponent, data: { title: 'Dashboard' }, canActivate: [ ProtectedGuard ] },
  { path: 'record-entry/:id', component: RecordEntryComponent, data: { title: 'Enter Student Record' }, canActivate: [ ProtectedGuard ] },
  { path: 'student-records', component: StudentRecordsComponent, data: { title: 'Student Records' }, canActivate: [ ProtectedGuard ] },
  { path: 'profile', component: ProfileComponent, data: { title: 'My Profile' }, canActivate: [ ProtectedGuard ] },
  { path: 'record-entry', redirectTo: '/record-entry/0', pathMatch: 'full', canActivate: [ ProtectedGuard ] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
