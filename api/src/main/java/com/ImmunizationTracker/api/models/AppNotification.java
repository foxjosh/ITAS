package com.ImmunizationTracker.api.models;

import java.time.LocalDate;

public class AppNotification {

    private Integer notificationId;
    private LocalDate notificationDate;
    private NotificationType type;
    private String summary;
    private String recommendations;
    private Boolean visible;

    public AppNotification(Integer notificationId, LocalDate notificationDate, NotificationType type, String summary, String recommendations, Boolean visible) {
        this.notificationId = notificationId;
        this.notificationDate = notificationDate;
        this.type = type;
        this.summary = summary;
        this.recommendations = recommendations;
        this.visible = visible;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public LocalDate getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(LocalDate notificationDate) {
        this.notificationDate = notificationDate;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
