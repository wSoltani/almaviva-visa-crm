jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SiteConfigurationService } from '../service/site-configuration.service';

import { SiteConfigurationComponent } from './site-configuration.component';

describe('Component Tests', () => {
  describe('SiteConfiguration Management Component', () => {
    let comp: SiteConfigurationComponent;
    let fixture: ComponentFixture<SiteConfigurationComponent>;
    let service: SiteConfigurationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SiteConfigurationComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(SiteConfigurationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SiteConfigurationComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SiteConfigurationService);

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
      expect(comp.siteConfigurations?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
