import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {PaymentService} from "../../service/payment.service";
import {User} from "../../model/user";
import {HttpErrorResponse} from "@angular/common/http";
import {Payment} from "../../model/payment";
import {Account} from "../../model/account";
import {AccountService} from "../../service/account.service";
import {MatTableDataSource} from "@angular/material/table";
import {Audit} from "../../model/audit";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import swal from 'sweetalert';

@Component({
  selector: 'app-show-payments',
  templateUrl: './show-payments.component.html',
  styleUrls: ['./show-payments.component.css']
})
export class ShowPaymentsComponent implements OnInit {
  public completedPayments: Payment[];
  public debitAccountsFullName: string[] = [];
  public creditAccountsFullName: string[] = [];
  public audits: Audit[];
  public detailPayment: Payment;
   public currentPage : number = 0;

  displayedColumns: string[] = ['operation', 'amount', 'creditAcc', 'debitAcc','status','date','time','buttons'];
  dataSource!: MatTableDataSource<Payment>;
  displayedColumns2: string[] = ['username', 'objectType', 'operation', 'date','time'];
  dataSource2!: MatTableDataSource<Audit>;
  @ViewChild('paginator', {static: true}) paginator!: MatPaginator;
  @ViewChild('sort', {static: true}) sort!: MatSort;
  @ViewChild('paginator2', {static: true}) paginator2!: MatPaginator;
  @ViewChild('sort2', {static: true}) sort2!: MatSort;

  constructor(private router:Router,private paymentService : PaymentService, private accountService: AccountService) { }

  ngOnInit(): void {
    this.getCompletedPayments();
  }

  public handlePage(event: PageEvent) {
    this.currentPage = event.pageIndex;
  }
  public getAudits(paymentId: number): void {
    this.paymentService.getAudits(paymentId).subscribe(
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



  applyFilter2(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource2.filter = filterValue.trim().toLowerCase();

    if (this.dataSource2.paginator) {
      this.dataSource2.paginator.firstPage();
    }
  }

  public getCompletedPayments(): void {
    this.paymentService.getCompletedPayments().subscribe(
      (response: Payment[]) => {
        this.completedPayments = response.filter((element) => element.operation != "DEPOSIT") ;;
        console.log(this.completedPayments);
        this.dataSource = new MatTableDataSource(this.completedPayments);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        for(let i = 0; i < this.completedPayments.length; i++) {
            this.accountService.getAccountById(this.completedPayments[i].debitAccountId).subscribe(
              (response: Account) => {
                this.debitAccountsFullName.push(response.fullname);
                this.creditAccountsFullName.push(this.completedPayments[i].creditAccount.fullname);
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
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  paymentDetails(payment: Payment) {
    this.detailPayment = payment;
    this.getAudits(payment.id);
    const container = document.getElementById('details-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#detailPaymentModal');
    container.appendChild(button);
    button.click();

  }


  rejectedPayments() {
    this.router.navigate(['/rejected-payments']);

  }

  visualizeBlockchain() {
    this.router.navigate(['/blockchain']);

  }

}
