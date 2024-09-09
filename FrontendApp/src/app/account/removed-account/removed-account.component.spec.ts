import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemovedAccountComponent } from './removed-account.component';

describe('RemovedAccountComponent', () => {
  let component: RemovedAccountComponent;
  let fixture: ComponentFixture<RemovedAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RemovedAccountComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RemovedAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
