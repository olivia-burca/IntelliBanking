import { Component, Input, OnInit } from '@angular/core';
import { BlockService } from '../service/block.service';
import { Payment } from '../model/payment';

@Component({
  selector: 'app-transactions-table',
  templateUrl: './transactions-table.component.html',
  styleUrls: ['./transactions-table.component.css']
})
export class TransactionsTableComponent implements OnInit {

  @Input()
  public transactions : Payment[] = [];

  constructor(public blockchainService: BlockService) { }

  ngOnInit() {
  }

}
