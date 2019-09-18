package com.ImmunizationTracker.api.models;

import java.util.ArrayList;

public class StudentRecordsViewModel {
    private ArrayList<StudentRecord> StudentRecords;

    public StudentRecordsViewModel() {

        StudentRecords = new ArrayList<StudentRecord>();
    }

    public ArrayList<StudentRecord> getStudentRecords() {
        return StudentRecords;
    }

    public void setStudentRecords(ArrayList<StudentRecord> studentRecords) {
        StudentRecords = studentRecords;
    }

    public void addStudentRecord(StudentRecord studentRecord){

        StudentRecords.add(studentRecord);
    }

}