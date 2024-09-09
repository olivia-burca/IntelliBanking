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
import { Cryptocurrency } from '../model/cryptocurrency';
import swal from 'sweetalert';

@Injectable({
  providedIn: 'root'
})
export class CryptoService {
  private baseUrl = 'http://localhost:8080/api';
  private currentUser = sessionStorage.getItem("currentUser");
  private currentUserId = 0;

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
  
  public getCryptocurrencies(): Observable<Cryptocurrency[]> {
    return this.http.get<Cryptocurrency[]>(`${this.baseUrl}/crypto`);
  }

}
