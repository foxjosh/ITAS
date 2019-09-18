package com.ImmunizationTracker.api.services;

import com.ImmunizationTracker.api.entities.StudentEntity;
import com.ImmunizationTracker.api.externalServices.FHIRServiceImmunization;
import com.ImmunizationTracker.api.externalServices.FHIRServicePatient;
import com.ImmunizationTracker.api.models.ImmunizationStatus;
import com.ImmunizationTracker.api.models.StudentRecord;
import com.ImmunizationTracker.api.models.VaccinationRecord;
import com.ImmunizationTracker.api.repository.StudentEntityRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StudentService {

    @Autowired
    private StudentEntityRepository studentEntityRepository;

    @Autowired
    private VaccinationService vaccinationService;

    private String fhirServerURL = "http://hapi.fhir.org/baseDstu3";

    public ArrayList<StudentRecord> getAllStudents(){
        ArrayList<StudentRecord> studentList = new ArrayList<StudentRecord>();

        Iterable<StudentEntity> entities = studentEntityRepository.findAll();
        for (StudentEntity entity : entities) {
            VaccinationRecord[] vaccinationRecords = vaccinationService.getVaccinationRecordsForStudent(entity.getStudentId(), entity.getPatientId());

            StudentRecord studentRecord = new StudentRecord(
                    entity.getStudentId(),
                    entity.getPatientId(),
                    entity.getFirstName(),
                    entity.getLastName(),
                    entity.getDob(),
                    entity.getSex(),
                    entity.getGrade(),
                    entity.getHomeroom(),
                    entity.getGuardianContact(),
                    entity.getNotes(),
                    entity.getImmunizationScore(),
                    vaccinationRecords
            );
            studentList.add(studentRecord);
        }
        return studentList;
    }

    public StudentRecord getStudentByStudentId(Integer studentId) throws NotFoundException {
        StudentEntity entity = studentEntityRepository.findById(studentId)
                .orElse(null);

        if(entity == null){
            throw new NotFoundException("Could not find student with id " + studentId);
        }

        VaccinationRecord[] vaccinationRecords = vaccinationService.getVaccinationRecordsForStudent(entity.getStudentId(), entity.getPatientId());

        StudentRecord studentRecord = new StudentRecord(
                entity.getStudentId(),
                entity.getPatientId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDob(),
                entity.getSex(),
                entity.getGrade(),
                entity.getHomeroom(),
                entity.getGuardianContact(),
                entity.getNotes(),
                entity.getImmunizationScore(),
                vaccinationRecords
        );

        return studentRecord;
    }

    public StudentRecord createStudent(StudentRecord student){

        //1. Save in FHIR.  Get the PatientID for future calls.
        FHIRServicePatient fhirServicePatient = new FHIRServicePatient(fhirServerURL);
        String patientId = fhirServicePatient.addNewPatient(student.getFirstName(),student.getLastName());
        fhirServicePatient.addBirthDate(patientId, student.getDob().toString());
        fhirServicePatient.addGender(patientId, student.getSex());

        //2. Save in local database with the PatientID
        StudentEntity entity = new StudentEntity();
        entity.setPatientId(patientId);
        entity.setFirstName(student.getFirstName());
        entity.setLastName(student.getLastName());
        entity.setDob(student.getDob());

        entity.setSex(student.getSex());
        entity.setGrade(student.getGrade());
        entity.setHomeroom(student.getHomeroom());
        entity.setGuardianContact(student.getGuardianContact());
        entity.setNotes(student.getNotes());
        entity.setImmunizationScore(0.0); //setting to 0 since that is accurate for a new student

        StudentEntity savedStudent = studentEntityRepository.save(entity);

        //3. Create StudentVaccinationScheduleEntities
        VaccinationRecord[] records = vaccinationService.createVaccinationScheduleForNewStudent(savedStudent);

        //4. Return StudentRecord will the PatientID
        student.setStudentId(savedStudent.getStudentId());
        student.setPatientId(patientId);
        student.setVaccinations(records);


        return student;
    }

    public StudentRecord updateStudent(StudentRecord student) throws NotFoundException {
        //1. Get Student Record from our DB
        StudentEntity entity = studentEntityRepository.findById(student.getStudentId())
                .orElse(null);

        if(entity == null) throw new NotFoundException("Could not find student with id " + student.getStudentId());

        //2. Update Local Student Record for each field if different
        if(entity.getFirstName() != student.getFirstName()) entity.setFirstName(student.getFirstName());
        if(entity.getLastName() != student.getLastName()) entity.setLastName(student.getLastName());
        if(entity.getDob() != student.getDob()) entity.setDob(student.getDob());
        if(entity.getSex() != student.getSex()) entity.setSex(student.getSex());
        if(entity.getGrade() != student.getGrade()) entity.setGrade(student.getGrade());
        if(entity.getHomeroom() != student.getHomeroom()) entity.setHomeroom(student.getHomeroom());
        if(entity.getGuardianContact() != student.getGuardianContact()) entity.setGuardianContact(student.getGuardianContact());
        if(entity.getNotes() != student.getNotes()) entity.setNotes(student.getNotes());

        StudentEntity savedStudent = studentEntityRepository.save(entity);

        //3. Update FHIR Server with Name, Vaccinations, etc.
        vaccinationService.updateVaccinations(student.getStudentId(), student.getPatientId(), student.getVaccinations());
        VaccinationRecord[] vaccinationRecords = vaccinationService.getVaccinationRecordsForStudent(student.getStudentId(), student.getPatientId());

        //4. Get Vaccinations and calculate new Immunization Score
        savedStudent.setImmunizationScore(calculateImmunizationScore(vaccinationRecords));
        StudentEntity savedStudent2 = studentEntityRepository.save(savedStudent);

        //5. Return savedStudent (Should not have changed)
        StudentRecord updatedStudentRecord = new StudentRecord(
                savedStudent2.getStudentId(),
                savedStudent2.getPatientId(),
                savedStudent2.getFirstName(),
                savedStudent2.getLastName(),
                savedStudent2.getDob(),
                savedStudent2.getSex(),
                savedStudent2.getGrade(),
                savedStudent2.getHomeroom(),
                savedStudent2.getGuardianContact(),
                savedStudent2.getNotes(),
                savedStudent2.getImmunizationScore(),
                vaccinationRecords
        );
        return updatedStudentRecord;
    }

    public void deleteAllStudents(){
//        ArrayList<StudentRecord> studentRecords = getAllStudents();
//
//        //1. Delete FHIR Data
//        for (int i = 0; i < studentRecords.size(); i++) {
//            String patientToDeleteId = studentRecords.get(i).getPatientId();
//            FHIRServicePatient fhirServicePatient = new FHIRServicePatient(fhirServerURL);
//            fhirServicePatient.deletePatient(patientToDeleteId);
//        }


        //2. Delete Local DB Student
        studentEntityRepository.deleteAll();
    }

    private double calculateImmunizationScore(VaccinationRecord[] records){
        double incomplete = 0.0;
        double overdue = 0.0;
        double complete = 0.0;
        for (int i = 0; i < records.length; i++) {
            VaccinationRecord record = records[i];
            ImmunizationStatus status = record.getStatus();
            if(status == ImmunizationStatus.INCOMPLETE) incomplete++;
            if(status == ImmunizationStatus.OVERDUE) overdue++;
            if(status == ImmunizationStatus.COMPLETED) complete++;
        }

        return complete / (incomplete + overdue + complete);
    }
}