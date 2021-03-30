jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MandateService } from '../service/mandate.service';

import { MandateComponent } from './mandate.component';

describe('Component Tests', () => {
  describe('Mandate Management Component', () => {
    let comp: MandateComponent;
    let fixture: ComponentFixture<MandateComponent>;
    let service: MandateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MandateComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(MandateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MandateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MandateService);

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
      expect(comp.mandates?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
