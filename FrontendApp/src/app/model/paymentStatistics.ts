export class PaymentStatistics {
  status!: string;
  count!: number;
  sum!: number;
  constructor(status: string, count:number, sum:number) {
    this.status = status;
    this.count = count;
    this.sum = sum;
  }
}
