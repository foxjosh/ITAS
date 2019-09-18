package com.ImmunizationTracker.api.controllers;

import com.ImmunizationTracker.api.entities.NotificationEntity;
import com.ImmunizationTracker.api.entities.StudentVaccinationDoseScheduleEntity;
import com.ImmunizationTracker.api.entities.VaccinationDoseEntity;
import com.ImmunizationTracker.api.entities.VaccinationEntity;
import com.ImmunizationTracker.api.models.*;
import com.ImmunizationTracker.api.repository.NotificationEntityRepository;
import com.ImmunizationTracker.api.repository.StudentVaccinationDoseScheduleEntityRepository;
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
@RequestMapping(path="/reset")
public class ResetController {
    @Autowired
    private VaccinationEntityRepository vaccinationEntityRepository;
    @Autowired
    private VaccinationDoseEntityRepository vaccinationDoseEntityRepository;
    @Autowired
    private StudentVaccinationDoseScheduleEntityRepository studentVaccinationDoseScheduleEntityRepository;
    @Autowired
    private NotificationEntityRepository notificationEntityRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private NotificationsService notificationsService;

    @GetMapping(path="")
    @ResponseBody
    public String resetDataForDemo() {
        deleteAllVaccinationEntities();
        deleteAllStudentEntities();
        createVaccinationEntities();
        resetNotifications();
        createStubbedStudentEntities();
        return "Success!";
    }

    private void resetVaccinationEntities(){
        createVaccinationEntities();
    }

    private void deleteAllVaccinationEntities(){
        studentVaccinationDoseScheduleEntityRepository.deleteAll();
        vaccinationDoseEntityRepository.deleteAll();
        vaccinationEntityRepository.deleteAll();
    }

    private void createVaccinationEntities(){
        //Hep B
        VaccinationEntity hepB1 = new VaccinationEntity();
        hepB1.setFullName("Hepatitis B");
        hepB1.setShortName("HepB");
        hepB1.setCode("08");
        VaccinationEntity hepB1_saved = vaccinationEntityRepository.save(hepB1);

        VaccinationDoseEntity hepB1_dose1 = new VaccinationDoseEntity();
        hepB1_dose1.setDoseNumber(1);
        hepB1_dose1.setFullName("Hepatitis B - Dose 1");
        hepB1_dose1.setSchedule("0d");
        hepB1_dose1.setThresholdPercentage(.90);
        hepB1_dose1.setVaccinationEntity(hepB1_saved);
        vaccinationDoseEntityRepository.save(hepB1_dose1);

        VaccinationDoseEntity hepB1_dose2 = new VaccinationDoseEntity();
        hepB1_dose2.setDoseNumber(2);
        hepB1_dose2.setFullName("Hepatitis B - Dose 2");
        hepB1_dose2.setSchedule("2m");
        hepB1_dose2.setThresholdPercentage(.90);
        hepB1_dose2.setVaccinationEntity(hepB1_saved);
        vaccinationDoseEntityRepository.save(hepB1_dose2);

        VaccinationDoseEntity hepB1_dose3 = new VaccinationDoseEntity();
        hepB1_dose3.setDoseNumber(3);
        hepB1_dose3.setFullName("Hepatitis B - Dose 3");
        hepB1_dose3.setSchedule("18m");
        hepB1_dose3.setThresholdPercentage(.90);
        hepB1_dose3.setVaccinationEntity(hepB1_saved);
        vaccinationDoseEntityRepository.save(hepB1_dose3);

        //Rotavirus
        VaccinationEntity rv = new VaccinationEntity();
        rv.setFullName("Rotavirus");
        rv.setShortName("RV");
        rv.setCode("122");
        VaccinationEntity rv_saved = vaccinationEntityRepository.save(rv);

        VaccinationDoseEntity rv_dose1 = new VaccinationDoseEntity();
        rv_dose1.setDoseNumber(1);
        rv_dose1.setFullName("Rotavirus - Dose 1");
        rv_dose1.setSchedule("2m");
        rv_dose1.setThresholdPercentage(.70);
        rv_dose1.setVaccinationEntity(rv_saved);
        vaccinationDoseEntityRepository.save(rv_dose1);

        VaccinationDoseEntity rv_dose2 = new VaccinationDoseEntity();
        rv_dose2.setDoseNumber(2);
        rv_dose2.setFullName("Rotavirus - Dose 2");
        rv_dose2.setSchedule("4m");
        rv_dose2.setThresholdPercentage(.70);
        rv_dose2.setVaccinationEntity(rv_saved);
        vaccinationDoseEntityRepository.save(rv_dose2);

        //Diphtheria, tetanus, & acellular pertussis
//        VaccinationEntity dtap = new VaccinationEntity();
//        dtap.setFullName("Diphtheria, tetanus, & acellular pertussis");
//        dtap.setShortName("DTaP");
//        dtap.setCode("20");
//        dtap.setDoseScheduleAndRecommendedCoverage("0d|2m|6m|18m|7y");
//        vaccinationEntityRepository.save(dtap);

    }

