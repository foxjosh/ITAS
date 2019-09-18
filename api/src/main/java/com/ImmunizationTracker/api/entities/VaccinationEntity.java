package com.ImmunizationTracker.api.entities;

import javax.persistence.*;
import java.util.Set;

@Entity // This tells Hibernate to make a table out of this class
public class VaccinationEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer vaccinationId;

    private String fullName;
    private String shortName;
    private String code;

    @OneToMany
    private Set<VaccinationDoseEntity> vaccinationDoseEntities;

    public Integer getVaccinationId() {
        return vaccinationId;
    }

    public void setVaccinationId(Integer vaccinationId) {
        this.vaccinationId = vaccinationId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<VaccinationDoseEntity> getVaccinationDoseEntities() {
        return vaccinationDoseEntities;
    }

    public void setVaccinationDoseEntities(Set<VaccinationDoseEntity> vaccinationDoseEntities) {
        this.vaccinationDoseEntities = vaccinationDoseEntities;
    }
}
