package com.ImmunizationTracker.api.entities;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class VaccinationDoseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer vaccinationDoseId;

    @ManyToOne
    private VaccinationEntity vaccinationEntity;

    private Integer doseNumber;
    private String fullName;
    private String schedule; // Example "1m" = 1 month.  Options are d = days, w = weeks, m = months, y = years
    private double thresholdPercentage; //.85 = Less than 85% coverage will trigger an alert

    public Integer getVaccinationDoseId() {
        return vaccinationDoseId;
    }

    public void setVaccinationDoseId(Integer vaccinationDoseId) {
        this.vaccinationDoseId = vaccinationDoseId;
    }

    public VaccinationEntity getVaccinationEntity() {
        return vaccinationEntity;
    }

    public void setVaccinationEntity(VaccinationEntity vaccinationEntity) {
        this.vaccinationEntity = vaccinationEntity;
    }

    public Integer getDoseNumber() {
        return doseNumber;
    }

    public void setDoseNumber(Integer doseNumber) {
        this.doseNumber = doseNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public double getThresholdPercentage() {
        return thresholdPercentage;
    }

    public void setThresholdPercentage(double thresholdPercentage) {
        this.thresholdPercentage = thresholdPercentage;
    }
}
