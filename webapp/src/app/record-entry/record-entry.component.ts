import {Component, OnInit} from '@angular/core';
import {StudentRecord} from "../models/student-record";
import {ImmunizationStatus} from "../models/immunization-status";
import {ActivatedRoute, Router} from "@angular/router";
import { VaccinationRecord } from '../models/vaccination-record';
import { ApiClientService } from '../api-client.service';
import {StudentRecordsViewModel} from "../models/student-records-view-model";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-record-entry',
  templateUrl: './record-entry.component.html',
  styleUrls: ['./record-entry.component.scss', './switches.scss']
})
export class RecordEntryComponent implements OnInit {

  hasStudentLoaded: Promise<boolean>;
  isNewStudent: boolean;
  isEditMode: boolean;
  studentRecord: StudentRecord;

  studentRecordsViewModel: StudentRecordsViewModel;
  selectedStudent: number;

  genderList: string[];
  gradeList: string[];
  homeroomList: string[];

  pieChartType: string;
  pieChartData: number[];
  pieChartLabels: string[];
  pieChartColors: Array<any>;
  pieChartLegend: boolean;

  loading: boolean;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private apiClient: ApiClientService,
    private toastr: ToastrService
  ) {
    this.loading = true;
    this.pieChartType = 'pie';
    this.pieChartLabels = ["Completed", "Due Soon", "Overdue"];
    this.pieChartData = [0, 0, 0];
    this.pieChartColors = [{
        backgroundColor: ['rgba(40, 167, 69, 0.4)', 'rgba(255, 193, 7, 0.4)','rgba(220, 53, 69, 0.4)'],
        borderColor: ['rgba(40, 167, 69, 1)','rgba(255, 193, 7, 1)','rgba(148, 159, 177, 1)'],
        hoverBackgroundColor: ['rgba(40, 167, 69, 0.7)', 'rgba(255, 193, 7, 0.7)','rgba(220, 53, 69, 0.7)'],
      }];
    this.pieChartLegend = false;
  }

  ngOnInit() {
    this.loading = true;
    this.route.params.subscribe(params => {
      this.isNewStudent = (params['id'] === "0");
      this.isEditMode = this.isNewStudent;
      this.setStudentRecordState(params['id']);
    });

    this.apiClient.getAllStudentRecords()
      .subscribe(studentRecords => {
        this.studentRecordsViewModel = studentRecords;
        this.loading = false;
      });
    this.setListValues();
  }

  private setStudentRecordState(studentId: number){
    if (this.isNewStudent) {
      this.studentRecord = new StudentRecord(0);
      this.hasStudentLoaded = Promise.resolve(true);
    }
    else {
      this.loading = true;
      this.apiClient.getStudentRecord(studentId)
        .subscribe(studentRecord => {
          this.studentRecord = studentRecord;
          this.setPieChartData();
          this.selectedStudent = this.studentRecord.studentId;
          this.loading = false;
          this.hasStudentLoaded = Promise.resolve(true);
        });
    }
  }

  private setListValues(){
    this.apiClient.getGenderList()
      .subscribe(genders => {
        this.genderList = genders;
      });

    this.apiClient.getGradeList()
      .subscribe(grades => {
        this.gradeList = grades;
      });

    this.apiClient.getHomeroomList()
      .subscribe(homerooms => {
        this.homeroomList = homerooms;
      });
  }

  private createStudentProfile() {
    // add validation
    this.loading = true;
    this.apiClient.createStudentRecord(this.studentRecord)
      .subscribe(student => {
        if (student) {
          this.loading = false;
          this.router.navigate(['/record-entry/' + student.studentId]);
        }
        else {
          this.loading = false;
          this.toastr.error("Could not create student information at this time due to server error.");
        }
      });
  }

  private saveStudentProfile() {
    // add validation
    this.loading = true;
    this.isEditMode = false;
    this.apiClient.saveStudentRecord(this.studentRecord)
      .subscribe(student => {
        if (student) {
          this.studentRecord = student;
          this.selectedStudent = this.studentRecord.studentId;
          this.setPieChartData();
          this.loading = false;
        }
        else {
          this.loading = false;
          this.toastr.error("Could not save student information at this time due to server error.");
        }
      });
  }

  saveOrEditProfile(){
    if (this.isEditMode === false){
      this.isEditMode = true;
    }
    else {
      this.saveProfile();
    }

  }

  saveProfile() {
    this.isEditMode = false;

    if (this.isNewStudent) {
      this.createStudentProfile();
    }
    else {
      this.saveStudentProfile();
    }
  }

  statusClass(status: ImmunizationStatus) {
    let className = "";

    switch(status) {
      case ImmunizationStatus.Overdue:
        className = "fas fa-lg fa-fw fa-exclamation-triangle text-danger";
        break;
      case ImmunizationStatus.Completed:
        className = "fas fa-lg fa-fw fa-check text-success";
        break;
      case ImmunizationStatus.Incomplete:
      default:
        break;
    }

    return className;
  }

  toggleRecordStatus(record: VaccinationRecord) {
    let status = record.status;
    let newStatus = status;

    switch(status) {
      case ImmunizationStatus.Incomplete:
      case ImmunizationStatus.Overdue:
        newStatus = ImmunizationStatus.Completed;
        if (!record.completedDate) {
          record.completedDate = new Date();
        }
        break;
      case ImmunizationStatus.Completed:
        if (record.recommendedDate && new Date(record.recommendedDate) < new Date() ) {
          newStatus = ImmunizationStatus.Overdue;
        }
        else {
          newStatus = ImmunizationStatus.Incomplete;
        }
        break;
      default:
        break;
    }

    record.status = newStatus;

    this.setPieChartData();
    this.saveProfile();
  }

  // saveRecord(record: VaccinationRecord, newRecord: boolean) {
  //   if (!record.completedDate) {
  //     // if date was not set, set it to today
  //     record.completedDate = new Date();
  //   }

  //   if (record && record.status === ImmunizationStatus.Completed) {
  //     // TODO: Save the record here!

  //     if (newRecord) {
  //       // clear the record entry field
  //       this.newRecord.empty();
  //       // TODO: BUG -- Slider status of newRecord doesn't update correctly -- should revert to unselected
  //     }
  //   }
  // }

  getSaveButtonLabel() {
    if(this.isNewStudent) return "Create Student Profile";
    if(this.isEditMode) return "Save Profile";
    if(!this.isEditMode) return "Edit Profile";
  }

  getSaveButtonClass() {
    if(this.isEditMode) return "btn-primary";
    return "btn-secondary";
  }

  calculateAge(dob: any) {
    // Help given from https://stackoverflow.com/a/41792176
    let age = 0;
    const message = " years old";
    const birthDate = new Date(dob);
    if (birthDate) {
      const diff = Math.abs(Date.now() - birthDate.getTime());
      age = Math.floor((diff / (1000 * 3600 * 24))/ 365.25);
    }
    return age + message;
  }

  isVaccineComplete(record: VaccinationRecord){
    return record.status === ImmunizationStatus.Completed;
  }

  setPieChartData() {
    if (this.studentRecord && this.studentRecord.studentId === 0) {
      this.pieChartData = [0, 0, 0];
    }
    else if (this.studentRecord) {
      let completed = 0, incomplete = 0, overdue = 0;
      for (const record of this.studentRecord.vaccinations) {
        switch (record.status) {
          case ImmunizationStatus.Completed:
            completed++;
            break;
          case ImmunizationStatus.Incomplete:
            // not really due soon unless within 6? months
            const deadline = new Date();
            const recommendDate = new Date(record.recommendedDate);
            deadline.setMonth(deadline.getMonth() + 6);
            if (recommendDate.getTime() < deadline.getTime()) {
              incomplete++;
            }
            break;
          case ImmunizationStatus.Overdue:
            overdue++;
            break;
        }
      }
      this.pieChartData = [completed, incomplete, overdue];
    }
  }

  changeStudent(student: StudentRecord) {
    this.router.navigate(['/record-entry/' + student.studentId]);
  }

  searchStudents(term: string, item: StudentRecord) {
    term = term.toLowerCase();
    return item.firstName.toLowerCase().indexOf(term) > -1 ||
      item.lastName.toLowerCase().indexOf(term) > -1 ||
      item.grade.toLowerCase().indexOf(term) > -1;
  }

}
