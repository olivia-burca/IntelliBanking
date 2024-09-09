import {User} from "./user";

export class AccountHistory {
  id!: number;
  accountId!: number;
  fullname!: string;
  address!: string;
  currency!: string;
  accountstatus!: string;
  status!: string;
  nextstatus!: string;
  owner!: User; // ????????
  timestamp!: string;
  owner_id!: number;
  publicKey!: string;
  privateKey!: string;
}
