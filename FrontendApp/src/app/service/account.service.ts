import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account} from '../model/account';
import { DatePipe } from '@angular/common';
import { FormBuilder, FormGroup, FormControl,ReactiveFormsModule,Validators}
  from '@angular/forms';
import {Audit} from "../model/audit";
import {AccountHistory} from "../model/accountHistory";
import {Balance} from "../model/balance";
import {Payment} from "../model/payment";
import {User} from "../model/user";
import swal from 'sweetalert';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
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


  public getAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.baseUrl}/accounts/status/ACTIVE`);
  }

  public getAccountById(accountId: number): Observable<Account> {
    return this.http.get<Account>(`${this.baseUrl}/accounts/${accountId}`);
  }

  public getAccountByPublicKey(publicKey: string): Observable<Account> {
    return this.http.get<Account>(`${this.baseUrl}/accounts/publicKey?publicKey=${(publicKey)}`);
  }

  public getAudits(accountId: number): Observable<Audit[]> {
    return this.http.get<Audit[]>(`${this.baseUrl}/audit/ACCOUNT/${accountId}`);
  }

  public getAuditByTimestamp(timestamp: string): Observable<Audit> {
    return this.http.get<Audit>(`${this.baseUrl}/audit/timestamp/${timestamp}`);
  }

  public getAccountHistory(timestamp: string): Observable<AccountHistory> {
    return this.http.get<AccountHistory>(`${this.baseUrl}/accounthistory/timestamp/${timestamp}`);
  }

  public getPayments(accountId: number): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.baseUrl}/payments/${accountId}`);
  }

  public getApproveAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.baseUrl}/accounts/status/APPROVE`);
  }

  public getRemovedAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.baseUrl}/accounts/status/REMOVED`);
  }
  public getRejectedAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.baseUrl}/accounts/status/REJECTED`);
  }

  public addAccount(account: Account): Observable<Account> {
    return this.http.post<Account>(`${this.baseUrl}/accounts/add?userId=${(this.currentUserId)}`, account);
  }

  public updateAccount(account: Account): Observable<Account> {
    return this.http.put<Account>(`${this.baseUrl}/accounts/update?userId=${(this.currentUserId)}`, account);
  }

  public deleteAccount(accountId: number): Observable<void> {
    //User;
    return this.http.put<void>(`${this.baseUrl}/accounts/delete/${accountId}?userId=${(this.currentUserId)}`,Account);
  }

  public approveAccount(account: Account): Observable<Account> {
    return this.http.put<Account>(`${this.baseUrl}/accounts/approve?userId=${(this.currentUserId)}`, account);
  }

  public rejectAccount(account: Account): Observable<Account> {
    return this.http.put<Account>(`${this.baseUrl}/accounts/reject?userId=${(this.currentUserId)}`, account);
  }

  public getLastAudit(objectId: number): Observable<Audit> {
    return this.http.get<Audit>(`${this.baseUrl}/audit/last/ACCOUNT/${objectId}`);
  }

  public getLastBalance(accountId: number): Observable<Balance> {
    return this.http.get<Balance>(`${this.baseUrl}/balances/last/${accountId}`);
  }

  public getBalances(accountId: number): Observable<Balance[]> {
    return this.http.get<Balance[]>(`${this.baseUrl}/balances/${accountId}`);

  }

  public addPayment(payment: Payment, id: number): Observable<Payment> {
    return this.http.post<Payment>(`${this.baseUrl}/payments/add?userId=${(this.currentUserId)}&accountId=${(id)}`, payment);

  }

}
