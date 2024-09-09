import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {UserService} from "../../service/user.service";
import {AccountService} from "../../service/account.service";
import {User} from "../../model/user";
import {Audit} from "../../model/audit";
import {UserHistory} from "../../model/userHistory";
import {Account} from "../../model/account";
import {AccountHistory} from "../../model/accountHistory";
import {HttpErrorResponse} from "@angular/common/http";
import {FormControl, FormGroup, NgForm} from "@angular/forms";
import * as bcrypt from "bcryptjs";
import {Balance} from "../../model/balance";
import {Payment} from "../../model/payment";
import {PaymentStatistics} from "../../model/paymentStatistics";
import {PaymentService} from "../../service/payment.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {DatePipe} from "@angular/common";
import cc from "currency-codes";
import { CryptoService } from 'src/app/service/crypto.service';
import { Cryptocurrency } from 'src/app/model/cryptocurrency';
import swal from 'sweetalert';



@Component({
  selector: 'app-show-accounts',
  templateUrl: './show-accounts.component.html',
  styleUrls: ['./show-accounts.component.css']
})
export class ShowAccountsComponent implements OnInit {
  public accounts: Account[];
  public editAccount: Account;
  public deleteAccount: Account;
  public detailAccount: Account;
  public currentAccount: Account;
  public audits: Audit[];
  public accHistory: AccountHistory;
  public lastBalance: Balance;
  public balances: Balance[];
  public depositAccount: Account;
  public transactionAccountCredit: Account;
  public transactionAccountDebit: Account;
  public payments: Payment[];
  public debitAccountsFullName: string[] = new Array(100);
  public creditAccountsFullName: string[] = new Array(100);
  public debitStatistics: PaymentStatistics[] = [];
  public creditStatistics: PaymentStatistics[] = [];
  public balanceAudit: Audit;
  public paymentAudit: Audit;
  //public currencies = require('currency-codes');
  public currencyCodes : string[] = [];
  public currenciesNonCrypto = require('currency-codes');
  //public pageEvent: PageEvent;
  public currentPage : number = 0;
  

  pipe: DatePipe;
  filterForm = new FormGroup({
    fromDate: new FormControl(),
    toDate: new FormControl(),
  });
  get fromDate() { return this.filterForm.get('fromDate').value; }
  get toDate() { return this.filterForm.get('toDate').value; }

  pipe2: DatePipe;
  filterForm2 = new FormGroup({
    fromDate2: new FormControl(),
    toDate2: new FormControl(),
  });
  get fromDate2() { return this.filterForm2.get('fromDate2').value; }
  get toDate2() { return this.filterForm2.get('toDate2').value; }

  displayedColumns: string[] = ['username', 'fullname', 'address', 'currency','accountStatus', 'status','nextstatus', 'buttons1','buttons2','buttons3'];
  dataSource!: MatTableDataSource<Account>;
  displayedColumns2: string[] = ['username', 'objectType', 'operation', 'date','time', 'buttons'];
  dataSource2!: MatTableDataSource<Audit>;
  @ViewChild('paginator', {static: true}) paginator!: MatPaginator;
  @ViewChild('sort', {static: true}) sort!: MatSort;
  @ViewChild('paginator2', {static: true}) paginator2!: MatPaginator;
  @ViewChild('sort2', {static: true}) sort2!: MatSort;
  displayedColumns3: string[] = ['total', 'pendingCredit', 'pendingDebit', 'date','time','buttons'];
  dataSource3!: MatTableDataSource<Balance>;
  @ViewChild('paginator3', {static: true}) paginator3!: MatPaginator;
  @ViewChild('sort3', {static: true}) sort3!: MatSort;
  displayedColumns4: string[] = ['operation', 'amount', 'creditAcc', 'debitAcc','status','date','time','buttons'];
  dataSource4!: MatTableDataSource<Payment>;
  @ViewChild('paginator4', {static: true}) paginator4!: MatPaginator;
  @ViewChild('sort4', {static: true}) sort4!: MatSort;



  constructor(private router:Router,private accountService : AccountService, private paymentService: PaymentService, private cryptoService: CryptoService) {
    /*
    this.pipe = new DatePipe('en');
    this.dataSource3.filterPredicate = (data, filter) =>{
      if (this.fromDate && this.toDate) {
        const date = new Date(data.timestamp);
        return date >= this.fromDate && date <= this.toDate;
      }
      return true;
    }

     */

  }

  public handlePage(event: PageEvent) {
    this.currentPage = event.pageIndex;
  }

 

