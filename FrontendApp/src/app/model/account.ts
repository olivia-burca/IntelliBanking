import {User} from "./user";

export class Account {
  id!: number;
  fullname!: string;
  address!: string;
  currency!: string;
  accountStatus!: string;
  status!: string;
  nextstatus!: string;
  owner: User; // ????????
  owner_id!: number;
  publicKey!: string;
  privateKey!: string;
}
