import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemovedPaymentComponent } from './removed-payment.component';

describe('RemovedPaymentComponent', () => {
  let component: RemovedPaymentComponent;
  let fixture: ComponentFixture<RemovedPaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RemovedPaymentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RemovedPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
