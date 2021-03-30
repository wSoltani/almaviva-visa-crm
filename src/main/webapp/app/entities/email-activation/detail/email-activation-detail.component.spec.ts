import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmailActivationDetailComponent } from './email-activation-detail.component';

describe('Component Tests', () => {
  describe('EmailActivation Management Detail Component', () => {
    let comp: EmailActivationDetailComponent;
    let fixture: ComponentFixture<EmailActivationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EmailActivationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ emailActivation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EmailActivationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EmailActivationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load emailActivation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.emailActivation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
