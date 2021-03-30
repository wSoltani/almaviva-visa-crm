import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VisaDetailComponent } from './visa-detail.component';

describe('Component Tests', () => {
  describe('Visa Management Detail Component', () => {
    let comp: VisaDetailComponent;
    let fixture: ComponentFixture<VisaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VisaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ visa: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VisaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VisaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load visa on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.visa).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
