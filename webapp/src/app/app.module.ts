import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { StudentRecordsComponent } from './student-records/student-records.component';
import { RecordEntryComponent } from './record-entry/record-entry.component';
import { ProfileComponent } from './profile/profile.component';
import { LoginComponent } from './login/login.component';
import { ChartsModule } from 'ng2-charts';
import { AuthenticationModule} from './authentication/authentication.module';
import { FilterStudentRecordGridPipe } from './student-records/filter-student-record-grid.pipe';
import { NgSelectModule } from '@ng-select/ng-select';
import {ToastrModule} from 'ngx-toastr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    StudentRecordsComponent,
    RecordEntryComponent,
    ProfileComponent,
    LoginComponent,
    FilterStudentRecordGridPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ChartsModule,
    AuthenticationModule,
    NgSelectModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(
      {
        timeOut: 2000,
        positionClass: 'toast-top-center',
        preventDuplicates: true,
        resetTimeoutOnDuplicate: true
      }
    )
  ],
  providers: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
