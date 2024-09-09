import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../service/user.service";
import {NgForm} from "@angular/forms";
import {Payment} from "../model/payment";
import {HttpErrorResponse} from "@angular/common/http";
import swal from 'sweetalert';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
  public username: string;
  public today : Date;
  public date : string;
  public time: string;

//var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();


  constructor(private router:Router, private userService: UserService) {
    if(sessionStorage.getItem('currentUser') != null)
      this.username = sessionStorage.getItem('currentUser');
  }

  ngOnInit(): void {
    this.today = new Date();
    this.date = this.today.getDate() +'/'+(this.today.getMonth()+1)+'/'+ this.today.getFullYear();
    this.time = this.today.getHours() + ":";
    if(this.today.getMinutes() < 10)
      this.time = this.time + "0";
    this.time = this.time + this.today.getMinutes();
  }


  logOut() {
    sessionStorage.removeItem("currentUser");
    this.router.navigate(['/']);
  }

  onOpenModal() {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#changePasswordModal');
    container.appendChild(button);
    button.click();
  }

  public changePassword(password: NgForm, check: NgForm): void {

    if(password.value.password != check.value.password)
    {
     
      swal("Error", "Re-entered password is not the same!", "error");
      check.reset();
      password.reset();
    }
    else
    {

        this.userService.changePassword(password.value.password).subscribe(
          (response: void) => {
            check.reset();
            password.reset();
            
            swal("Operation succedeed", "Password changed successfully.", "success");
          },
          (error: HttpErrorResponse) => {
            swal("Error", error.error, "error");
            
            check.reset();
            password.reset();
          }
        );

    }

  }
}
