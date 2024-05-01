import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class StudentInfoService {

  constructor(
    private http: HttpClient
  ) {}
  getAddress() : Observable<any>{
    return this.http.get(SERVER_API_URL + "api/resource-server/address");
  }
  getSubjects() : Observable<any>{
    return this.http.get(SERVER_API_URL + "api/resource-server/subject")
  }
  getRelation() : Observable<any>{
    return this.http.get(SERVER_API_URL + "api/resource-server/relation");
  }

}
