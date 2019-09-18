package com.ImmunizationTracker.api.entities;

import com.ImmunizationTracker.api.models.NotificationType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity // This tells Hibernate to make a table out of this class
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer notificationId;

    //I think vaccinationId or something makes sense to track what triggered this
    private LocalDate notificationDate;
    private NotificationType type;

    @Column(length = 3000)
    private String summary;

    @Column(length = 3000)
    private String recommendations;
    private Boolean visible;

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