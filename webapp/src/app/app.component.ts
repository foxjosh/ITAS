import { Component, Injectable, OnInit } from '@angular/core';
import { TestAPIClient } from './test-apiclient';
import {Router} from "@angular/router";
import {ApiClientService} from "./api-client.service";
import {DashboardViewModel} from "./models/dashboard-view-model";
import {AuthenticationService} from "./authentication/authentication.service";

@Injectable()
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers:  [ TestAPIClient ]
})
export class AppComponent implements OnInit {
  dashboardViewModel: DashboardViewModel;
  showMenu: boolean;
  // helloWorldResult = 'No response yet...';

  constructor(private testApiClient: TestAPIClient, private apiClient: ApiClientService,
              private router: Router, private authService: AuthenticationService) {
  }

  ngOnInit() {
    this.showMenu = false;
    // this.testApiClient.getHelloWorld()
    //   .subscribe(response => {
    //     this.helloWorldResult = response.toString();
    //   });

    this.apiClient.getDashboard()
      .subscribe(dashboard => this.dashboardViewModel = dashboard);
  }

  logout() {
    this.authService.logout();
  }
}
