import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SiteConfigurationDetailComponent } from './site-configuration-detail.component';

describe('Component Tests', () => {
  describe('SiteConfiguration Management Detail Component', () => {
    let comp: SiteConfigurationDetailComponent;
    let fixture: ComponentFixture<SiteConfigurationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SiteConfigurationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ siteConfiguration: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SiteConfigurationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SiteConfigurationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load siteConfiguration on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.siteConfiguration).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
