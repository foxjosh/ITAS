package com.ImmunizationTracker.api.externalServices;

import com.ImmunizationTracker.api.models.PatientInformation;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import org.hl7.fhir.instance.model.api.IIdType;
import java.util.HashMap;
import java.util.Map;




/*
 * Patient methods start at line: 46
 * Immunization methods start at line: 426
 */

public class FHIRServicePatient {

    IGenericClient client;

    public FHIRServicePatient() {

    }

    public FHIRServicePatient(String baseUrl) {
        FhirContext ctx = FhirContext.forDstu3();
        client = ctx.newRestfulGenericClient(baseUrl);
    }

    public PatientInformation getPatientById(int id) {
        PatientInformation p = new PatientInformation("Jane", "Doe"); //Actually get this from FHIR server...
        return p;
    }

    public PatientInformation[] getPatientsByIds(int[] ids) {
        PatientInformation[] patients = new PatientInformation[ids.length];
        for (int i = 0; i < ids.length; i++) {
            patients[i] = new PatientInformation("Jane", "Doe");
        }
        return patients;
    }

    /*
     * Patient add / get methods
     */

    /**
     * Create a new patient with the given first name and last name
     *
     * @param givenName  (String) first name of the patient
     * @param familyName (String) last name of the patient
     * @return (String) return the id of the newly created patient
     */
    public String addNewPatient(String givenName, String familyName) {
        IdType id = null;

        try {
            Patient patient = new Patient();

            patient.addName()
                    .addGiven(givenName)
                    .setFamily(familyName);

            MethodOutcome outcome = client.create()
                    .resource(patient)
                    .execute();

            id = (IdType) outcome.getId();
        } catch (Exception e) {
            System.out.println("An error occurred attempting to add new patient:");
            e.printStackTrace();
        }
        return id.getIdPart();
    }

