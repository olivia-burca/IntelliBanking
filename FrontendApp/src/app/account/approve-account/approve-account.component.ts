import {Component, OnInit, ViewChild} from '@angular/core';
import {User} from "../../model/user";
import {Audit} from "../../model/audit";
import {UserHistory} from "../../model/userHistory";
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {HttpErrorResponse} from "@angular/common/http";
import {AccountHistory} from "../../model/accountHistory";
import {Account} from "../../model/account";
import {AccountService} from "../../service/account.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import swal from 'sweetalert';

@Component({
  selector: 'app-approve-account',
  templateUrl: './approve-account.component.html',
  styleUrls: ['./approve-account.component.css']
})
export class ApproveAccountComponent implements OnInit {

  public approveAccounts: Account[];
  public approveAccount: Account;
  public rejectAccount: Account;
  public detailAccount: Account;
  public audits: Audit[];
  public accHistory: AccountHistory;
  public accountAudit: Account;
  private currentUser = sessionStorage.getItem("currentUser");
  private currentUserId = 0;

  displayedColumns: string[] = ['username', 'fullname', 'address', 'currency','accountStatus', 'status','nextstatus', 'buttons1'];
  dataSource!: MatTableDataSource<Account>;
  displayedColumns2: string[] = ['username', 'objectType', 'operation', 'date','time', 'buttons'];
  dataSource2!: MatTableDataSource<Audit>;
  @ViewChild('paginator', {static: true}) paginator!: MatPaginator;
  @ViewChild('sort', {static: true}) sort!: MatSort;
  @ViewChild('paginator2', {static: true}) paginator2!: MatPaginator;
  @ViewChild('sort2', {static: true}) sort2!: MatSort;

  constructor(private router:Router,private accountService : AccountService, private userService: UserService) {
    if(this.currentUser != null)
    {
      this.userService.getUserByUsername(this.currentUser).subscribe(
        (response: User) => {
          this.currentUserId = response.id;
          console.log(this.currentUserId);
        },
        (error: HttpErrorResponse) => {
          swal("Error",error.message, "error");
        }
      );
    }
  }

  ngOnInit(): void {
    this.getApproveAccounts();
  }

  public getApproveAccounts(): void {
    this.accountService.getApproveAccounts().subscribe(
      (response: Account[]) => {
        this.approveAccounts = response;
        console.log(this.approveAccounts);
        this.dataSource = new MatTableDataSource(this.approveAccounts);
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

  public onApproveAccount(account: Account): void {
    this.accountService.getLastAudit(account.id).subscribe(
      (response: Audit) => {
        console.log(response);
        if(response.userId == this.currentUserId)
        {
          
          swal("Error","You cannot approve your own modifications!", "error");
        }
        else {
          this.accountService.approveAccount(account).subscribe(
            (response: Account) => {
              console.log(response);
              this.getApproveAccounts();
            },
            (error: HttpErrorResponse) => {
              swal("Error",error.message, "error");
            }
          );
        }
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );

  }

  public onRejectAccount(account: Account): void {
    this.accountService.getLastAudit(account.id).subscribe(
      (response: Audit) => {
        console.log(response);
        if(response.userId == parseInt(sessionStorage.getItem("currentUserId")))
        {
      
          swal("Error","You cannot reject your own modifications!", "error");
        }
        else {
          this.accountService.rejectAccount(account).subscribe(
            (response: Account) => {
              console.log(response);
              this.getApproveAccounts();
            },
            (error: HttpErrorResponse) => {
              swal("Error",error.message, "error");
            }
          );
        }
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
  }

  public getAudits(accountId: number): void {
    this.accountService.getAudits(accountId).subscribe(
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
  applyFilter2(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource2.filter = filterValue.trim().toLowerCase();

    if (this.dataSource2.paginator) {
      this.dataSource2.paginator.firstPage();
    }
  }

  public getAccountHistory(timestamp: string): void {
    this.accountService.getAccountHistory(timestamp).subscribe(
      (response: AccountHistory) => {
        this.accHistory = response;
        console.log(this.accHistory);
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
  }

  accountDetails(account: Account) {
    this.detailAccount = account;
    const container = document.getElementById('details-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#detailAccountModal');
    container.appendChild(button);
    button.click();
    this.getAudits(account.id);

  }
  accountHistory(timestamp: string ) {
    this.getAccountHistory(timestamp);
    const container = document.getElementById('account-history-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#accountHistoryModal');
    container.appendChild(button);
    button.click();

  }


  public onOpenModal(account: Account, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'approve') {
      this.approveAccount = account;
      button.setAttribute('data-target', '#approveAccountModal');
    }
    if (mode === 'reject') {
      this.rejectAccount = account;
      button.setAttribute('data-target', '#rejectAccountModal');
    }
    container.appendChild(button);
    button.click();
  }


}
