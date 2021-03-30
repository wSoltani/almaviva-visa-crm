jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AVServiceService } from '../service/av-service.service';

import { AVServiceComponent } from './av-service.component';

describe('Component Tests', () => {
  describe('AVService Management Component', () => {
    let comp: AVServiceComponent;
    let fixture: ComponentFixture<AVServiceComponent>;
    let service: AVServiceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AVServiceComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(AVServiceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AVServiceComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AVServiceService);

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
      expect(comp.aVServices?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
