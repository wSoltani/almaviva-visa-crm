jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SiteService } from '../service/site.service';

import { SiteComponent } from './site.component';

describe('Component Tests', () => {
  describe('Site Management Component', () => {
    let comp: SiteComponent;
    let fixture: ComponentFixture<SiteComponent>;
    let service: SiteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SiteComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(SiteComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SiteComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SiteService);

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
      expect(comp.sites?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
