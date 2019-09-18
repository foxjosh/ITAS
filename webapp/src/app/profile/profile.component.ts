import { Component, OnInit } from '@angular/core';
import {AppUser} from "../models/app-user";
import {ApiClientService} from "../api-client.service";
import {DashboardViewModel} from "../models/dashboard-view-model";
import {StudentRecordsViewModel} from "../models/student-records-view-model";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  user: AppUser;
  dashboardViewModel: DashboardViewModel;
  studentRecordsViewModel: StudentRecordsViewModel;

  constructor(private apiClient: ApiClientService) { }

  ngOnInit() {
    this.populateDummyData();

    this.apiClient.getDashboard()
      .subscribe(dashboard => this.dashboardViewModel = dashboard);

    this.apiClient.getAllStudentRecords()
      .subscribe(studentRecords => {
        this.studentRecordsViewModel = studentRecords;
      });
  }

  private populateDummyData() {
    this.user = new AppUser("admin", "Admin User", "Administrator");
  }

}
