import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TakeExam } from './take-exam';

describe('TakeExam', () => {
  let component: TakeExam;
  let fixture: ComponentFixture<TakeExam>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TakeExam]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TakeExam);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
