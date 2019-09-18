import {Component, OnInit} from '@angular/core';
import { Observable, of } from 'rxjs';

import {AppNotification} from '../models/app-notification';
import {NotificationType} from '../models/notification-type';
import {ApiClientService} from '../api-client.service';
import { DashboardViewModel } from '../models/dashboard-view-model';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  providers:  [ ApiClientService ]
})
export class DashboardComponent implements OnInit {

  dashboardViewModel: DashboardViewModel;
  loading: boolean;

  constructor(
    private apiClient: ApiClientService,
    private toastr: ToastrService
) { }

  ngOnInit() {
    this.loading = true;
    this.apiClient.getDashboard()
      .subscribe(dashboard => {
        this.dashboardViewModel = dashboard;
        this.loading = false;
      });
  }

  dismissAlert(notification: AppNotification) {
    this.toastr.info('This feature does not exist yet.');
  }

  getAlertIconCssClass(alertType: NotificationType) {
    let className = 'far fa-lightbulb';
    if (alertType == NotificationType.Warning) {
      className = 'far fa-frown';
    }
    return className;
  }

}
