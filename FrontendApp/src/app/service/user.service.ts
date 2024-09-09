import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { User} from '../model/user';
import { DatePipe } from '@angular/common';
import { FormBuilder, FormGroup, FormControl,ReactiveFormsModule,Validators}
  from '@angular/forms';
import {Audit} from "../model/audit";
import {UserHistory} from "../model/userHistory";
import { catchError, retry } from 'rxjs/operators';
import {Users} from "../model/users";
import swal from 'sweetalert';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/api/users';
  private auditUrl = 'http://localhost:8080/api/audit';
  private userHistoryUrl = 'http://localhost:8080/api/userhistory';
  private currentUser = sessionStorage.getItem("currentUser");
  private currentUserId = 0;

  public formData!:  FormGroup;
  constructor(private http: HttpClient) {
    if(this.currentUser != null)
    {
      this.getUserByUsername(this.currentUser).subscribe(
        (response: User) => {
          this.currentUserId = response.id;
          console.log(this.currentUserId);
        },
        (error: HttpErrorResponse) => {
          swal("Error", error.error, "error");
        }
      );
    }
  }

  login2(user :User): Observable<User> {
    return this.http.put<User>(`http://localhost:8080/api/login`, user);

  }

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/status/ACTIVE`);
  }

  public getUserById(userId: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${userId}`);
  }

  public getUserByUsername(username: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/username/${username}`);
  }

  public getAudits(userId: number): Observable<Audit[]> {
    return this.http.get<Audit[]>(`${this.auditUrl}/USER/${userId}`);
  }
  public getUserHistory(timestamp: string): Observable<UserHistory> {
    return this.http.get<UserHistory>(`${this.userHistoryUrl}/timestamp/${timestamp}`);
  }

  public getApproveUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/status/APPROVE`);
  }

  public getRemovedUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/status/REMOVED`);
  }
  public getRejectedUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/status/REJECTED`);
  }

  public addUser(user: User): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/add?userId=${(this.currentUserId)}`, user);
  }

  public updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/update?userId=${(this.currentUserId)}`, user);
  }

  public deleteUser(userId: number): Observable<void> {
    //User;
    return this.http.put<void>(`${this.baseUrl}/delete/${userId}?userId=${(this.currentUserId)}`,User);
  }

  public approveUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/approve?userId=${(this.currentUserId)}`, user);
  }

  public rejectUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/reject?userId=${(this.currentUserId)}`, user);
  }

  public getLastAudit(objectId: number): Observable<Audit> {
    return this.http.get<Audit>(`${this.auditUrl}/last/USER/${objectId}`);
  }

  public changePassword(password: String): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/change-password?userId=${(this.currentUserId)}`, password);
  }


}
