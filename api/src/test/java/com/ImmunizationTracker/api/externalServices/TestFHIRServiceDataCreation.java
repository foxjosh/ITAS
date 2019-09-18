package com.ImmunizationTracker.api.externalServices;

//import org.junit.jupiter.api.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestFHIRServiceDataCreation extends TestCase {

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
    public TestFHIRServiceDataCreation(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static TestSuite suite() {
        return new TestSuite(TestFHIRServiceDataCreation.class);
    }



    /*
     * Test of Adding a patient and Immunization
     */
    public void testAddPatientImmunizationDataGenerator() {
        FHIRServicePatient adderPatient = new FHIRServicePatient(serverBase);
        FHIRServiceImmunization adderImmunization = new FHIRServiceImmunization(serverBase);
        String patientId = adderPatient.addNewPatient("Prakash", "Bhatta");
        System.out.println("PATIENT ID == "+ patientId);
        adderPatient.addGender(patientId,"MALE");
        adderPatient.addBirthDate(patientId,"2010-10-08");
        adderPatient.addAddress(patientId,"HOME","XXX Rust","Saint Monica","Missouri","631654");
        adderPatient.addEmail(patientId,"HOME","xyz@hotmail.com");
        adderPatient.addPhone(patientId,"HOME","123-456-7866");

        //Immunization 1
        String immunizationId = adderImmunization.addImmunization(patientId);
        System.out.println("Immunization ID == "+ immunizationId);
        adderImmunization.addImmunizationCode(immunizationId,"Measles");
        adderImmunization.addImmunizationDate(immunizationId,"2010-01-01");
        adderImmunization.addImmunizationExpiration(immunizationId,"2005-05-05");
        adderImmunization.addImmunizationExplanation(immunizationId,"Explaination");
        adderImmunization.addImmunizationNote(immunizationId,"Immunization Note");
        adderImmunization.addImmunizationStatus(immunizationId,"completed");

        //Immunization 2
        String immunizationId2 = adderImmunization.addImmunization(patientId);
        System.out.println("Immunization ID 2 == "+ immunizationId2);
        adderImmunization.addImmunizationCode(immunizationId2,"Mumps");
        adderImmunization.addImmunizationDate(immunizationId2,"2010-01-01");
        adderImmunization.addImmunizationExpiration(immunizationId2,"2005-05-05");
        adderImmunization.addImmunizationExplanation(immunizationId2,"Explaination");
        adderImmunization.addImmunizationNote(immunizationId2,"Immunization Note");
        adderImmunization.addImmunizationStatus(immunizationId2,"completed");


        //assertEquals("", "");

    }

}
