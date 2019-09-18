import { Pipe, PipeTransform } from '@angular/core';
import { StudentRecord } from '../models/student-record';

@Pipe({
  name: 'filterStudentRecordGrid',
  pure: false
})
export class FilterStudentRecordGridPipe implements PipeTransform {

  transform(studentRecords: StudentRecord[], searchText: string): any {
    if(!searchText) return studentRecords;

    return studentRecords.filter(record => {
      if(record.firstName.indexOf(searchText) > -1) return true;
      if(record.lastName.indexOf(searchText) > -1) return true;
      if(record.grade.indexOf(searchText) > -1) return true;
      if(record.dob.toString().indexOf(searchText) > -1) return true;
      if(record.homeroom.indexOf(searchText) > -1) return true;
    });
  }

}