package com.ImmunizationTracker.api.externalServices;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestFHIRServiceImmunization extends TestCase {

    /* **************************************************************************************************************
     * ***************************************************************************************************************
     * PatientID and immunizationID needs to be changed to a newly created patient and immunization. Run testAddPatient
     * and testAddImmunization first, get the returned id, place returned id. Example: after running testAddPatient
     * only you get 510760. Replace the patientID variable with this. This keeps from redundant information being added
     * to patients file such as address, phone numbers and emails.
     * ***************************************************************************************************************
     * ***************************************************************************************************************/
    String patientID = "530517";
    String immunizationID = "530518";


    /*
     * Use this URL, along with /Patient/patientID or Immunization/immunizationID to see the live FHIR record of the
     * patient or immunization. For example: http://hapi.fhir.org/baseDstu3/Patient/510750/
     */
    String serverBase = "http://hapi.fhir.org/baseDstu3";


    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestFHIRServiceImmunization(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static TestSuite suite() {
        return new TestSuite(TestFHIRServiceImmunization.class);
    }


    //@Test
    public void testAddImmunization() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String id = adder.addImmunization(patientID);
        System.out.println(id);
    }

    //@Test
    public void testAddImmunizationCode() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String system = "http://hl7.org/fhir/sid/cvx";
        String code = "05";
        String immunizationName = "Measles";
        adder.addImmunizationCode(immunizationID, immunizationName);
    }

    //@Test
    public void testAddImmunizationDate() throws ParseException {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String date = "2018-11-19";
        adder.addImmunizationDate(immunizationID, date);
    }

    //@Test
    public void testAddImmunizationExpiration() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String date = "2020-06-19";
        adder.addImmunizationExpiration(immunizationID, date);
    }

    //@Test
    public void testAddImmunizationStatus() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String status = "completed";
        adder.addImmunizationStatus(immunizationID, status);
    }

    //@Test
    public void testAddImmunizationNote() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String note = "This is a test of a note";
        adder.addImmunizationNote(immunizationID, note);
    }

    //@Test
    public void testAddImmunizationExplanation() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String explanation = "This is a test explanation";
        adder.addImmunizationExplanation(immunizationID, explanation);
    }

    /*
    * Test of adding a new immunization to a patient.
    */
    public void testGetImmunizationForPatients() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String patientId = "528839";
        List actualImmunizationLists = adder.getImmunizationsForPatient( patientId);
        String actualImmunizationListsString = actualImmunizationLists.toString();
        //String immunizationResourceId = adder.addImmunizationToPatient(pid);
        System.out.println("Immunization Resource IDs == " + actualImmunizationLists.toString());
        String expectedImmunizationLists = "[528841, 528842, 528843]";
        assertEquals(actualImmunizationListsString, expectedImmunizationLists);
    }

    /*
    * Test of getting the hashmap of Immunization Status for a patient.
    */
    public void testGetImmunizationStatusOfPatient() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String patientId = "528839";
        Map<String, String> actualImmunizationLists = adder.getImmunizationStatusOfPatient( patientId);
        Map<String, String> expectedImmunizationLists = new HashMap<String, String>();
        expectedImmunizationLists.put("528842","COMPLETED");
        expectedImmunizationLists.put("528841","COMPLETED");
        expectedImmunizationLists.put("528843","COMPLETED");
        assertEquals(actualImmunizationLists, expectedImmunizationLists);
    }

    /*
    * Test of getting the hashmap of Immunization Status for a patient.
    */
    public void testGetImmunizationVaccineCodeSystemOfPatient() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String patientId = "528839";
        Map<String, String> actualImmunizationLists = adder.getImmunizationVaccineCodeSystemOfPatient(patientId);
        Map<String, String> expectedImmunizationLists = new HashMap<String, String>();
        //{528842=http://hl7.org/fhir/sid/cvx, 528841=thttp://hl7.org/fhir/sid/cvx, 528843=http://hl7.org/fhir/sid/cvx}
        expectedImmunizationLists.put("528842","http://hl7.org/fhir/sid/cvx");
        expectedImmunizationLists.put("528841","thttp://hl7.org/fhir/sid/cvx");
        expectedImmunizationLists.put("528843","http://hl7.org/fhir/sid/cvx");
        assertEquals(actualImmunizationLists, expectedImmunizationLists);
    }

    /*
    * Test of getting the hashmap of Immunization Status for a patient.
    */
    public void testGetImmunizationVaccineCode_CodeOfPatient() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String patientId = "528839";
        Map<String, String> actualImmunizationLists = adder.getImmunizationVaccineCode_CodeOfPatient(patientId);
        Map<String, String> expectedImmunizationLists = new HashMap<String, String>();
        expectedImmunizationLists.put("528842","104");
        expectedImmunizationLists.put("528841","05");
        expectedImmunizationLists.put("528843","104");
        assertEquals(actualImmunizationLists, expectedImmunizationLists);
    }

    /*
    * Test of getting the hashmap of Immunization Vaccine Code Display for a patient.
    */
    public void testGetImmunizationVaccineCode_DisplayOfPatient() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String patientId = "528839";
        Map<String, String> actualImmunizationLists = adder.getImmunizationVaccineCode_DisplayOfPatient(patientId);
        Map<String, String> expectedImmunizationLists = new HashMap<String, String>();
        expectedImmunizationLists.put("528842","Hep A-Hep B");
        expectedImmunizationLists.put("528841","measles");
        expectedImmunizationLists.put("528843","Hep A-Hep B");
        assertEquals(actualImmunizationLists, expectedImmunizationLists);

    }

    /*
    * Test of getting the hashmap of Immunization Vaccination Date for a patient.
    */
    public void testGetImmunizationVaccinationDateOfPatient() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String patientId = "528839";
        Map<String, String> actualImmunizationLists = adder.getImmunizationVaccinationDateOfPatient(patientId);
        Map<String, String> expectedImmunizationLists = new HashMap<String, String>();
        expectedImmunizationLists.put("528842","Mon Nov 19 13:56:55 CST 2018");
        expectedImmunizationLists.put("528841","Mon Nov 19 13:56:55 CST 2018");
        expectedImmunizationLists.put("528843","Mon Nov 19 13:56:55 CST 2018");
        assertEquals(actualImmunizationLists, expectedImmunizationLists);

    }

    /*
    * Test of getting the hashmap of Immunization Vaccination Expiration Date for a patient.
    */
    public void testGetImmunizationVaccinationExpirationDateOfPatient() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String patientId = "528839";
        Map<String, String> actualImmunizationLists = adder.getImmunizationVaccinationExpirationDateOfPatient(patientId);
        Map<String, String> expectedImmunizationLists = new HashMap<String, String>();
        expectedImmunizationLists.put("528842","Mon Nov 19 00:00:00 CST 2018");
        expectedImmunizationLists.put("528841","Mon Nov 19 00:00:00 CST 2018");
        expectedImmunizationLists.put("528843","Mon Nov 19 00:00:00 CST 2018");
        assertEquals(actualImmunizationLists, expectedImmunizationLists);

    }

    /*
    * Test of getting the hashmap of Immunization Vaccination Note for a patient.
    */
    public void testGetImmunizationVaccinationNoteOfPatient() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String patientId = "528839";
        Map<String, String> actualImmunizationLists = adder.getImmunizationVaccinationNoteOfPatient(patientId);
        Map<String, String> expectedImmunizationLists = new HashMap<String, String>();
        expectedImmunizationLists.put("528842","On antibotics conflict");
        expectedImmunizationLists.put("528841","On antibotics conflict");
        expectedImmunizationLists.put("528843","On antibotics conflict");
        assertEquals(actualImmunizationLists, expectedImmunizationLists);

    }

    /*
    * Test of deleting a Immunization DI
    */
    public void testDeleteImmunization() {
        FHIRServiceImmunization adder = new FHIRServiceImmunization(serverBase);
        String immunizationID = "547768";
        adder.deleteImmunization(immunizationID);
        assertEquals("", "");

    }
}
