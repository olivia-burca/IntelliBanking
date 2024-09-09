import {Component, OnInit, OnDestroy, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {User} from "../../model/user";
import {HttpErrorResponse} from "@angular/common/http";
import {Audit} from "../../model/audit";
import {UserHistory} from "../../model/userHistory";
import $ from 'jquery';
import {Subject} from 'rxjs';
import { DataTableDirective } from 'angular-datatables';
import {ADTSettings} from "angular-datatables/src/models/settings";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import swal from 'sweetalert';


@Component({
  selector: 'app-approve-user',
  templateUrl: './approve-user.component.html',
  styleUrls: ['./approve-user.component.css']
})
export class ApproveUserComponent implements OnInit {
  public approveUsers: User[];
  public approveUser: User;
  public rejectUser: User;
  public detailUser: User;
  public audits: Audit[];
  public uHistory: UserHistory;
  public userAudit: User;
  private currentUser = sessionStorage.getItem("currentUser");
  private currentUserId = 0;

  displayedColumns: string[] = ['img','username', 'email', 'status', 'nextstatus', 'buttons'];
  dataSource!: MatTableDataSource<User>;
  displayedColumns2: string[] = ['username', 'objectType', 'operation', 'date','time', 'buttons'];
  dataSource2!: MatTableDataSource<Audit>;
  @ViewChild('paginator', {static: true}) paginator!: MatPaginator;
  @ViewChild('sort', {static: true}) sort!: MatSort;
  @ViewChild('paginator2', {static: true}) paginator2!: MatPaginator;
  @ViewChild('sort2', {static: true}) sort2!: MatSort;



  constructor(private router:Router,private userService : UserService) {
    if(this.currentUser != null)
    {
      this.userService.getUserByUsername(this.currentUser).subscribe(
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

  ngOnInit(): void {
    this.getApproveUsers();
  }

  //this.dtTrigger.next(response);

  public getApproveUsers(): void {
    this.userService.getApproveUsers().subscribe(
      (response: User[]) => {
        this.approveUsers = response;
        console.log(this.approveUsers);
        this.dataSource = new MatTableDataSource(this.approveUsers);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

      },
      (error: HttpErrorResponse) => {
        swal("Error", error.error, "error");
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


  public onApproveUser(user: User): void {
    this.userService.getLastAudit(user.id).subscribe(
      (response: Audit) => {
        console.log(response);
        if(response.userId == this.currentUserId)
        {
        
          swal("Error", "You cannot approve your own modifications!", "error");
        }
        else {
          this.userService.approveUser(user).subscribe(
            (response: User) => {
              console.log(response);
              this.getApproveUsers();
            },
            (error: HttpErrorResponse) => {
              swal("Error", error.error, "error");
            }
          );
        }
      },
      (error: HttpErrorResponse) => {
        swal("Error", error.error, "error");
      }
    );

  }

  public onRejectUser(user: User): void {
    this.userService.getLastAudit(user.id).subscribe(
      (response: Audit) => {
        console.log(response);
        if(response.userId == parseInt(sessionStorage.getItem("currentUserId")))
        {
          
          swal("Error", "You cannot reject your own modifications!", "error");
        }
        else {
          this.userService.rejectUser(user).subscribe(
            (response: User) => {
              console.log(response);
              this.getApproveUsers();
            },
            (error: HttpErrorResponse) => {
              swal("Error", error.error, "error");
            }
          );
        }
      },
      (error: HttpErrorResponse) => {
        swal("Error", error.error, "error");
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
        swal("Error", error.error, "error");
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
        swal("Error", error.error, "error");
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


  public onOpenModal(user: User, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'approve') {
      this.approveUser = user;
      button.setAttribute('data-target', '#approveUserModal');
    }
    if (mode === 'reject') {
      this.rejectUser = user;
      button.setAttribute('data-target', '#rejectUserModal');
    }
    container.appendChild(button);
    button.click();
  }



}
