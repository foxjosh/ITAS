package com.ImmunizationTracker.api.controllers;

import com.ImmunizationTracker.api.entities.NotificationEntity;
import com.ImmunizationTracker.api.entities.StudentEntity;
import com.ImmunizationTracker.api.models.AppNotification;
import com.ImmunizationTracker.api.models.DashboardViewModel;
import com.ImmunizationTracker.api.models.NotificationType;
import com.ImmunizationTracker.api.repository.NotificationEntityRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping(path="/dashboard")
public class DashboardController {
    @Autowired
    private NotificationEntityRepository notificationEntityRepository;

    @GetMapping("")
    @ResponseBody
    public DashboardViewModel getDashboardViewModel() {
        DashboardViewModel vm = new DashboardViewModel();
        vm.setAppNotifications(getAppNotifications());
        return vm;
    }

    @PostMapping("/dismiss/{notificationId}")
    @ResponseBody
    public String dismissNotification(@PathVariable(value="notificationId") Integer notificationId) throws NotFoundException {
        NotificationEntity entity = notificationEntityRepository.findById(notificationId)
                .orElse(null);

        if(entity == null){
            throw new NotFoundException("Could not find notification with id " + notificationId);
        }

        entity.setVisible(false);
        notificationEntityRepository.save(entity);
        return "Successfully dismissed alert!";
    }

    private ArrayList<AppNotification> getAppNotifications(){
        ArrayList<AppNotification> list = new ArrayList<AppNotification>();

        Iterable<NotificationEntity> entities = notificationEntityRepository.findAll();
        for(NotificationEntity entity : entities){
            AppNotification appNotification = new AppNotification(
                    entity.getNotificationId(),
                    entity.getNotificationDate(),
                    entity.getType(),
                    entity.getSummary(),
                    entity.getRecommendations(),
                    entity.getVisible());

            list.add(appNotification);
        }
        return list;
    }
}
