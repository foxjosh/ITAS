package com.ImmunizationTracker.api.services;

import com.ImmunizationTracker.api.entities.NotificationEntity;
import com.ImmunizationTracker.api.entities.StudentEntity;
import com.ImmunizationTracker.api.entities.StudentVaccinationDoseScheduleEntity;
import com.ImmunizationTracker.api.entities.VaccinationDoseEntity;
import com.ImmunizationTracker.api.models.ImmunizationStatus;
import com.ImmunizationTracker.api.models.NotificationType;
import com.ImmunizationTracker.api.models.VaccinationRecord;
import com.ImmunizationTracker.api.repository.NotificationEntityRepository;
import com.ImmunizationTracker.api.repository.StudentVaccinationDoseScheduleEntityRepository;
import com.ImmunizationTracker.api.repository.VaccinationDoseEntityRepository;
import com.ImmunizationTracker.api.repository.VaccinationEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class NotificationsService {

    @Autowired
    private NotificationEntityRepository notificationEntityRepository;
    @Autowired
    private StudentVaccinationDoseScheduleEntityRepository studentVaccinationDoseScheduleEntityRepository;
    @Autowired
    private VaccinationEntityRepository vaccinationEntityRepository;
    @Autowired
    private VaccinationDoseEntityRepository vaccinationDoseEntityRepository;
    @Autowired
    private VaccinationService vaccinationService;

    public void clearNotifications(){
        notificationEntityRepository.deleteAll();
    }

    public void calculateNotifications(){
        //Clear All Notifications
        clearNotifications();

        //Get lots of data
        Iterable<VaccinationDoseEntity> vaccinationDoses = vaccinationDoseEntityRepository.findAll();
        Iterable<StudentVaccinationDoseScheduleEntity> studentVaccinationDoseScheduleEntities = studentVaccinationDoseScheduleEntityRepository.findAll();

        //Generate our notifications for threshold issues
        calculateThresholdWarningNotifications(vaccinationDoses,studentVaccinationDoseScheduleEntities);
    }

    private void calculateThresholdWarningNotifications(Iterable<VaccinationDoseEntity> vaccinationDoses, Iterable<StudentVaccinationDoseScheduleEntity> studentVaccinationDoseScheduleEntities){
        //For each vaccine dose we are tracking, tally up each students records of it.
        for(VaccinationDoseEntity vaccinationDose : vaccinationDoses){
            double completed = 0.0;
            double incomplete = 0.0;
            double overdue = 0.0;
            for(StudentVaccinationDoseScheduleEntity studentVaccinationDoseScheduleEntity : studentVaccinationDoseScheduleEntities){

                //If this Student Record is for the vaccine we care about
                if(studentVaccinationDoseScheduleEntity.getVaccinationDoseEntity().getVaccinationDoseId() == vaccinationDose.getVaccinationDoseId()){
                    VaccinationRecord record = vaccinationService.getVaccinationRecordForStudentVaccinationDoseScheduleEntity(studentVaccinationDoseScheduleEntity);
                    ImmunizationStatus status = record.getStatus();
                    if(status == ImmunizationStatus.INCOMPLETE) incomplete++;
                    if(status == ImmunizationStatus.OVERDUE) overdue++;
                    if(status == ImmunizationStatus.COMPLETED) completed++;
                }
            }

            double immunizationCoverageValue = completed / (incomplete + overdue + completed);
            double immunizationThresholdAlert = vaccinationDose.getThresholdPercentage();
            if(immunizationCoverageValue < immunizationThresholdAlert){
                String immunizationName = vaccinationDose.getFullName();

                NotificationEntity e = new NotificationEntity();
                e.setNotificationDate(LocalDate.now());
                e.setType(NotificationType.WARNING);
                e.setSummary(String.format("%s coverage has fallen below recommended threshold levels of %.0f%% and is at %.0f%% at the time of this alert. %.0f%% population coverage is recommended to reduce risk of outbreak but may not, itself, be 100%% effective at prevention. It is recommended you take immediate actions to increase coverage.", immunizationName, immunizationThresholdAlert * 100.0, immunizationCoverageValue * 100.0, immunizationThresholdAlert * 100.0));
                e.setRecommendations(String.format("Send information home to parents and guardians about the risks of %s, importance of immunization and consider hosting a immunization clinic. Students most at risk for infectious disease are also the least likely to become vaccinated due to cost or other socioeconomic factors. These barriers could be reduced by offering vaccinations on site.", immunizationName));
                e.setVisible(true);

                notificationEntityRepository.save(e);
            }
        }
    }
}
