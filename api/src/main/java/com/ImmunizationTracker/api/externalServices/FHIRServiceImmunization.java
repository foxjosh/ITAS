package com.ImmunizationTracker.api.externalServices;

import com.ImmunizationTracker.api.models.PatientInformation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import org.hl7.fhir.dstu3.model.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;

/*
 * Immunization methods start
 */

public class FHIRServiceImmunization {

    IGenericClient client;

    public FHIRServiceImmunization() {

    }

    public FHIRServiceImmunization(String baseUrl) {
        FhirContext ctx = FhirContext.forDstu3();
        client = ctx.newRestfulGenericClient(baseUrl);
    }


    /*
     * Immunization add / get methods
     */

    /**
     * Add a new immunization record to the patient and return the immunization id
     *
     * @param patientID (String) id of the patient
     * @return (String) id of the immunization
     */
    public String addImmunization(String patientID) {

        IdType immunizationID = null;

        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(patientID).execute();

            Immunization immunization = new Immunization();
            immunization.setPatient(new Reference(patient.getId()));

            MethodOutcome outcome = client.create().resource(immunization).execute();

            immunizationID = (IdType) outcome.getId();
        } catch (Exception e) {
            System.out.println("An error occurred trying to add new immunization:");
            e.printStackTrace();
        }

