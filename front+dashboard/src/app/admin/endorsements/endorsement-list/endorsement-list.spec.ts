import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EndorsementList } from './endorsement-list';

describe('EndorsementList', () => {
  let component: EndorsementList;
  let fixture: ComponentFixture<EndorsementList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EndorsementList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EndorsementList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
