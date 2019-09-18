package com.ImmunizationTracker.api.models;

import java.time.LocalDate;

public class VaccinationRecord {

    private Integer studentVaccinationDoseScheduleId;
    //this is the FHIR ID
    private String immunizationRecordId;
    private String vaccinationName;
    private ImmunizationStatus status;
    private LocalDate recommendedDate;
    private LocalDate completedDate;
    private String note;

    public VaccinationRecord(Integer studentVaccinationDoseScheduleId, String immunizationRecordId, String vaccinationName, ImmunizationStatus status, LocalDate recommendedDate, LocalDate completedDate, String note) {
        this.studentVaccinationDoseScheduleId = studentVaccinationDoseScheduleId;
        this.immunizationRecordId = immunizationRecordId;
        this.vaccinationName = vaccinationName;
        this.status = status;
        this.recommendedDate = recommendedDate;
        this.completedDate = completedDate;
        this.note = note;
    }

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

    public String getVaccinationName() {
        return vaccinationName;
    }

    public void setVaccinationName(String vaccinationName) {
        this.vaccinationName = vaccinationName;
    }

    public ImmunizationStatus getStatus() {
        return status;
    }

    public void setStatus(ImmunizationStatus status) {
        this.status = status;
    }

    public LocalDate getRecommendedDate() {
        return recommendedDate;
    }

    public void setRecommendedDate(LocalDate recommendedDate) {
        this.recommendedDate = recommendedDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
