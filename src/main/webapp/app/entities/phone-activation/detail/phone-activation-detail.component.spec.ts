import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PhoneActivationDetailComponent } from './phone-activation-detail.component';

describe('Component Tests', () => {
  describe('PhoneActivation Management Detail Component', () => {
    let comp: PhoneActivationDetailComponent;
    let fixture: ComponentFixture<PhoneActivationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PhoneActivationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ phoneActivation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PhoneActivationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PhoneActivationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load phoneActivation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.phoneActivation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
