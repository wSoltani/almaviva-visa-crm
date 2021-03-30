jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmailActivationService } from '../service/email-activation.service';

import { EmailActivationComponent } from './email-activation.component';

describe('Component Tests', () => {
  describe('EmailActivation Management Component', () => {
    let comp: EmailActivationComponent;
    let fixture: ComponentFixture<EmailActivationComponent>;
    let service: EmailActivationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmailActivationComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(EmailActivationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmailActivationComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EmailActivationService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.emailActivations?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
