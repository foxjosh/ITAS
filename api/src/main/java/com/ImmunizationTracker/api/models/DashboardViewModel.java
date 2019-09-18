package com.ImmunizationTracker.api.models;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel {

    private ArrayList<AppNotification> AppNotifications;

    public DashboardViewModel() {
        AppNotifications = new ArrayList<AppNotification>();
    }

    public ArrayList<AppNotification> getAppNotifications() {
        return AppNotifications;
    }

    public void setAppNotifications(ArrayList<AppNotification> appNotifications) {
        AppNotifications = appNotifications;
    }

    public void addAppNotification(AppNotification appNotification){
        AppNotifications.add(appNotification);
    }



}
