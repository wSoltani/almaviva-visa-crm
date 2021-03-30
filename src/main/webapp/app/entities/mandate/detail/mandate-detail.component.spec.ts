import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MandateDetailComponent } from './mandate-detail.component';

describe('Component Tests', () => {
  describe('Mandate Management Detail Component', () => {
    let comp: MandateDetailComponent;
    let fixture: ComponentFixture<MandateDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MandateDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mandate: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MandateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MandateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mandate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mandate).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
