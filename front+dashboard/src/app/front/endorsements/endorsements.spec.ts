import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Endorsements } from './endorsements';

describe('Endorsements', () => {
  let component: Endorsements;
  let fixture: ComponentFixture<Endorsements>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Endorsements]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Endorsements);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
