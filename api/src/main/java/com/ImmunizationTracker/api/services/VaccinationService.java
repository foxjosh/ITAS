package com.ImmunizationTracker.api.services;

import com.ImmunizationTracker.api.entities.StudentEntity;
import com.ImmunizationTracker.api.entities.StudentVaccinationDoseScheduleEntity;
import com.ImmunizationTracker.api.entities.VaccinationDoseEntity;
import com.ImmunizationTracker.api.entities.VaccinationEntity;
import com.ImmunizationTracker.api.externalServices.FHIRServiceImmunization;
import com.ImmunizationTracker.api.models.ImmunizationStatus;
import com.ImmunizationTracker.api.models.VaccinationRecord;
import com.ImmunizationTracker.api.repository.StudentVaccinationDoseScheduleEntityRepository;
import com.ImmunizationTracker.api.repository.VaccinationDoseEntityRepository;
import com.ImmunizationTracker.api.repository.VaccinationEntityRepository;
import javassist.NotFoundException;
import org.hl7.fhir.dstu3.model.Immunization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class VaccinationService {

    @Autowired
    private StudentVaccinationDoseScheduleEntityRepository studentVaccinationDoseScheduleEntityRepository;
    @Autowired
    private VaccinationEntityRepository vaccinationEntityRepository;
    @Autowired
    private VaccinationDoseEntityRepository vaccinationDoseEntityRepository;

    private String fhirServerURL = "http://hapi.fhir.org/baseDstu3";


    public VaccinationRecord[] createVaccinationScheduleForNewStudent(StudentEntity student){
        //1. Get Vaccines that we will track
        Iterable<VaccinationDoseEntity> vaccinations = vaccinationDoseEntityRepository.findAll();

        //2. For each vaccination
        for(VaccinationDoseEntity vaccine : vaccinations){
            //2a. Get Student DOB
            LocalDate studentDob = student.getDob();
            String doseSchedule = vaccine.getSchedule();

            //2b. Get VaccinationSchedule
            LocalDate recommendedDate = getRecommendedDateForVaccine(studentDob, doseSchedule);

            StudentVaccinationDoseScheduleEntity entity = new StudentVaccinationDoseScheduleEntity();
            entity.setStudentEntity(student);
            entity.setVaccinationDoseEntity(vaccine);
            entity.setRecommendedDate(recommendedDate);

            studentVaccinationDoseScheduleEntityRepository.save(entity);
        }

        //3. Get Vaccinations and return VaccinationRecord[]
        VaccinationRecord[] records = getVaccinationRecordsForStudent(student.getStudentId(), student.getPatientId());
        return records;
    }

    public VaccinationRecord[] getVaccinationRecordsForStudent(Integer studentId, String patientId) {
        ArrayList<VaccinationRecord> records = new ArrayList<VaccinationRecord>();

        Iterable<StudentVaccinationDoseScheduleEntity> entities = studentVaccinationDoseScheduleEntityRepository.findAll();

        for (StudentVaccinationDoseScheduleEntity entity: entities){
            if(entity.getStudentEntity().getStudentId() == studentId) {
                VaccinationRecord record = getVaccinationRecordForStudentVaccinationDoseScheduleEntity(entity);
                records.add(record);
            }
        }

        return records.toArray(new VaccinationRecord[0]);
    }

    public VaccinationRecord getVaccinationRecordForStudentVaccinationDoseScheduleEntity(StudentVaccinationDoseScheduleEntity entity){
        String immunizationRecordId = entity.getImmunizationRecordId();

        VaccinationRecord record = null;

        //if this is null, set Status to Incomplete or OVERDUE, completed date and notes to null
        //if this is not null, set Status to Complete, completed date and notes from server object
        if (immunizationRecordId == null) {

            record = new VaccinationRecord(
                    entity.getStudentVaccinationDoseScheduleId(),
                    immunizationRecordId,
                    entity.getVaccinationDoseEntity().getFullName(),
                    translateNewImmunizationStatusForDate(entity.getRecommendedDate()) ,
                    entity.getRecommendedDate(),
                    null,
                    null
            );
        } else {

            FHIRServiceImmunization fhirServiceImmunization = new FHIRServiceImmunization(fhirServerURL);

            String status  = fhirServiceImmunization.getImmunizationStatusByImmunizationID(immunizationRecordId);
            LocalDate completedDate = fhirServiceImmunization.getImmunizationDateByImmunizationID(immunizationRecordId);
            String note = fhirServiceImmunization.getImmunizationNoteByImmunizationID(immunizationRecordId);

            System.out.println("Status from FHIR server is: " + status);
            record = new VaccinationRecord(
                    entity.getStudentVaccinationDoseScheduleId(),
                    immunizationRecordId,
                    entity.getVaccinationDoseEntity().getFullName(),
                    translateImmunizationStatusFromFHIR(status, entity.getRecommendedDate()),
                    entity.getRecommendedDate(),
                    completedDate,
                    note
            );
        }

        return record;
    }

    private ImmunizationStatus translateImmunizationStatusFromFHIR(String FHIRImmunizationStatus, LocalDate recommendedDate){
        String status = (FHIRImmunizationStatus == null) ? "" : FHIRImmunizationStatus.toLowerCase();
        switch(status){
            case("completed"):
                return ImmunizationStatus.COMPLETED;
            default:
                if(recommendedDate.plusMonths(6).isBefore(LocalDate.now())){
                    return ImmunizationStatus.OVERDUE;
                }
                else{
                    return ImmunizationStatus.INCOMPLETE;
                }
        }
    }

    private ImmunizationStatus translateNewImmunizationStatusForDate(LocalDate recommendedDate){
        if(recommendedDate.plusMonths(6).isBefore(LocalDate.now())){
            return ImmunizationStatus.OVERDUE;
        }
        else{
            return ImmunizationStatus.INCOMPLETE;
        }
    }

    public void updateVaccinations(int studentId, String patientId, VaccinationRecord[] updatedRecords) throws NotFoundException {
        VaccinationRecord[] currentServerStateRecords = getVaccinationRecordsForStudent(studentId, patientId);

        FHIRServiceImmunization fhirServiceImmunization = new FHIRServiceImmunization(fhirServerURL);

        for (int i = 0; i < updatedRecords.length; i++) {

            //if updatedRecord.getImmunizationRecordId is null, it is new and create immunization
            if (updatedRecords[i].getImmunizationRecordId() == null){
                String immunizationId = fhirServiceImmunization.addImmunization(patientId);

                StudentVaccinationDoseScheduleEntity entity = studentVaccinationDoseScheduleEntityRepository.findById(updatedRecords[i].getStudentVaccinationDoseScheduleId())
                        .orElse(null);

                if(entity == null) throw new NotFoundException("Could not find StudentVaccinationDoseScheduleEntity with id " + updatedRecords[i].getStudentVaccinationDoseScheduleId());
                entity.setImmunizationRecordId(immunizationId);
                studentVaccinationDoseScheduleEntityRepository.save(entity);

                fhirServiceImmunization.addImmunizationStatus(immunizationId, updatedRecords[i].getStatus().toString());

                if(updatedRecords[i].getVaccinationName() != null) {
                    fhirServiceImmunization.addImmunizationCode(immunizationId, updatedRecords[i].getVaccinationName());
                }

                if(updatedRecords[i].getNote() != null){
                    fhirServiceImmunization.addImmunizationNote(immunizationId, updatedRecords[i].getNote());
                }

                if(updatedRecords[i].getCompletedDate() != null){
                    fhirServiceImmunization.addImmunizationDate(immunizationId, updatedRecords[i].getCompletedDate().toString());
                }

                if(updatedRecords[i].getRecommendedDate() != null) {
                    fhirServiceImmunization.addImmunizationExpiration(immunizationId, updatedRecords[i].getRecommendedDate().toString());
                }

            }
            else{
                String immunizationId = updatedRecords[i].getImmunizationRecordId();

                fhirServiceImmunization.addImmunizationStatus(immunizationId, updatedRecords[i].getStatus().toString());

                if(updatedRecords[i].getNote() != null){
                    String newNote = updatedRecords[i].getNote();
                    String currentNote = currentServerStateRecords[i].getNote();
                    if (!(newNote.equalsIgnoreCase(currentNote))){
                        fhirServiceImmunization.addImmunizationNote(immunizationId, newNote);
                    }
                }

                if(updatedRecords[i].getCompletedDate() != null){
                    LocalDate newDate = updatedRecords[i].getCompletedDate();
                    LocalDate currentDate = currentServerStateRecords[i].getCompletedDate();

                    if (!(newDate.equals(currentDate))) {
                        fhirServiceImmunization.addImmunizationDate(immunizationId, newDate.toString());
                    }
                }

                if(updatedRecords[i].getRecommendedDate() != null) {
                    LocalDate newDate = updatedRecords[i].getRecommendedDate();
                    LocalDate currentDate = currentServerStateRecords[i].getRecommendedDate();

                    if (!(newDate.equals(currentDate))) {
                        fhirServiceImmunization.addImmunizationExpiration(immunizationId, updatedRecords[i].getRecommendedDate().toString());
                    }
                }
            }

        }
    }

    private LocalDate getRecommendedDateForVaccine(LocalDate studentDob, String doseSchedule) {
        Integer timeSpanValue = Integer.parseInt(doseSchedule.substring(0, doseSchedule.length()-1));
        String timeSpanUnit = doseSchedule.substring(doseSchedule.length()-1, doseSchedule.length());

        LocalDate recommendedDate = studentDob;
        switch(timeSpanUnit){
            case "d":
                recommendedDate = studentDob.plusDays(timeSpanValue);
                break;
            case "w":
                recommendedDate = studentDob.plusWeeks(timeSpanValue);
                break;
            case "m":
                recommendedDate = studentDob.plusMonths(timeSpanValue);
                break;
            case "y":
                recommendedDate = studentDob.plusYears(timeSpanValue);
                break;
            default:
                break;
        }
        return recommendedDate;
    }
}
