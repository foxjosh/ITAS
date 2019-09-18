import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from './../environments/environment';

@Injectable()
export class TestAPIClient {
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

  getHelloWorld() {
    return this.http.get(this.apiUrlBase, 
      {responseType: 'text'});
  }
}