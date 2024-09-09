import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./user/login/login.component";
import {HomeComponent} from "./home/home.component";
import {ShowUsersComponent} from "./user/show-users/show-users.component";
import {ApproveUserComponent} from "./user/approve-user/approve-user.component";
import {RemovedUserComponent} from "./user/removed-user/removed-user.component";
import {ShowAccountsComponent} from "./account/show-accounts/show-accounts.component";
import {ApproveAccountComponent} from "./account/approve-account/approve-account.component";
import {RemovedAccountComponent} from "./account/removed-account/removed-account.component";
import {MenuComponent} from "./menu/menu.component";
import {ShowPaymentsComponent} from "./payment/show-payments/show-payments.component";

import {RemovedPaymentComponent} from "./payment/removed-payment/removed-payment.component";
import {AuthGuardService} from "./service/auth-guard.service";
import { BlockchainViewerComponent } from './blockchain-viewer/blockchain-viewer.component';


const routes: Routes = [ {path: '', component: LoginComponent},
  {path: 'home', component: MenuComponent, children: [{path:'', component:HomeComponent , canActivate:[AuthGuardService] }]},
  {path: 'users', component: MenuComponent, children: [{path:'', component:ShowUsersComponent, canActivate:[AuthGuardService]}]},
  {path: 'approve-users', component: MenuComponent, children: [{path:'', component:ApproveUserComponent, canActivate:[AuthGuardService]}]},
  {path: 'rejected-removed-users', component: MenuComponent, children: [{path:'', component:RemovedUserComponent, canActivate:[AuthGuardService]}]},
  {path: 'accounts', component: MenuComponent, children: [{path:'', component:ShowAccountsComponent, canActivate:[AuthGuardService]}]},
  {path: 'approve-accounts', component: MenuComponent, children: [{path:'', component:ApproveAccountComponent, canActivate:[AuthGuardService]}]},
  {path: 'rejected-removed-accounts', component: MenuComponent, children: [{path:'', component:RemovedAccountComponent, canActivate:[AuthGuardService]}]},
  {path: 'payments', component: MenuComponent, children: [{path:'', component:ShowPaymentsComponent, canActivate:[AuthGuardService]}]},

  {path: 'rejected-payments', component: MenuComponent, children: [{path:'', component:RemovedPaymentComponent, canActivate:[AuthGuardService]}]},

  {path: 'blockchain', component: MenuComponent, children: [{path:'', component:BlockchainViewerComponent, canActivate:[AuthGuardService]}]},

  ];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
