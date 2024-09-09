import {Account} from "./account";

export class Balance {
  id!: number;
  iban!: string;
  availableDebit!: number;
  availableCredit!: number;
  pendingDebit!: number;
  pendingCredit!: number;
  status!: string;
  nextstatus!: string;
  timestamp!: string;
  account!: Account; // ????????

}
