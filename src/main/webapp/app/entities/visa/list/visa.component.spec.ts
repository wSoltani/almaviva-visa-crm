jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VisaService } from '../service/visa.service';

import { VisaComponent } from './visa.component';

describe('Component Tests', () => {
  describe('Visa Management Component', () => {
    let comp: VisaComponent;
    let fixture: ComponentFixture<VisaComponent>;
    let service: VisaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VisaComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(VisaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VisaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(VisaService);

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
      expect(comp.visas?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
