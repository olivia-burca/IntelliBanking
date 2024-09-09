import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {User} from "../../model/user";
import {HttpErrorResponse} from "@angular/common/http";
import * as bcrypt from "bcryptjs";
import {Audit} from "../../model/audit";
import {UserHistory} from "../../model/userHistory";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import swal from 'sweetalert';

@Component({
  selector: 'app-removed-user',
  templateUrl: './removed-user.component.html',
  styleUrls: ['./removed-user.component.css']
})
export class RemovedUserComponent implements OnInit {
  public removedUsers: User[];
  public rejectedUsers: User[];
  public detailUser: User;
  public audits: Audit[];
  public uHistory: UserHistory;
  public userAudit: User;

  displayedColumns: string[] = ['img','username', 'email', 'status', 'nextstatus', 'buttons'];
  dataSource!: MatTableDataSource<User>;
  displayedColumns2: string[] = ['username', 'objectType', 'operation', 'date','time', 'buttons'];
  dataSource2!: MatTableDataSource<Audit>;
  @ViewChild('paginator', {static: true}) paginator!: MatPaginator;
  @ViewChild('sort', {static: true}) sort!: MatSort;
  @ViewChild('paginator2', {static: true}) paginator2!: MatPaginator;
  @ViewChild('sort2', {static: true}) sort2!: MatSort;

  constructor(private router:Router,private userService : UserService) { }

  ngOnInit(): void {
    //this.getRejectedUsers();
    this.getRemovedUsers();
  }

  public getRemovedUsers(): void {
    this.userService.getRemovedUsers().subscribe(
      (response: User[]) => {
        this.removedUsers = response;
        console.log(this.removedUsers);
        this.userService.getRejectedUsers().subscribe(
          (response: User[]) => {
            this.removedUsers = this.removedUsers.concat(response);
            console.log(this.removedUsers);
            this.dataSource = new MatTableDataSource(this.removedUsers);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;

          },
          (error: HttpErrorResponse) => {
            swal("Error",error.message, "error");
          }
        );
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
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

  userDetails(user: User) {
    this.detailUser = user;
    const container = document.getElementById('details-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#detailUserModal');
    container.appendChild(button);
    button.click();
    this.getAudits(user.id);

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


}