  applyFilterDate() {
    this.dataSource3.filter = ''+Math.random();
    if (this.dataSource3.paginator) {
      this.dataSource3.paginator.firstPage();
    }
  }

  applyFilterDate2() {
    this.dataSource4.filter = ''+Math.random();
    if (this.dataSource4.paginator) {
      this.dataSource4.paginator.firstPage();
    }
  }

  ngOnInit(): void {
    this.getAccounts();
    this.getCryptocurrencies();
    
  }

  isPositive(payment: Payment) {
    for(let i = 0; i < this.accounts.length; i++) {
      if(this.accounts[i].id == payment.debitAccountId) {
        if(this.accounts[i].publicKey == this.currentAccount.publicKey) {
          return true;
      }
    }
  }
  return false;
}

  public getCryptocurrencies(): void {
    this.cryptoService.getCryptocurrencies().subscribe(
      (response: Cryptocurrency[]) => {
        for(let i=0; i<response.length; i++) {
          this.currencyCodes.push(response[i].symbol);
        }
        console.log(this.currencyCodes);
      },
      (error: HttpErrorResponse) => {
        
        swal("Error", error.message, "error");
      }
    );
  }

  public getAccounts(): void {
    this.accountService.getAccounts().subscribe(
      (response: Account[]) => {
        this.accounts = response;
        console.log(this.accounts);


        this.dataSource = new MatTableDataSource(this.accounts);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      (error: HttpErrorResponse) => {
        swal("Error", error.message, "error");
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
        swal("Error", error.message, "error");
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

  public getLastBalance(accountId: number): void {
    this.accountService.getLastBalance(accountId).subscribe(
      (response: Balance) => {
        this.lastBalance = response;
        console.log('LAST BALANCE');
        console.log(this.lastBalance);
      },
      (error: HttpErrorResponse) => {
        swal("Error", error.message, "error");
      }
    );
  }

  public getBalances(accountId: number): void {
    this.accountService.getBalances(accountId).subscribe(
      (response: Balance[]) => {
        this.balances = response;
        console.log(this.balances);
        this.dataSource3= new MatTableDataSource(this.balances);
        this.dataSource3.paginator = this.paginator3;
        this.dataSource3.sort = this.sort3;

        this.filterForm.reset();

        this.pipe = new DatePipe('en');
        this.dataSource3.filterPredicate = (data, filter) =>{
          if (this.fromDate && this.toDate) {
            const date = new Date(data.timestamp);
            return date >= this.fromDate && date <= this.toDate;
          }
          return true;
        }

      },
      (error: HttpErrorResponse) => {
        swal("Error", error.message, "error");
      }
    );
  }

  public getAccountHistory(timestamp: string): void {
    this.accountService.getAccountHistory(timestamp).subscribe(
      (response: AccountHistory) => {
        this.accHistory = response;
        console.log(this.accHistory);
      },
      (error: HttpErrorResponse) => {
        swal("Error", error.message, "error");
      }
    );
  }

  public getBalanceAudit(timestamp: string): void {
    this.accountService.getAuditByTimestamp(timestamp).subscribe(
      (response: Audit) => {
        this.balanceAudit = response;
        console.log(this.balanceAudit);
      },
      (error: HttpErrorResponse) => {
        swal("Error", error.message, "error");
      }
    );
  }

  public getPaymentAudit(timestamp: string): void {
    this.accountService.getAuditByTimestamp(timestamp).subscribe(
      (response: Audit) => {
        this.paymentAudit = response;
        console.log(this.paymentAudit);
      },
      (error: HttpErrorResponse) => {
        swal("Error", error.message, "error");
      }
    );
  }


  public getPayments(account: Account): void {
    this.accountService.getPayments(account.id).subscribe(
      (response: Payment[]) => {
        
        this.payments = response.filter((element) => element.operation != "DEPOSIT") ;
        console.log('Payments for account: ' + account.id + '\n');
        console.log(this.payments);
        this.dataSource4 = new MatTableDataSource(this.payments);
        this.dataSource4.paginator = this.paginator4;
        this.dataSource4.sort = this.sort4;

        this.filterForm2.reset();

        this.pipe2 = new DatePipe('en');
        this.dataSource4.filterPredicate = (data, filter) =>{
          if (this.fromDate2 && this.toDate2) {
            const date = new Date(data.timestamp);
            return date >= this.fromDate2 && date <= this.toDate2;
          }
          return true;
        }

        for(let i = 0; i < this.payments.length; i++) {
        
          this.accountService.getAccountById(this.payments[i].debitAccountId).subscribe(
            (response: Account) => {
              this.debitAccountsFullName[i] = response.fullname;
                this.creditAccountsFullName[i] = this.payments[i].creditAccount.fullname;
            },
            (error: HttpErrorResponse) => {
              swal("Error", error.message, "error");
            }
          );

        }
       
      },
      (error: HttpErrorResponse) => {
        swal("Error", error.message, "error");
      }
    );
  }


  public getPaymentsStatistics(account: Account): void {
    this.paymentService.getDebitStatistics("COMPLETED", account.id).subscribe(
      (response: PaymentStatistics) => {
        if(response != null)
          this.debitStatistics[0] = response;
        else
          this.debitStatistics[0] = new PaymentStatistics("COMPLETED",0,0);

        console.log(response);
       
                this.paymentService.getDebitStatistics("REJECTED", account.id).subscribe(
                  (response: PaymentStatistics) => {
                    if(response != null)
                      this.debitStatistics[1] = response;
                    else
                      this.debitStatistics[1] = new PaymentStatistics("REJECTED",0,0);
                    console.log(response);
                    this.paymentService.getCreditStatistics("COMPLETED", account.id).subscribe(
                      (response: PaymentStatistics) => {
                        if(response != null)
                          this.creditStatistics[0] = response;
                        else
                          this.creditStatistics[0] = new PaymentStatistics("COMPLETED",0,0);
                        console.log(response);
                                this.paymentService.getCreditStatistics("REJECTED", account.id).subscribe(
                                  (response: PaymentStatistics) => {
                                    if(response != null)
                                      this.creditStatistics[1] = response;
                                    else
                                      this.creditStatistics[1] = new PaymentStatistics("REJECTED",0,0);
                                    console.log(response);
                                  },
                                  (error: HttpErrorResponse) => {
                                    swal("Error", error.message, "error");
                                  }
                                );
                             
                      },
                      (error: HttpErrorResponse) => {
                        swal("Error", error.message, "error");
                      }
                    );
                  },
                  (error: HttpErrorResponse) => {
                    swal("Error", error.message, "error");
                  }
                );
              
      },
      (error: HttpErrorResponse) => {
        swal("Error", error.message, "error");
      }
    );
  }



  public onAddAccount(addForm: NgForm): void {


    document.getElementById('add-account-form').click();
    //console.log(this.currencies.codes().includes(addForm.value.currency.toUpperCase()));
    //if(!(this.currencies.codes().includes(addForm.value.currency.toUpperCase())))
    //{
    //  alert('Currency code does not exist!')
    //}
    console.log(this.currencyCodes.includes(addForm.value.currency.toUpperCase()));
    if(!(this.currencyCodes.includes(addForm.value.currency.toUpperCase())) && !(this.currenciesNonCrypto.codes().includes(addForm.value.currency.toUpperCase())))
    {
     
      swal("Error",'Currency code does not exist!', "error");
    }
    else
    {
      addForm.value.currency = addForm.value.currency.toUpperCase();
      console.log(addForm.value.currency);
      this.accountService.addAccount(addForm.value).subscribe(
        (response: Account) => {
          console.log(response);
          this.getAccounts();
          addForm.reset();
        },
        (error: HttpErrorResponse) => {
          swal("Error", error.message, "error");
          addForm.reset();
        }
      );
    }

  }

  public onUpdateAccount(account: Account): void {
    //account.owner = this.editAccount.owner;
    this.accountService.updateAccount(account).subscribe(
      (response: Account) => {
        console.log(response);
        this.getAccounts();
      },
      (error: HttpErrorResponse) => {
        swal("Error", error.message, "error");
      }
    );
  }

  public onDeleteAccount(accountId: number): void {
    this.getLastBalance(accountId);
    if(this.lastBalance.availableDebit - this.lastBalance.availableCredit == 0 && this.lastBalance.pendingDebit == 0 && this.lastBalance.pendingCredit)
    {
      this.accountService.deleteAccount(accountId).subscribe(
        (response: void) => {
          console.log(response);
          this.getAccounts();
        },
        (error: HttpErrorResponse) => {
          swal("Error", error.message, "error");
        }
      );
    }
    else {
      swal("Error", "You cannot remove an account that still has money or pending transactions!", "error");
    }

  }

  public onOpenModal(account: Account, mode: string): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'add') {
      button.setAttribute('data-target', '#addAccountModal');
    }
    else if (mode === 'edit') {
      this.editAccount = account;
      button.setAttribute('data-target', '#updateAccountModal');
    }
    else if (mode === 'delete') {
      this.deleteAccount = account;
      button.setAttribute('data-target', '#deleteAccountModal');
    }
    else if (mode === 'transaction') {
      this.transactionAccountCredit = account;
      button.setAttribute('data-target', '#transactionAccountModal');
    }
   
    container.appendChild(button);
    button.click();
  }

  accountDetails(account: Account) {
    this.detailAccount = account;
    this.getAudits(account.id);
    const container = document.getElementById('details-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#detailAccountModal');
    container.appendChild(button);
    button.click();

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

  seeBalanceAudit(timestamp: string ) {
    this.getBalanceAudit(timestamp);
    const container = document.getElementById('balance-audit-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#balanceAuditModal');
    container.appendChild(button);
    button.click();

  }

  seePaymentAudit(timestamp: string ) {
    this.getPaymentAudit(timestamp);
    const container = document.getElementById('payment-audit-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#paymentAuditModal');
    container.appendChild(button);
    button.click();

  }

  seePayments(account: Account ) {
    this.debitAccountsFullName.splice(0);
    this.creditAccountsFullName.splice(0);
    //this.debitAccountsPublicKey = new Array(100);
    //this.creditAccountsPublicKey = new Array(100);
    console.log('SHOULD BE EMPTY ARRAYS \n')
    console.log(this.debitAccountsFullName);
    console.log(this.creditAccountsFullName);
    this.currentAccount = account;
    this.getPayments(account);
    const container = document.getElementById('payment-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#paymentModal');
    container.appendChild(button);
    button.click();

  }

  seePaymentsStatistics(account: Account ) {

    this.debitStatistics.splice(0);
    this.creditStatistics.splice(0);
    this.getPaymentsStatistics(account);
    const container = document.getElementById('payment-statistics-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#paymentStatisticsModal');
    container.appendChild(button);
    button.click();

  }


  approveAccounts() {
    this.router.navigate(['/approve-accounts']);

  }

  rejectedRemovedAccounts() {
    this.router.navigate(['/rejected-removed-accounts']);

  }


  currentBalance(account: Account) {
    this.getLastBalance(account.id);
    this.currentAccount = account;
    const container = document.getElementById('last-balance-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#lastBalanceModal');
    container.appendChild(button);
    button.click();

  }

  balanceHistory(account: Account) {
    this.getBalances(account.id);
    const container = document.getElementById('balance-history-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#balanceHistoryModal');
    container.appendChild(button);
    button.click();

  }

  
  public onTransaction(payment: NgForm, check2: NgForm, publicKeyDebit: NgForm) {
    payment.value.operation = "TRANSACTION";
    console.log(payment.value.amount);
    console.log(check2.value.amount2);
    if(payment.value.amount != check2.value.amount2)
    {
      swal("Error", "Please re-enter the same amount!", "error");
      payment.reset();
      publicKeyDebit.reset();
      check2.reset();
    }
    else {
      if(payment.value.amount < 0)
      {
        
        swal("Error", "You cannot enter a negative amount.", "error");
        publicKeyDebit.reset();
        check2.reset();
        payment.reset();
      }
      else {
        console.log('PUBLIC KEY DEBIT ACC');
        console.log(publicKeyDebit.value.publicKeyDebit);
        this.accountService.getAccountByPublicKey(publicKeyDebit.value.publicKeyDebit).subscribe(
          (response: Account) => {
            this.transactionAccountDebit = response;
            console.log(this.transactionAccountDebit);
            if (this.transactionAccountDebit.publicKey == this.transactionAccountCredit.publicKey) {
              
              swal("Error","You cannot make a transaction into the same account!", "error");
              payment.reset();
              publicKeyDebit.reset();
              check2.reset();
            } else {
              if (this.transactionAccountDebit.currency != this.transactionAccountCredit.currency) {
                
                swal("Error","The accounts of the transaction must have the same currency!", "error");
                payment.reset();
                publicKeyDebit.reset();
                check2.reset();
              } else {
                payment.value.debitAccountId = this.transactionAccountDebit.id;
                swal("Trying to mine the block.....");
                this.accountService.addPayment(payment.value, this.transactionAccountCredit.id).subscribe(
                  (response: any) => {
                    swal("Block mined!","Last added block was mined!", "success");
                    console.log(response);
                    this.getAccounts();
                    payment.reset();
                    publicKeyDebit.reset();
                    check2.reset();
                  },
                  (error: HttpErrorResponse) => {
                    swal("Error",error.error, "error");
                    payment.reset();
                    publicKeyDebit.reset();
                    check2.reset();
                  }
                );
              }
            }
          },
          (error: HttpErrorResponse) => {
            
            swal("Error","The debit account does not exist!", "error");
            payment.reset();
            publicKeyDebit.reset();
            check2.reset();
          }
        );
      }
    }

  }

}
