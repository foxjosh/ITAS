import { ImmunizationStatus } from "./immunization-status";

export class VaccinationRecord {
  
    immunizationRecordId: string;
    vaccinationName: string;
    status: ImmunizationStatus;
    completedDate: Date;
    recommendedDate: Date;
    note: string;

    constructor(immunizationRecordId: string, vaccinationName: string, status: ImmunizationStatus, recommendedDate: Date, completedDate: Date,
                note: string){
        this.immunizationRecordId = immunizationRecordId;
        this.vaccinationName = vaccinationName;
        this.status = status;
        this.completedDate = completedDate;
        this.recommendedDate = recommendedDate;
        this.note = note;
    }
}
