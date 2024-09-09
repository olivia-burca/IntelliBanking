import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { LoginComponent } from './user/login/login.component';
import {UserService} from "./service/user.service";
import { HomeComponent } from './home/home.component';
import { ShowUsersComponent } from './user/show-users/show-users.component';
import { ApproveUserComponent } from './user/approve-user/approve-user.component';
import { RemovedUserComponent } from './user/removed-user/removed-user.component';
import { ShowAccountsComponent } from './account/show-accounts/show-accounts.component';
import { ApproveAccountComponent } from './account/approve-account/approve-account.component';
import { RemovedAccountComponent } from './account/removed-account/removed-account.component';
import { MenuComponent } from './menu/menu.component';
import { ShowPaymentsComponent } from './payment/show-payments/show-payments.component';
import { RemovedPaymentComponent } from './payment/removed-payment/removed-payment.component';
import {DataTablesModule} from 'angular-datatables';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import {MatButtonModule} from "@angular/material/button";
import { NgNumberFormatterModule } from 'ng-number-formatter';
import { BlockViewComponent } from './block-view/block-view.component';
import { BlockchainViewerComponent } from './blockchain-viewer/blockchain-viewer.component';
import { TransactionsTableComponent } from './transactions-table/transactions-table.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    ShowUsersComponent,
    ApproveUserComponent,
    RemovedUserComponent,
    ShowAccountsComponent,
    ApproveAccountComponent,
    RemovedAccountComponent,
    MenuComponent,
    ShowPaymentsComponent,
    RemovedPaymentComponent,
    BlockViewComponent,
    BlockchainViewerComponent,
    TransactionsTableComponent

  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule, FormsModule,
        ReactiveFormsModule,
        MatTableModule,
        MatSortModule,
        MatSelectModule,
        MatPaginatorModule,
        MatFormFieldModule,
        MatTooltipModule,
        MatInputModule,
        MatIconModule, 
        BrowserAnimationsModule,
        DataTablesModule, MatDatepickerModule,
        MatNativeDateModule, MatButtonModule,
        NgNumberFormatterModule
    ],
  providers: [UserService],
  bootstrap: [AppComponent]
})
export class AppModule { }
