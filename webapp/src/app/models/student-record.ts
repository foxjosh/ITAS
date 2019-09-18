import { VaccinationRecord } from "./vaccination-record";
import { first } from "rxjs/operators";

export class StudentRecord {
  studentId: number;
  firstName: string;
  lastName: string;
  dob: Date;
  sex: string;
  grade: string;
  homeroom: string;
  guardianContact: string;
  notes: string;
  immunizationScore: number;
  vaccinations: VaccinationRecord[]

  constructor(
    studentId?: number,
    firstName?: string,
    lastName?: string,
    dob?: Date,
    sex?: string,
    grade?: string,
    homeroom?: string,
    guardianContact?: string,
    notes?: string,
    immunizationScore?: number,
    vaccinations?: VaccinationRecord[]
  ){
    this.studentId = studentId; 
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
}

