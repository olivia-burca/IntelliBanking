import {Account} from "./account";

export class Payment {
  id!: number;
  operation!: string;
  amount!: number;
  debitAccountId!: number;
  debitAccountPublicKey!: string;
  status!: string;
  nextstatus!: string;
  timestamp!: string;
  creditAccount!: Account; // ????????
  //debitAccountIban! : string;
  transactionHash!: string;
  sender!: string;
  recipient!: string;
  sequence!: number;

}