    private void resetNotifications(){
        notificationsService.clearNotifications();
        createStubbedNotifications();
    }

    private void createStubbedNotifications(){
        NotificationEntity e1 = new NotificationEntity();
        e1.setNotificationDate(LocalDate.of(2018,11,10));
        e1.setType(NotificationType.WARNING);
        e1.setSummary("Your school is below the 95% threshold for the MMR vaccine.");
        e1.setRecommendations("Send notes home to parents with information about the importance of vaccinating their children.");
        e1.setVisible(true);

        NotificationEntity e2 = new NotificationEntity();
        e2.setNotificationDate(LocalDate.of(2018,11,1));
        e2.setType(NotificationType.INFORMATION);
        e2.setSummary("Flu season is approaching");
        e2.setRecommendations("Remind students to get their flu shots.");
        e2.setVisible(true);

        NotificationEntity e3 = new NotificationEntity();
        e3.setNotificationDate(LocalDate.of(2018,10,31));
        e3.setType(NotificationType.WARNING);
        e3.setSummary("Your school is below the 95% threshold for the Varicella vaccine.");
        e3.setRecommendations("Send notes home to parents with information about the importance of vaccinating their children.");
        e3.setVisible(true);

        notificationEntityRepository.save(e1);
        notificationEntityRepository.save(e2);
        notificationEntityRepository.save(e3);
    }


    private void resetStudentEntities(){
        createStubbedStudentEntities();
    }

    private void deleteAllStudentEntities(){
        studentService.deleteAllStudents();
    }

    private void createStubbedStudentEntities(){
        ArrayList<StudentRecord> records = getStubbedStudentRecordsData();

        for (StudentRecord record : records) {
            studentService.createStudent(record);
        }
    }


    private ArrayList<StudentRecord> getStubbedStudentRecordsData(){
        StudentRecord s1 = new StudentRecord(
                1, //Will be overwritten
                null, //Will be overwritten
                "Betty",
                "Nicholson",
                LocalDate.of(2011, 9, 10),
                "Female",
                "2nd",
                "Ms. Honey",
                "Sandra Nicholson (Mom)",
                "Student held non-medical exemption in 2015 but has since caught up on immunization schedule.",
                .3,
                null
        );

        StudentRecord s2 = new StudentRecord(
                2, //Will be overwritten
                null, //Will be overwritten
                "Andy",
                "Thomas",
                LocalDate.of(2010, 7, 12),
                "Male",
                "3rd",
                "Ms. Honey",
                "Dave Thomas (Dad)",
                "",
                .8,
                null
        );

        StudentRecord s3 = new StudentRecord(
                3, //Will be overwritten
                null, //Will be overwritten
                "Jimmy",
                "Smith",
                LocalDate.of(2008, 7, 29),
                "Male",
                "5th",
                "Ms. Trunchbull",
                "Robert Smith (Dad)",
                "",
                .5,
                null
        );

        ArrayList<StudentRecord> list = new ArrayList<StudentRecord>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        return list;
    }
}
