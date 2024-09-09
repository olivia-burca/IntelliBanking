import { Component, OnInit } from '@angular/core';
import { Block } from '../model/block';
import { BlockScrollStrategy } from '@angular/cdk/overlay';
import { BlockService } from '../service/block.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';
import { Payment } from '../model/payment';

@Component({
  selector: 'app-blockchain-viewer',
  templateUrl: './blockchain-viewer.component.html',
  styleUrls: ['./blockchain-viewer.component.css']
})
export class BlockchainViewerComponent implements OnInit {

  public blocks : Block[] = [];
  public currentTransactions : Payment[] = [];
  public selectedBlock : Block = null;

  constructor(private blockchainService: BlockService) {
    
  }

  ngOnInit() {
    this.getBlocks();
  }

  public getBlocks(): void {
    this.blockchainService.getBlocks().subscribe(
      (response: Block[]) => {
        this.blocks = response;
        console.log(this.blocks);
        this.selectedBlock = this.blocks[0];
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
  }

  public getTransactions(blockId : number): void {
    this.blockchainService.getPaymentsOfBlock(blockId).subscribe(
      (response: Payment[]) => {
        this.currentTransactions = response;
        console.log("CURRENT TRANSACTIONS"+this.currentTransactions);
        
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
  }
  
  

  showTransactions(block : Block) {
    console.log(block);
    this.selectedBlock = block;
    this.getTransactions(block.id);
    return false;
  }
/*
  blockHasTx(block) {
    return block.transactions.length > 0;
  }

  selectedBlockHasTx() {
    return this.blockHasTx(this.selectedBlock);
  } */

  isSelectedBlock(block : Block) {
    return this.selectedBlock === block;
  }

  getBlockNumber(block : Block) {
    return this.blocks.indexOf(block) + 1;
  }

}
