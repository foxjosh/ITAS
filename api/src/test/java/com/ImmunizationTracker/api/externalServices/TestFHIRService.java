package com.ImmunizationTracker.api.externalServices;

//import org.junit.jupiter.api.Test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;


public class TestFHIRService extends TestCase {

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
    public TestFHIRService(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TestFHIRService.class);
    }

    /*
     * Test of creating a new patient with only the name.
     */
    
    public void testAddPatient() {
        FHIRService adder = new FHIRService(serverBase);
        String newGivenName = "John";
        String newFamilyName = "Doe";
        String pid = adder.addNewPatient( newGivenName, newFamilyName);
        String givenName = adder.getGivenName(pid);
        String familyName = adder.getFamilyName(pid);
        String expectedName = "John Doe";
        String actualName = givenName + " " + familyName;
        System.out.println(pid);
        assertEquals(expectedName, actualName);
    }

    /*
     * Test of getting the given name from the patientID
     */
    
    public void testGetGivenName() {
        FHIRService reader = new FHIRService(serverBase);
        String expectedGivenName = "John";
        String actualGivenName = reader.getGivenName(patientID);
        assertEquals(expectedGivenName, actualGivenName);
    }

    /*
     * Test of getting the family name from the patientID
     */
   
    public void testGetFamilyName() {
        FHIRService reader = new FHIRService(serverBase);
        String expectedFamilyName = "Doe";
        String actualFamilyName = reader.getFamilyName(patientID);
        assertEquals(expectedFamilyName, actualFamilyName);
    }

    /*
     * Test of adding the gender to the patientID
     */
    ////@Test
    public void testAddGender() {
        FHIRService adder = new FHIRService(serverBase);
        String gender = "MALE";
        String expectedGender = "MALE";
        String actualGender = adder.addGender(patientID, gender);
        assertEquals(expectedGender, actualGender);
    }

    /*
     * Test of getting the gender from the patientID
     */
    ////@Test
    public void testGetGender() {
        FHIRService reader = new FHIRService(serverBase);
        String expectedGender = "MALE";
        String actualGender = reader.getGender(patientID);
        assertEquals(expectedGender, actualGender);
    }

    /*
     * Test of adding the birth date to the patientID
     */
    ////@Test
    public void testAddBirthDate() {
        FHIRService adder = new FHIRService(serverBase);
        String birthDate = "1960-10-30";
        String expectedBirthDate = "1960-10-29" + "T19:30:00Z";
        String actualBirthDate = adder.addBirthDate(patientID, birthDate);
        assertEquals(expectedBirthDate, actualBirthDate);
    }

    /*
     * Test of getting the birth date from the patientID
     */
    //@Test
    public void testGetBirthday() {
        FHIRService reader = new FHIRService(serverBase);
        String expectedBirthday = "1960-10-29T19:30:00Z";
        String actualBirthday = reader.getBirthDate(patientID);
        assertEquals(expectedBirthday, actualBirthday);
    }

    /*
     * Test of adding the address to the patientID
     */
    //@Test
    public void testAddAddress() {
        FHIRService adder = new FHIRService(serverBase);
        String use = "HOME";
        String street = "123 Drive";
        String city = "Atlanta";
        String state = "GA";
        String zip = "12345";
        List<String> expectedAddress = Arrays.asList("123 Drive", "Atlanta", "GA", "12345");
        List actualAddress = adder.addAddress(patientID, use, street, city, state, zip);
        assertEquals(expectedAddress, actualAddress);
    }

    /*
     * Test of getting the address from the patientID
     */
    //@Test
    public void testGetAddress() {
        FHIRService reader = new FHIRService(serverBase);
        List<String> expectedAddress = Arrays.asList("123 Drive", "Atlanta", "GA", "12345");
        List actualAddress = reader.getAddress(patientID);
        assertEquals(expectedAddress, actualAddress);
    }

    /*
     * Test of adding the phone number to the patientID
     */
    //@Test
    public void testAddPhone() {
        FHIRService adder = new FHIRService(serverBase);
        String use = "HOME";
        String value = "888-888-8888";
        List<String> expectedPhone = Arrays.asList("HOME", "888-888-8888");
        List actualPhone = adder.addPhone(patientID, use, value);
        assertEquals(expectedPhone, actualPhone);
    }

    /*
     * Test of getting the phone number(s) from the patientID
     */
    //@Test
    public void testGetPhone() {
        FHIRService reader = new FHIRService(serverBase);
        List<String> expectedPhone = Arrays.asList("HOME", "888-888-8888");
        List actualPhone = reader.getPhone(patientID);
        assertEquals(expectedPhone, actualPhone);
    }

    /*
     * Test of adding the email to the patientID
     */
    //@Test
    public void testAddEmail() {
        FHIRService adder = new FHIRService(serverBase);
        String use = "HOME";
        String value = "email@email.com";
        List<String> expectedEmail = Arrays.asList("HOME", "email@email.com");
        List actualEmail = adder.addEmail(patientID, use, value);
        assertEquals(expectedEmail, actualEmail);
    }

    /*
     * Test of getting the email(s) from the patientID
     */
    //@Test
    public void testGetEmail() {
        FHIRService reader = new FHIRService(serverBase);
        List<String> expectedEmail = Arrays.asList("HOME", "email@email.com");
        List actualEmail = reader.getEmail(patientID);
        assertEquals(expectedEmail, actualEmail);
    }

    //@Test
    public void testAddImmunization() {
        FHIRService adder = new FHIRService(serverBase);
        String id = adder.addImmunization(patientID);
        System.out.println(id);
    }

    //@Test
    public void testAddImmunizationCode() {
        FHIRService adder = new FHIRService(serverBase);
        String system = "http://hl7.org/fhir/sid/cvx";
        String code = "05";
        String immunizationName = "Measles";
        adder.addImmunizationCode(immunizationID, system, code, immunizationName);
    }

    //@Test
    public void testAddImmunizationDate() throws ParseException {
        FHIRService adder = new FHIRService(serverBase);
        String date = "2018-11-19";
        adder.addImmunizationDate(immunizationID, date);
    }

    //@Test
    public void testAddImmunizationExpiration() {
        FHIRService adder = new FHIRService(serverBase);
        String date = "2020-06-19";
        adder.addImmunizationExpiration(immunizationID, date);
    }

    //@Test
    public void testAddImmunizationStatus() {
        FHIRService adder = new FHIRService(serverBase);
        String status = "completed";
        adder.addImmunizationStatus(immunizationID, status);
    }

    //@Test
    public void testAddImmunizationNote() {
        FHIRService adder = new FHIRService(serverBase);
        String note = "This is a test of a note";
        adder.addImmunizationNote(immunizationID, note);
    }

    //@Test
    public void testAddImmunizationExplanation() {
        FHIRService adder = new FHIRService(serverBase);
        String explanation = "This is a test explanation";
        adder.addImmunizationExplanation(immunizationID, explanation);
    }
}
