package com.ImmunizationTracker.api.entities;

import com.ImmunizationTracker.api.models.ImmunizationStatus;
import com.ImmunizationTracker.api.models.NotificationType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity // This tells Hibernate to make a table out of this class
public class StudentVaccinationDoseScheduleEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer studentVaccinationDoseScheduleId;

    //This is saved in FHIR
    private String immunizationRecordId;

    @ManyToOne
    private StudentEntity studentEntity;

    @ManyToOne
    private VaccinationDoseEntity vaccinationDoseEntity;

    private LocalDate recommendedDate;

    public Integer getStudentVaccinationDoseScheduleId() {
        return studentVaccinationDoseScheduleId;
    }

    public void setStudentVaccinationDoseScheduleId(Integer studentVaccinationDoseScheduleId) {
        this.studentVaccinationDoseScheduleId = studentVaccinationDoseScheduleId;
    }

    public String getImmunizationRecordId() {
        return immunizationRecordId;
    }

    public void setImmunizationRecordId(String immunizationRecordId) {
        this.immunizationRecordId = immunizationRecordId;
    }

    public StudentEntity getStudentEntity() {
        return studentEntity;
    }

    public void setStudentEntity(StudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }

    public VaccinationDoseEntity getVaccinationDoseEntity() {
        return vaccinationDoseEntity;
    }

    public void setVaccinationDoseEntity(VaccinationDoseEntity vaccinationDoseEntity) {
        this.vaccinationDoseEntity = vaccinationDoseEntity;
    }

    public LocalDate getRecommendedDate() {
        return recommendedDate;
    }

    public void setRecommendedDate(LocalDate recommendedDate) {
        this.recommendedDate = recommendedDate;
    }
}