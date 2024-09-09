import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemovedUserComponent } from './removed-user.component';

describe('RemovedUserComponent', () => {
  let component: RemovedUserComponent;
  let fixture: ComponentFixture<RemovedUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RemovedUserComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RemovedUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
