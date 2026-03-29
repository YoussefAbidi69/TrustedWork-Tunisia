import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FrontLayout } from './front-layout';

describe('FrontLayout', () => {
  let component: FrontLayout;
  let fixture: ComponentFixture<FrontLayout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FrontLayout]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FrontLayout);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
