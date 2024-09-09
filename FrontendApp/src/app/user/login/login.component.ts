import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import * as bcrypt from 'bcryptjs';

import { FormBuilder, FormGroup, FormControl, ReactiveFormsModule,Validators }
  from '@angular/forms';
import { Router } from '@angular/router';

import { DatePipe }         from '@angular/common';
import {UserService} from "../../service/user.service";
import swal from 'sweetalert';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user: any={};
  username! : string;
  loginForm!:  FormGroup;

  constructor(private router:Router,private userService : UserService,
              public fb: FormBuilder) { }
  ngOnInit() {
    this.loginForm = this.fb.group({
      'username' : [null, Validators.required],
      'password' : [null, Validators.required]
    });
  }

  login2() {
    const val = this.loginForm.value ;
    this.userService.login2(val).subscribe(
      (res: any) =>{
        this.user = res;
        //if (res.notEmpty())
        //{
          if(this.user.status == "ACTIVE") {
            this.router.navigate(['/home']);
            sessionStorage.setItem('currentUser', this.user.username);

          }
          else {
            
            swal("Error",'User is not active!', "error")
          }
        //}
        //else
        //{
        //  alert( 'Username does not exist!')
        //}
      },
      error =>

      swal("Error",error.error, "error")


    );
  }



}
