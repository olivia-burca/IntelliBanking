import { Payment } from "./payment";

export class Block {
    id!: number;
    hash!: string;
    previousHash!: string;
    data!: string;
    timestamp!: number;
    nonce!: number;
    transactions!: Payment[];
  }
  