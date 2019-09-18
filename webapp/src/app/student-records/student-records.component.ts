import { Component, OnInit } from '@angular/core';
import { ApiClientService } from '../api-client.service';
import { StudentRecordsViewModel } from '../models/student-records-view-model';
import { StudentRecord } from '../models/student-record';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-student-records',
  templateUrl: './student-records.component.html',
  styleUrls: ['./student-records.component.scss']
})
export class StudentRecordsComponent implements OnInit {

  sortAsc: boolean;
  orderBy: string;
  filterText: string;
  loading: boolean;

  studentRecordsViewModel: StudentRecordsViewModel;

  constructor(
    private apiClient: ApiClientService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.loading = true;
    this.sortAsc = false;
    this.orderBy = 'name';
    this.filterText = '';
    this.apiClient.getAllStudentRecords()
      .subscribe(studentRecords => {
        this.studentRecordsViewModel = studentRecords;
        this.changeSort(this.orderBy);
        this.loading = false;
      });
  }

  removeStudent(student: StudentRecord) {
    this.toastr.info("This feature does not exist yet.");
  }

  changeSort(fieldName: string) {
    if (this.orderBy == fieldName) {
      this.sortAsc = !this.sortAsc;
    }
    this.orderBy = fieldName;

    this.studentRecordsViewModel.studentRecords = 
      this.studentRecordsViewModel.studentRecords.sort((val1, val2) => {
        var sortInt = this.sortAsc ? 1 : -1;
        if(val1[fieldName] < val2[fieldName]) return -1 * sortInt;
        if(val1[fieldName] > val2[fieldName]) return 1 * sortInt;
        return 0;
      }
    );
  }
}
