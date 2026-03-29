import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MySkills } from './my-skills';

describe('MySkills', () => {
  let component: MySkills;
  let fixture: ComponentFixture<MySkills>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MySkills]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MySkills);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
