import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {User} from "../../model/user";
import {UserService} from "../../service/user.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import * as bcrypt from "bcryptjs";
import {Audit} from "../../model/audit";
import {UserHistory} from "../../model/userHistory";
import {Users} from "../../model/users";
import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import swal from 'sweetalert';

@Component({
  selector: 'app-show-users',
  templateUrl: './show-users.component.html',
  styleUrls: ['./show-users.component.css']
})
export class ShowUsersComponent implements OnInit {
  public users: User[];
  public editUser: User;
  public deleteUser: User;
  public detailUser: User;
  public audits: Audit[];
  public uHistory: UserHistory;
  public userAudit: User;
  public mode: string;

  displayedColumns: string[] = ['img','username', 'email', 'status', 'nextstatus', 'buttons'];
  dataSource!: MatTableDataSource<User>;
  displayedColumns2: string[] = ['username', 'objectType', 'operation', 'date','time', 'buttons'];
  dataSource2!: MatTableDataSource<Audit>;
  @ViewChild('paginator', {static: true}) paginator!: MatPaginator;
  @ViewChild('sort', {static: true}) sort!: MatSort;
  @ViewChild('paginator2', {static: true}) paginator2!: MatPaginator;
  @ViewChild('sort2', {static: true}) sort2!: MatSort;


  constructor(private router:Router,private userService : UserService) {
    //this.getUsers();
  }

  ngOnInit(): void {
    //this.getUsers();

    this.getUsers();
  }




  public getUsers(): void {
    this.userService.getUsers().subscribe(
      (response: User[]) => {
        this.users = response;
        console.log(this.users);
        this.dataSource = new MatTableDataSource(this.users);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  applyFilter2(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource2.filter = filterValue.trim().toLowerCase();

    if (this.dataSource2.paginator) {
      this.dataSource2.paginator.firstPage();
    }
  }


  public getAudits(userId: number): void {
    this.userService.getAudits(userId).subscribe(
      (response: Audit[]) => {
        this.audits = response;
        console.log(this.audits);
        this.dataSource2 = new MatTableDataSource(this.audits);
        this.dataSource2.paginator = this.paginator2;
        this.dataSource2.sort = this.sort2;
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
  }
  public getUserHistory(timestamp: string): void {
    this.userService.getUserHistory(timestamp).subscribe(
      (response: UserHistory) => {
        this.uHistory = response;
        console.log(this.uHistory);
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
  }

  public onAddUser(addForm: NgForm): void {

    document.getElementById('add-user-form').click();

    this.userService.addUser(addForm.value).subscribe(
      (response: User) => {
        console.log(response);
        this.getUsers();
        addForm.reset();

      },
      (error: HttpErrorResponse) => {
        swal("Error",error.error, "error");
        addForm.reset();
      }
    );
  }

  public onUpdateUser(user: User): void {
    this.userService.updateUser(user).subscribe(
      (response: User) => {
        console.log(response);
        this.getUsers();
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
  }

  public onDeleteUser(userId: number): void {
    this.userService.deleteUser(userId).subscribe(
      (response: void) => {
        console.log(response);
        this.getUsers();
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
  }

  public onOpenModal(user: User, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'add') {
      button.setAttribute('data-target', '#addUserModal');
    }
    if (mode === 'edit') {
      this.editUser = user;
      button.setAttribute('data-target', '#updateUserModal');
    }
    if (mode === 'delete') {
      this.deleteUser = user;
      button.setAttribute('data-target', '#deleteUserModal');
    }
    container.appendChild(button);
    button.click();
  }

  userDetails(user: User) {
    this.detailUser = user;
    this.getAudits(user.id);
    const container = document.getElementById('details-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#detailUserModal');
    container.appendChild(button);
    button.click();

  }
  userHistory(timestamp: string ) {
    this.getUserHistory(timestamp);
    const container = document.getElementById('user-history-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#userHistoryModal');
    container.appendChild(button);
    button.click();

  }


  approveUsers() {
    this.router.navigate(['/approve-users']);

  }

  rejectedRemovedUsers() {
    this.router.navigate(['/rejected-removed-users']);

  }

}
