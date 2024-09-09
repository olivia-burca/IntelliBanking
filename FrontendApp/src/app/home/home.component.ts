import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import * as bcrypt from "bcryptjs";

import {Subject} from "rxjs";
import {DataTableDirective} from "angular-datatables";
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Cryptocurrency } from '../model/cryptocurrency';
import { CryptoService } from '../service/crypto.service';
import swal from 'sweetalert';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public detailCrypto: Cryptocurrency;
  public cryptocurrencies: Cryptocurrency[];
  displayedColumns: string[] = ['rank','name', 'symbol', 'price', 'marketCap', 'vwap24h', 'supply', 'volume24h','change24h', 'buttons'];
  dataSource!: MatTableDataSource<Cryptocurrency>;
  @ViewChild('paginator', {static: true}) paginator!: MatPaginator;
  @ViewChild('sort', {static: true}) sort!: MatSort;

  constructor(private router:Router,private cryptoService : CryptoService,) { }

  ngOnInit(): void {
    this.getCryptocurrencies();
  }

  public getCryptocurrencies(): void {
    this.cryptoService.getCryptocurrencies().subscribe(
      (response: Cryptocurrency[]) => {
        this.cryptocurrencies = response;
        console.log(this.cryptocurrencies);

        for(let i = 0; i < this.cryptocurrencies.length; i++) {
          let marketCapNr = parseFloat(this.cryptocurrencies[i].marketCap);
          if (marketCapNr > 999999) {
            marketCapNr = marketCapNr/1000000;
            let marketCapStr = marketCapNr.toString();
            let index = marketCapStr.indexOf(".");
            marketCapStr = marketCapStr.slice(0, index + 3);
            this.cryptocurrencies[i].marketCap = marketCapStr.concat("m");
          }

          let supplyNr = parseFloat(this.cryptocurrencies[i].supply);
          if (supplyNr > 999999) {
            supplyNr = supplyNr/1000000;
            let supplyStr = supplyNr.toString();
            let index = supplyStr.indexOf(".");
            supplyStr = supplyStr.slice(0, index + 3);
            this.cryptocurrencies[i].supply = supplyStr.concat("m");
          }

          let volumeNr = parseFloat(this.cryptocurrencies[i].volume24h);
          if (volumeNr > 999999) {
            volumeNr = volumeNr/1000000;
            let volumeStr = volumeNr.toString();
            let index = volumeStr.indexOf(".");
            volumeStr = volumeStr.slice(0, index + 3);
            this.cryptocurrencies[i].volume24h = volumeStr.concat("m");
          }

        }

        this.dataSource = new MatTableDataSource(this.cryptocurrencies);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

      },
      (error: HttpErrorResponse) => {
        swal("Error", error.message, "error");
      }
    );
  }

  isPositiveChange(change : string) {
    let changeNr = parseFloat(change);
    if(changeNr > 0) return true;
    else return false;

  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  cryptoDetails(crypto: Cryptocurrency) {
    this.detailCrypto = crypto;
    const container = document.getElementById('details-container');

    while (container.firstChild) {
      container.removeChild(container.firstChild);
    }

    var baseUrl = "https://widgets.cryptocompare.com/";

    var s = document.createElement("script");
    s.type = "text/javascript";
    s.async = true;
    var symbol = crypto.symbol
    if(symbol == "CDT")
    {
      symbol = "BCDT";
    }
    if(symbol == "AGIX")
    {
      symbol = "AGI";
    }

    var appName = encodeURIComponent(window.location.hostname);
    if(appName==""){appName="local";}var theUrl = baseUrl+'serve/v2/coin/chart?fsym=' + symbol+ '&tsym=USD&period=1Y';
    s.src = theUrl + ( theUrl.indexOf("?") >= 0 ? "&" : "?") + "app=" + appName;
    container.appendChild(s);

    
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#detailCryptoModal');
    container.appendChild(button);
    button.click();
   

  }

}
