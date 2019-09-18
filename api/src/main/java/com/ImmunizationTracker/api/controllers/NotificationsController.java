package com.ImmunizationTracker.api.controllers;

import com.ImmunizationTracker.api.entities.NotificationEntity;
import com.ImmunizationTracker.api.entities.VaccinationDoseEntity;
import com.ImmunizationTracker.api.entities.VaccinationEntity;
import com.ImmunizationTracker.api.models.NotificationType;
import com.ImmunizationTracker.api.models.StudentRecord;
import com.ImmunizationTracker.api.repository.NotificationEntityRepository;
import com.ImmunizationTracker.api.repository.VaccinationDoseEntityRepository;
import com.ImmunizationTracker.api.repository.VaccinationEntityRepository;
import com.ImmunizationTracker.api.services.NotificationsService;
import com.ImmunizationTracker.api.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping(path="/notifications")
public class NotificationsController {

    @Autowired
    private NotificationsService notificationsService;

    @GetMapping(path="/calculate")
    @ResponseBody
    public String calculateAlerts() {
        notificationsService.calculateNotifications();
        return "Notifications Processed!";
    }
}
