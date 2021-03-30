jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PhoneActivationService } from '../service/phone-activation.service';

import { PhoneActivationComponent } from './phone-activation.component';

describe('Component Tests', () => {
  describe('PhoneActivation Management Component', () => {
    let comp: PhoneActivationComponent;
    let fixture: ComponentFixture<PhoneActivationComponent>;
    let service: PhoneActivationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PhoneActivationComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(PhoneActivationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PhoneActivationComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PhoneActivationService);

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
      expect(comp.phoneActivations?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
