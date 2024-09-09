import { Component, OnInit, Input } from '@angular/core';
import { Block } from '../model/block';
import { PaymentService } from '../service/payment.service';
import { BlockService } from '../service/block.service';
import { HttpErrorResponse } from '@angular/common/http';
import swal from 'sweetalert';

@Component({
  selector: 'app-block-view',
  templateUrl: './block-view.component.html',
  styleUrls: ['./block-view.component.css']
})
export class BlockViewComponent implements OnInit {

  @Input()
  public block : Block;

  @Input()
  public selectedBlock: Block;

  public blocksInChain: Block[] = [];

  constructor(private blockchainService: BlockService) {
  }

  ngOnInit() {
    this.getBlocks();
  }

  public getBlocks(): void {
    this.blockchainService.getBlocks().subscribe(
      (response: Block[]) => {
        this.blocksInChain = response;
        console.log(this.blocksInChain);
      },
      (error: HttpErrorResponse) => {
        swal("Error",error.message, "error");
      }
    );
  }

  isSelectedBlock() {
    return this.block === this.selectedBlock;
  }

  getBlockNumber(block : Block) {
    for(let i = 0; i < this.blocksInChain.length; i++) {
      if(this.blocksInChain[i].hash == block.hash)
        return i +1;
    }
    return -1;
  }
  

}