        return immunizationID.getIdPart();
    }

    /**
     * Add the status to the immunization record
     * <p>
     * While it makes since to only use completed, I have included the 3 enum possibilities.
     * <p>
     * Inputs should be (completed, error, null)
     *
     * @param immunizationID (String) id of the immunization linked to patient
     */
    public void addImmunizationStatus(String immunizationID, String status) {


        try {
            Immunization immunization = client.read().resource(Immunization.class).withId(immunizationID).execute();

            if (status.equalsIgnoreCase("completed")) {
                immunization.setStatus(Immunization.ImmunizationStatus.COMPLETED);
            } else {
                immunization.setStatus(Immunization.ImmunizationStatus.ENTEREDINERROR);
            }

            client.update().resource(immunization).execute();
        } catch (Exception e) {
            System.out.println("An error occurred trying to add new immunization status: " + status);
            e.printStackTrace();
        }
    }

    /**
     * Add the immunization code information to the immunization record
     * <p>
     * Need to do more research to find out the proper code and immunization name to apply here
     *
     * @param immunizationID   (String) id of the immunization linked to patient
     * @param immunizationName (String) name of the immunization administered
     */
    public void addImmunizationCode(String immunizationID,String immunizationName) {

        try {
            Immunization immunization = client.read().resource(Immunization.class).withId(immunizationID).execute();

            CodeableConcept cpt = new CodeableConcept();
            Coding coding = new Coding();
            coding.setDisplay(immunizationName);


            cpt.getCoding().add(coding);
            immunization.setVaccineCode(cpt);

            client.update().resource(immunization).execute();
        } catch (Exception e) {
            System.out.println("An error occurred trying to add new immunization code:");
            e.printStackTrace();
        }
    }

    /**
     * Add the date immunization administered to the immunization record
     * <p>
     * Date format is YYYY-MM-DD, standard FHIR date format
     *
     * @param immunizationID (String) id of the immunization linked to patient
     * @param date           (String) date immunization administered
     */
    public void addImmunizationDate(String immunizationID, String date) {

        try {
            Immunization immunization = client.read().resource(Immunization.class).withId(immunizationID).execute();
            immunization.setDate(java.sql.Date.valueOf(date));

            client.update().resource(immunization).execute();
        } catch (Exception e) {
            System.out.println("An error occurred trying to add new immunization date:");
            e.printStackTrace();
        }
    }

    /**
     * Add the date immunization expires to the immunization record
     * <p>
     * Date format is YYYY-MM-DD, standard FHIR date format
     *
     * @param immunizationID (String) id of the immunization linked to patient
     * @param date           (String) date of the immunization expiration
     */
    public void addImmunizationExpiration(String immunizationID, String date) {

        try {
            Immunization immunization = client.read().resource(Immunization.class).withId(immunizationID).execute();
            immunization.setExpirationDate(java.sql.Date.valueOf(date));

            client.update().resource(immunization).execute();
        } catch (Exception e) {
            System.out.println("An error occurred trying to add new immunization expiration:");
            e.printStackTrace();
        }
    }

    /**
     * Add an explanation to the immunization record
     * <p>
     * Not sure the difference between explanation and note but both are included
     *
     * @param immunizationID (String) id of the immunization linked to patient
     * @param explanation    (String) text of the explanation
     */
    public void addImmunizationExplanation(String immunizationID, String explanation) {

        try {
            Immunization immunization = client.read().resource(Immunization.class).withId(immunizationID).execute();
            Immunization.ImmunizationExplanationComponent explanationComponent = new Immunization.ImmunizationExplanationComponent();
            explanationComponent.addReason().setText(explanation);
            immunization.setExplanation(explanationComponent);

            client.update().resource(immunization).execute();
        } catch (Exception e) {
            System.out.println("An error occurred trying to add new immunization explanation:");
            e.printStackTrace();
        }
    }

    /**
     * Add a note to the immunization record
     * <p>
     * Not sure the difference between note and explanation but both are included
     *
     * @param immunizationID (String) id of the immunization linked to patient
     * @param note           (String) text of the note
     */
    public void addImmunizationNote(String immunizationID, String note) {

        try {
            Immunization immunization = client.read().resource(Immunization.class).withId(immunizationID).execute();
            List<Annotation> notes = immunization.getNote();
            if(notes == null || notes.size() == 0){
                immunization.addNote().setText(note);
            }
            else {
                immunization.getNote().get(0).setText(note);
            }
            client.update().resource(immunization).execute();
            System.out.println(immunization.getNote().get(0).getText());
        } catch (Exception e) {
            System.out.println("An error occurred trying to add new status note:");
            e.printStackTrace();
        }
    }



    /**
     * This is a Script to generate and it is much slower than getImmunizationsForPatient Method
     * Get Immunization Status from Immunization ID -- Script
     * @param patientID  (String) ID of the patient
     * @return (String) return status resource id of the patient
     */

    public List getImmunizationsForPatientScript(String patientID) {

        String immunizationStatus = "";
        Bundle bundle = (Bundle) client.search().forResource(Immunization.class)
                .prettyPrint()
                .execute();

        List<Bundle.BundleEntryComponent> urls = bundle.getEntry();

        List<String> immunizationList = new ArrayList<String>();

        for (Bundle.BundleEntryComponent url : urls){
            String observationType = url.getResource().fhirType().toString();
            if(observationType.equals("Immunization")){
                String immID = url.getResource().getIdElement().getIdPart().toString();
                Immunization imm = client.read().resource(Immunization.class).withId(immID).execute();
                if (imm.getPatient().getReference() != null){
                    String patientImmunizationRefrence = imm.getPatient().getReference().toString();
                    if ((patientImmunizationRefrence != null) && (patientImmunizationRefrence != "")
                            && (imm.getPatient().getReference().substring(8).equals(patientID))) {
                        immunizationList.add(immID);
                    }
                }
            }


        }
        do {
            Bundle nextPage = client.loadPage().next(bundle).execute();
            List<Bundle.BundleEntryComponent> nexturls = nextPage.getEntry();
            for (Bundle.BundleEntryComponent nexturl : nexturls){
                String observationType2 = nexturl.getResource().fhirType().toString();
                if(observationType2.equals("Immunization")){
                    if(observationType2.equals("Immunization")) {
                        String immID = nexturl.getResource().getIdElement().getIdPart().toString();
                        Immunization imm = client.read().resource(Immunization.class).withId(immID).execute();
                        if (imm.getPatient().getReference() != null){
                            String patientImmunizationRefrence = imm.getPatient().getReference().toString();
                            if ((patientImmunizationRefrence != null) && (patientImmunizationRefrence != "")
                                    && (imm.getPatient().getReference().substring(8).equals(patientID))) {
                                immunizationList.add(immID);
                            }
                        }

                    }

                }
            }
            bundle = nextPage;
        }
        while (bundle.getLink(Bundle.LINK_NEXT) != null);
        return immunizationList;
    }

    /**
     * Get Immunization Status from Immunization ID -- Bundle method
     * @param patientID  (String) ID of the patient
     * @return (String) return status resource id of the patient
     */

    public List getImmunizationsForPatient(String patientID) {

        String immunizationStatus = "";
        Patient patient = client.read().resource(Patient.class).withId(patientID).execute();
        String urlVal = "http://hapi.fhir.org/baseDstu3/Patient/" + patientID;
        Bundle bundle = (Bundle) client.search().forResource(Immunization.class)
                .where(new ReferenceClientParam("patient").hasId(urlVal))
                .returnBundle(Bundle.class)
                .include(new Include("Immunization:patient"))
                .prettyPrint()
                .execute();

        List<Bundle.BundleEntryComponent> urls = bundle.getEntry();

        List<String> immunizationList = new ArrayList<String>();

        for (Bundle.BundleEntryComponent url : urls){
            String observationType = url.getResource().fhirType().toString();
            if(observationType.equals("Immunization")){
                String immID = url.getResource().getIdElement().getIdPart().toString();
                Immunization imm = client.read().resource(Immunization.class).withId(immID).execute();
                if (imm.getPatient().getReference() != null){
                    String patientImmunizationRefrence = imm.getPatient().getReference().toString();
                    if ((patientImmunizationRefrence != null) && (patientImmunizationRefrence != "")
                            && (imm.getPatient().getReference().substring(8).equals(patientID))) {
                        immunizationList.add(immID);
                    }
                }
            }


        }
//        do {
//            Bundle nextPage = client.loadPage().next(bundle).execute();
//            List<Bundle.BundleEntryComponent> nexturls = nextPage.getEntry();
//            for (Bundle.BundleEntryComponent nexturl : nexturls){
//                String observationType2 = nexturl.getResource().fhirType().toString();
//                if(observationType2.equals("Immunization")){
//                    if(observationType2.equals("Immunization")) {
//                        String immID = nexturl.getResource().getIdElement().getIdPart().toString();
//                        Immunization imm = client.read().resource(Immunization.class).withId(immID).execute();
//                        if (imm.getPatient().getReference() != null){
//                            String patientImmunizationRefrence = imm.getPatient().getReference().toString();
//                            if ((patientImmunizationRefrence != null) && (patientImmunizationRefrence != "")
//                                    && (imm.getPatient().getReference().substring(8).equals(patientID))) {
//                                immunizationList.add(immID);
//                            }
//                        }
//
//                    }
//
//                }
//            }
//            bundle = nextPage;
//        }
//        while (bundle.getLink(Bundle.LINK_NEXT) != null);

        return immunizationList;
    }


    //  Gets the single status for the Immunization by the immunization ID
    public String getImmunizationStatusByImmunizationID (String immunizationID) {

        String immunizationStatus = "";

        try {
            Immunization immunization = client.read().resource(Immunization.class).withId(immunizationID).execute();
            immunizationStatus = (immunization.getStatus() != null) ? immunization.getStatus().toString() : "";

        } catch (Exception e) {
            System.out.println("An error occurred trying to add new immunization date:");
            e.printStackTrace();
        }

        return immunizationStatus;
    }


    public Immunization getRealImmunizationsForPatient(String immunizationID) {
        Immunization realImmunization = null;
        try {
            realImmunization = client.read().resource(Immunization.class).withId(immunizationID).execute();
        } catch (Exception e) {
            System.out.println("An error while getting real Immunization");
            e.printStackTrace();
        }
        return realImmunization;
    }


    /**
     * Gets immunizations Status of a patient
     *
     * @param patientID (String) ID of the patient
     * @return (HashMap) return immunization status of id of the patient
     */
    public Map<String, String> getImmunizationStatusOfPatient(String patientID) {

        Map<String, String> immunizationStatusMap = null;
        try {
            List immunizationLists = getImmunizationsForPatient(patientID);
            immunizationStatusMap = new HashMap<String, String>();
            for (int i = 0; i < immunizationLists.size(); i++) {
                String immunizationIDVal = immunizationLists.get(i).toString();
                Immunization imm = client.read().resource(Immunization.class).withId(immunizationIDVal).execute();
                immunizationStatusMap.put(immunizationIDVal, imm.getStatus().toString());
            }
            //System.out.println("ImmunizationStausMap == " + immunizationStatusMap.toString());
        } catch (Exception e) {
            System.out.println("An error while adding up immunization Status");
            e.printStackTrace();
        }
        return immunizationStatusMap;
    }

    /**
     * Gets immunizations Vaccine Code System of a patient
     *
     * @param patientID (String) ID of the patient
     * @return (HashMap) return HashMap of Immunization id and VaccinationCodeSystem for id of the patient
     */

    public Map<String, String> getImmunizationVaccineCodeSystemOfPatient(String patientID) {

        Map<String, String> immunizationVaccineCodeSystem = null;
        try {
            List immunizationLists = getImmunizationsForPatient(patientID);
            immunizationVaccineCodeSystem = new HashMap<String, String>();
            for (int i = 0; i < immunizationLists.size(); i++) {
                String immunizationIDVal = immunizationLists.get(i).toString();
                Immunization imm = client.read().resource(Immunization.class).withId(immunizationIDVal).execute();
                //System.out.println("Immunization Resource ID == " + imm.toString());
                String vaccineCodeSystem = imm.getVaccineCode().getCoding().get(0).getSystem().toString();
                //System.out.println("Immunization SystemCode == " + vaccineCodeSystem);
                immunizationVaccineCodeSystem.put(immunizationIDVal, vaccineCodeSystem);

            }
            //System.out.println("ImmunizationStausMap == " + immunizationVaccineCodeSystem.toString());
        } catch (Exception e) {
            System.out.println("An error while getting Vaccine Code System!!");
            e.printStackTrace();
        }
        return immunizationVaccineCodeSystem;
    }


    /**
     * Gets immunizations Vaccine Code - Code of a patient
     *
     * @param patientID (String) ID of the patient
     * @return (HashMap) return HashMap of Immunization id and VaccinationCode-Code for id of the patient
     */

    public Map<String, String> getImmunizationVaccineCode_CodeOfPatient(String patientID) {

        Map<String, String> immunizationVaccineCode_Code = null;
        try {
            List immunizationLists = getImmunizationsForPatient(patientID);
            immunizationVaccineCode_Code = new HashMap<String, String>();
            for (int i = 0; i < immunizationLists.size(); i++) {
                String immunizationIDVal = immunizationLists.get(i).toString();
                Immunization imm = client.read().resource(Immunization.class).withId(immunizationIDVal).execute();
                //System.out.println("Immunization Resource ID == " + imm.toString());
                String vaccineCodeCode = imm.getVaccineCode().getCoding().get(0).getCode();
                //System.out.println("Immunization SystemCode == " + vaccineCodeCode);

                immunizationVaccineCode_Code.put(immunizationIDVal, vaccineCodeCode);

            }
            //System.out.println("ImmunizationStausMap == " + immunizationVaccineCode_Code.toString());
        } catch (Exception e) {
            System.out.println("An error while getting VaccineCode Code!!");
            e.printStackTrace();
        }
        return immunizationVaccineCode_Code;
    }

    /**
     * Gets immunizations Vaccine Code - Display of a patient
     *
     * @param patientID (String) ID of the patient
     * @return (HashMap) return HashMap of Immunization id and VaccinationCode-Display for id of the patient
     */

    public Map<String, String> getImmunizationVaccineCode_DisplayOfPatient(String patientID) {

        Map<String, String> immunizationVaccineCode_Display = null;
        try {
            List immunizationLists = getImmunizationsForPatient(patientID);
            immunizationVaccineCode_Display = new HashMap<String, String>();
            for (int i = 0; i < immunizationLists.size(); i++) {
                String immunizationIDVal = immunizationLists.get(i).toString();
                Immunization imm = client.read().resource(Immunization.class).withId(immunizationIDVal).execute();
                //System.out.println("Immunization Resource ID == " + imm.toString());
                String vaccineCodeDisplay = imm.getVaccineCode().getCoding().get(0).getDisplay().toString();
                //System.out.println("Immunization SystemCode == " + vaccineCodeDisplay);
                immunizationVaccineCode_Display.put(immunizationIDVal, vaccineCodeDisplay);

            }
            //System.out.println("ImmunizationStausMap == " + immunizationVaccineCode_Display.toString());
        } catch (Exception e) {
            System.out.println("An error while getting VaccineCode Display!!");
            e.printStackTrace();
        }
        return immunizationVaccineCode_Display;
    }

    //  Gets the single date for the Immunization by the immunization ID
    public LocalDate getImmunizationDateByImmunizationID (String immunizationID) {
        LocalDate date = null;

        try {
            Immunization immunization = client.read().resource(Immunization.class).withId(immunizationID).execute();

            Date recommendDate = immunization.getDate();
            date = recommendDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


        } catch (Exception e) {
            System.out.println("An error occurred trying to add new immunization date:");
            e.printStackTrace();
        }

        return date;

    }

    /**
     * Gets immunizations Vaccination Date of a patient
     *
     * @param patientID (String) ID of the patient
     * @return (HashMap) return HashMap of Immunization id and Vaccination Date for id of the patient
     */

    public Map<String, String> getImmunizationVaccinationDateOfPatient(String patientID) {

        Map<String, String> immunizationVaccinationDate = null;
        try {
            List immunizationLists = getImmunizationsForPatient(patientID);
            immunizationVaccinationDate = new HashMap<String, String>();
            for (int i = 0; i < immunizationLists.size(); i++) {
                String immunizationIDVal = immunizationLists.get(i).toString();
                Immunization imm = client.read().resource(Immunization.class).withId(immunizationIDVal).execute();
                String vaccinationDate = imm.getDate().toString();
                //System.out.println("Immunization Vaccine Date == " + vaccinationDate);
                immunizationVaccinationDate.put(immunizationIDVal, vaccinationDate);
            }
            //System.out.println("ImmunizationVaccineDateMap == " + immunizationVaccinationDate.toString());
        } catch (Exception e) {
            System.out.println("An error while getting Vaccination Date!!");
            e.printStackTrace();
        }
        return immunizationVaccinationDate;
    }


    /**
     * Gets immunizations Vaccine Expiration Date of a patient
     *
     * @param patientID (String) ID of the patient
     * @return (HashMap) return HashMap of Immunization id and Vaccination Expiration Date for id of the patient
     */

    public Map<String, String> getImmunizationVaccinationExpirationDateOfPatient(String patientID) {

        Map<String, String> immunizationVaccinationExpirationDate = null;
        try {
            List immunizationLists = getImmunizationsForPatient(patientID);
            immunizationVaccinationExpirationDate = new HashMap<String, String>();
            for (int i = 0; i < immunizationLists.size(); i++) {
                String immunizationIDVal = immunizationLists.get(i).toString();
                Immunization imm = client.read().resource(Immunization.class).withId(immunizationIDVal).execute();
                String vaccinationExpirationDate = imm.getExpirationDate().toString();
                //System.out.println("Immunization Vaccine Expiration Date == " + vaccinationExpirationDate);
                immunizationVaccinationExpirationDate.put(immunizationIDVal, vaccinationExpirationDate);
            }
            //System.out.println("ImmunizationVaccineExpirationDateMap == " + immunizationVaccinationExpirationDate.toString());
        } catch (Exception e) {
            System.out.println("An error while getting Vaccination Expiration Date!!");
            e.printStackTrace();
        }
        return immunizationVaccinationExpirationDate;
    }


    //  Gets the single note for the Immunization by the immunization ID
    public String getImmunizationNoteByImmunizationID (String immunizationID) {

        String immunizationNote = "";

        try {
            Immunization immunization = client.read().resource(Immunization.class).withId(immunizationID).execute();
            immunizationNote = immunization.getNote().get(0).getText();

        } catch (Exception e) {
            System.out.println("An error occurred trying to getting note:");
            e.printStackTrace();
        }

        return immunizationNote;
    }

    /**
     * Gets immunizations Vaccination NOTE Date of a patient
     *
     * @param patientID (String) ID of the patient
     * @return (HashMap) return HashMap of Immunization id and Vaccination NOTE for id of the patient
     */

    public Map<String, String> getImmunizationVaccinationNoteOfPatient(String patientID) {

        Map<String, String> immunizationVaccinationNote = null;
        try {
            List immunizationLists = getImmunizationsForPatient(patientID);
            immunizationVaccinationNote = new HashMap<String, String>();
            for (int i = 0; i < immunizationLists.size(); i++) {
                String immunizationIDVal = immunizationLists.get(i).toString();
                Immunization imm = client.read().resource(Immunization.class).withId(immunizationIDVal).execute();
                String vaccinationNote = imm.getExplanation().getReason().get(0).getText();
                //System.out.println("Immunization Vaccine Expiration Date == " + vaccinationNote);
                immunizationVaccinationNote.put(immunizationIDVal, vaccinationNote);
            }
            //System.out.println("ImmunizationVaccinationNote == " + immunizationVaccinationNote.toString());
        } catch (Exception e) {
            System.out.println("An error while getting Vaccination Note!!");
            e.printStackTrace();
        }
        return immunizationVaccinationNote;
    }

    /**
     * Delete Immunization
     * @param immunizationID (String) ID of the Immunization
     */

    public void deleteImmunization(String immunizationID) {
        Immunization immunization = client.read().resource(Immunization.class).withId(immunizationID).execute();
        IIdType id = immunization.getIdElement();
        client.delete().resourceById(id).execute();
        //client.delete().resourceById(new IdDt("Immunization", immunizationID)).execute();
    }


}
