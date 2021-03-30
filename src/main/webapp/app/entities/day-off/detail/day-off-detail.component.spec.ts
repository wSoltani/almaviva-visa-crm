import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DayOffDetailComponent } from './day-off-detail.component';

describe('Component Tests', () => {
  describe('DayOff Management Detail Component', () => {
    let comp: DayOffDetailComponent;
    let fixture: ComponentFixture<DayOffDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DayOffDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ dayOff: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DayOffDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DayOffDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load dayOff on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dayOff).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
