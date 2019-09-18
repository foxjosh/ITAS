package com.ImmunizationTracker.api.models;

import java.time.LocalDate;

public class StudentRecord {
    private int studentId;
    private String patientId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String sex;
    private String grade;
    private String homeroom;
    private String guardianContact;
    private String notes;
    private double immunizationScore;
    private VaccinationRecord[] vaccinations;

    public StudentRecord(int studentId, String patientId, String firstName, String lastName, LocalDate dob, String sex, String grade, String homeroom, String guardianContact, String notes, double immunizationScore, VaccinationRecord[] vaccinations) {
        this.studentId = studentId;
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.sex = sex;
        this.grade = grade;
        this.homeroom = homeroom;
        this.guardianContact = guardianContact;
        this.notes = notes;
        this.immunizationScore = immunizationScore;
        this.vaccinations = vaccinations;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getHomeroom() {
        return homeroom;
    }

    public void setHomeroom(String homeroom) {
        this.homeroom = homeroom;
    }

    public String getGuardianContact() {
        return guardianContact;
    }

    public void setGuardianContact(String guardianContact) {
        this.guardianContact = guardianContact;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getImmunizationScore() {
        return immunizationScore;
    }

    public void setImmunizationScore(double immunizationScore) {
        this.immunizationScore = immunizationScore;
    }

    public VaccinationRecord[] getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(VaccinationRecord[] vaccinations) {
        this.vaccinations = vaccinations;
    }
}