    /**
     * Get the first name of the patient with the given id
     *
     * @param id (String) id of the patient
     * @return (String) return the first name of the patient
     */
    public String getGivenName(String id) {

        List<HumanName> patientFullName;
        String givenName = "";

        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            patientFullName = patient.getName();
            givenName = patientFullName.get(0).getGivenAsSingleString();
        } catch (Exception e) {
            System.out.println("An error occurred attempting to retrieve given name:");
            e.printStackTrace();
        }
        return givenName;
    }

    /**
     * Get the last name of the patient with the given id
     *
     * @param id (String) id of the patient
     * @return (String) return the last name of the patient
     */
    public String getFamilyName(String id) {

        List<HumanName> patientFullName;
        String familyName = "";

        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            patientFullName = patient.getName();
            familyName = patientFullName.get(0).getFamily();
        } catch (Exception e) {
            System.out.println("An error occurred attempting to retrieve family name:");
            e.printStackTrace();
        }
        return familyName;
    }

    /**
     * Add the gender of the patient with the given id
     * <p>
     * Gender is case/format sensitive. Must use the format MALE, FEMALE, NULL, OTHER, UNKNOWN as the parameter value
     *
     * @param id     (String) id of the patient
     * @param gender (String) gender of the patient
     * @return (String) return the gender of the patient (used for testing)
     */
    public String addGender(String id, String gender) {

        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            if (gender.equals("MALE")) {
                patient.setGender(Enumerations.AdministrativeGender.MALE);
            }
            if (gender.equals("FEMALE")) {
                patient.setGender(Enumerations.AdministrativeGender.FEMALE);
            }
            if (gender.equals("NULL")) {
                patient.setGender(Enumerations.AdministrativeGender.NULL);
            }
            if (gender.equals("OTHER")) {
                patient.setGender(Enumerations.AdministrativeGender.OTHER);
            }
            if (gender.equals("UNKNOWN")) {
                patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
            }

            client.update()
                    .resource(patient)
                    .execute();
        } catch (Exception e) {
            System.out.println("An error occurred attempting to add gender:");
            e.printStackTrace();
        }
        return getGender(id);
    }

    /**
     * Get the gender of the patient with the given id
     *
     * @param id (String) id of the patient
     * @return (String) return the gender of the patient
     */
    public String getGender(String id) {

        String gender = "";
        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            gender = String.valueOf(patient.getGender());
        } catch (Exception e) {
            System.out.println("An error occurred attempting to retrieve gender:");
            e.printStackTrace();
        }
        return gender;
    }

    /**
     * Add the birth date of the patient with the given id
     * <p>
     * Birth date is format sensitive. Must use the format YYYY-MM-DD as the parameter value
     *
     * @param id        (String) id of the patient
     * @param birthDate (String) birth date of the patient.
     * @return (String) return the birth date of the patient (used for testing)
     */
    public String addBirthDate(String id, String birthDate) {

        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            patient.setBirthDate(java.sql.Date.valueOf(birthDate));

            client.update()
                    .resource(patient)
                    .execute();
        } catch (Exception e) {
            System.out.println("An error occurred attempting to add birth date:");
            e.printStackTrace();
        }
        return getBirthDate(id);
    }

    /**
     * Get the birth date of the patient with the given id
     *
     * @param id (String) id of the patient
     * @return (String) return the birth date of the patient
     */
    public String getBirthDate(String id) {

        String birthDate = "";
        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            birthDate = patient.getBirthDate().toInstant().toString();
        } catch (Exception e) {
            System.out.println("An error occurred attempting to retrieve birth date:");
            e.printStackTrace();
        }
        return birthDate;
    }

    /**
     * Add the full address of the patient with the given id.
     * <p>
     * Use is case/format sensitive. Must use the format HOME, WORK as the parameter value.
     *
     * @param id     (String) id of the patient
     * @param use    (String) type of address
     * @param street (String) physical street name with house number
     * @param city   (String) physical city of address
     * @param state  (String) physical state of address
     * @param zip    (String) physical zip code of address
     * @return (String) return the address of the patient (used for testing)
     */
    public List addAddress(String id, String use, String street, String city, String state, String zip) {

        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            patient.addAddress().setUse(Address.AddressUse.valueOf(use)).setText(street).setCity(city).setState(state).setPostalCode(zip);

            client.update()
                    .resource(patient)
                    .execute();
        } catch (Exception e) {
            System.out.println("An error occurred attempting to add address:");
            e.printStackTrace();
        }
        return getAddress(id);
    }

    /**
     * Get the address of the patient with the given id
     *
     * @param id (String) id of the patient
     * @return (String) return the address of the patient
     */
    public List getAddress(String id) {

        List<Address> fhirAddress;
        List<String> address = new ArrayList<>();

        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            fhirAddress = patient.getAddress();

            address.add(fhirAddress.get(0).getText());
            address.add(fhirAddress.get(0).getCity());
            address.add(fhirAddress.get(0).getState());
            address.add(fhirAddress.get(0).getPostalCode());
        } catch (Exception e) {
            System.out.println("An error occurred attempting to retrieve address:");
            e.printStackTrace();
        }
        return address;
    }

    /**
     * Add the phone number of the patient with the given id
     * <p>
     * Use is case/format sensitive. Must use the format HOME, WORK as the parameter value.
     *
     * @param id    (String) id of the patient
     * @param use   (String) phone number type
     * @param value (String) physical phone number
     * @return (List) return the phone number(s) of the patient (used for testing)
     */
    public List addPhone(String id, String use, String value) {

        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE)
                    .setUse(ContactPoint.ContactPointUse.valueOf(use))
                    .setValue(value);

            client.update()
                    .resource(patient)
                    .execute();
        } catch (Exception e) {
            System.out.println("An error occurred attempting to add phone:");
            e.printStackTrace();
        }
        return getPhone(id);
    }

    /**
     * Get the phone number of the patient with the given id
     *
     * @param id (String) id of the patient
     * @return (List) return the phone number(s) of the patient
     */
    public List getPhone(String id) {

        List<ContactPoint> telecom;
        List<String> phone = new ArrayList<>();
        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            telecom = patient.getTelecom();

            for (ContactPoint item : telecom) {
                if (String.valueOf(item.getSystem()).equals("PHONE")) {
                    phone.add(String.valueOf(item.getUse()));
                    phone.add(item.getValue());
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred attempting to retrieve phone:");
            e.printStackTrace();
        }
        return phone;
    }

    /**
     * Add the email of the patient with the given id
     * <p>
     * Use is case/format sensitive. Must use the format HOME, WORK as the parameter value.
     *
     * @param id    (String) id of the patient
     * @param use   (String) email type
     * @param value (String) physical email address
     * @return (List) return the email(s) of the patient (used for testing)
     */
    public List addEmail(String id, String use, String value) {

        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL)
                    .setUse(ContactPoint.ContactPointUse.valueOf(use))
                    .setValue(value);

            client.update()
                    .resource(patient)
                    .execute();
        } catch (Exception e) {
            System.out.println("An error occurred attempting to add email:");
            e.printStackTrace();
        }
        return getEmail(id);
    }

    /**
     * Get the email of the patient with the given id
     *
     * @param id (String) id of the patient
     * @return (List) return the email(s) of the patient
     */
    public List getEmail(String id) {

        List<ContactPoint> telecom;
        List<String> email = new ArrayList<>();
        try {
            Patient patient = client.read()
                    .resource(Patient.class)
                    .withId(id).execute();

            telecom = patient.getTelecom();

            for (ContactPoint item : telecom) {
                if (String.valueOf(item.getSystem()).equals("EMAIL")) {
                    email.add(String.valueOf(item.getUse()));
                    email.add(item.getValue());
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred attempting to retrieve email:");
            e.printStackTrace();
        }
        return email;
    }
    /**
     * Delete patient
     *
     * @param patientID (String) ID of the patient
     *
     */

    public void deletePatient(String patientID) {

        client.delete().resourceById(new IdDt("Patient", patientID)).execute();

    }


}
