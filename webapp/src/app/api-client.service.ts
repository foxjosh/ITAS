import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { DashboardViewModel } from './models/dashboard-view-model';
import { StudentRecordsViewModel } from './models/student-records-view-model';
import { AppNotification } from './models/app-notification';
import { StudentRecord } from './models/student-record';

@Injectable({
  providedIn: 'root'
})
export class ApiClientService {
  constructor(
    private http: HttpClient, 
    private router: Router
  ) { }

  private apiUrlBase = this.getAPIBaseHref();
  private getAPIBaseHref(){
    var url = '';
    var origin = window.location.origin;
    if(origin.indexOf('localhost') > -1){
      url = 'http://localhost:8888/api/';
    }
    else {
      url = origin + "/api/";
    }
    return url;
  }

  getDashboard(): Observable<DashboardViewModel>{
    return this.http.get<DashboardViewModel>(this.apiUrlBase + "dashboard")
      .pipe(
        catchError(this.handleError('getDashboard', null))
      );
  }

  getAllStudentRecords(): Observable<StudentRecordsViewModel>{
    return this.http.get<StudentRecordsViewModel>(this.apiUrlBase + "studentrecords")
      .pipe(
        catchError(this.handleError('getAllStudentRecords', null))
      );
  }

  getStudentRecord(studentId: number): Observable<StudentRecord>{
    return this.http.get<StudentRecordsViewModel>(this.apiUrlBase + "studentrecords/" + studentId)
      .pipe(
        catchError(this.handleError('getStudentRecord', null))
      );
  }

  createStudentRecord(student: StudentRecord): Observable<StudentRecord>{
    return this.http.post<StudentRecord>(this.apiUrlBase + "studentrecords/create", student)
      .pipe(
        catchError(this.handleError('createStudentRecord', null))
      );
  }

  saveStudentRecord(student: StudentRecord): Observable<StudentRecord>{
    return this.http.put<StudentRecord>(this.apiUrlBase + "studentrecords/save/" + student.studentId, student)
      .pipe(
        catchError(this.handleError('createStudentRecord', null))
      );
  }

  getGenderList(): Observable<string[]>{
    return this.http.get<string[]>(this.apiUrlBase + "lists/gender")
      .pipe(
        catchError(this.handleError('getGenderList', null))
      );
  }

  getGradeList(): Observable<string[]>{
    return this.http.get<string[]>(this.apiUrlBase + "lists/grade")
      .pipe(
        catchError(this.handleError('getGradeList', null))
      );
  }

  getHomeroomList(): Observable<string[]>{
    return this.http.get<string[]>(this.apiUrlBase + "lists/homeroom")
      .pipe(
        catchError(this.handleError('getHomeroomList', null))
      );
  }


  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
 
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead
 
 
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
