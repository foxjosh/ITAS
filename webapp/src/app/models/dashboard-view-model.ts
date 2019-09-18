import { AppNotification } from "./app-notification";

export class DashboardViewModel {
  appNotifications: AppNotification[];

  constructor(appNotifications: AppNotification[]) {
    this.appNotifications = appNotifications;
  }
}

