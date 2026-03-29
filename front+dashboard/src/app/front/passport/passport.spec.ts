import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Passport } from './passport';

describe('Passport', () => {
  let component: Passport;
  let fixture: ComponentFixture<Passport>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Passport]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Passport);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
