import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Observable } from 'rxjs';

import { DatePipe } from '@angular/common';
import { FormBuilder, FormGroup, FormControl,ReactiveFormsModule,Validators}
  from '@angular/forms';

import {Balance} from "../model/balance";
import {Payment} from "../model/payment";
import {Audit} from "../model/audit";
import {PaymentStatistics} from "../model/paymentStatistics";
import {User} from "../model/user";
import swal from 'sweetalert';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private baseUrl = 'http://localhost:8080/api';
  private currentUser = sessionStorage.getItem("currentUser");
  private currentUserId = 0;
  public balances: Observable<Balance[]>;

  public formData!:  FormGroup;
  constructor(private http: HttpClient) {
    if(this.currentUser != null)
    {
      this.http.get<User>(`${this.baseUrl}/users/username/${this.currentUser}`).subscribe(
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


  public getCompletedPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.baseUrl}/payments/status/COMPLETED`);
  }

  public getApprovePayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.baseUrl}/payments/status/APPROVE`);
  }

  public getRejectedPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.baseUrl}/payments/status/REJECTED`);
  }
  public getAuthorizePayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.baseUrl}/payments/status/AUTHORIZE`);
  }


  public rejectPayment(payment: Payment): Observable<Payment> {
    return this.http.put<Payment>(`${this.baseUrl}/payments/reject?userId=${(this.currentUserId)}`, payment);
  }


  public getLastAudit(objectId: number): Observable<Audit> {
    return this.http.get<Audit>(`${this.baseUrl}/audit/last/PAYMENT/${objectId}`);
  }

  public getAudits(paymentId: number): Observable<Audit[]> {
    return this.http.get<Audit[]>(`${this.baseUrl}/audit/PAYMENT/${paymentId}`);
  }

  public getDebitStatistics(status: string, id: number): Observable<PaymentStatistics> {
    return this.http.get<PaymentStatistics>(`${this.baseUrl}/payments/debit-statistics/${status}/${id}`);
  }

  public getCreditStatistics(status: string, id: number): Observable<PaymentStatistics> {
    return this.http.get<PaymentStatistics>(`${this.baseUrl}/payments/credit-statistics/${status}/${id}`);
  }



}
