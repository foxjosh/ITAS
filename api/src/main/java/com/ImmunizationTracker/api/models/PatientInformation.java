package com.ImmunizationTracker.api.models;

public class PatientInformation {

    private String firstName;
    private String lastName;

    public PatientInformation(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }
}
