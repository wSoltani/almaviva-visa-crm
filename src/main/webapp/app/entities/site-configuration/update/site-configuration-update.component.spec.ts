jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SiteConfigurationService } from '../service/site-configuration.service';
import { ISiteConfiguration, SiteConfiguration } from '../site-configuration.model';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

import { SiteConfigurationUpdateComponent } from './site-configuration-update.component';

describe('Component Tests', () => {
  describe('SiteConfiguration Management Update Component', () => {
    let comp: SiteConfigurationUpdateComponent;
    let fixture: ComponentFixture<SiteConfigurationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let siteConfigurationService: SiteConfigurationService;
    let siteService: SiteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SiteConfigurationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SiteConfigurationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SiteConfigurationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      siteConfigurationService = TestBed.inject(SiteConfigurationService);
      siteService = TestBed.inject(SiteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Site query and add missing value', () => {
        const siteConfiguration: ISiteConfiguration = { id: 456 };
        const site: ISite = { id: 10166 };
        siteConfiguration.site = site;

        const siteCollection: ISite[] = [{ id: 83396 }];
        spyOn(siteService, 'query').and.returnValue(of(new HttpResponse({ body: siteCollection })));
        const additionalSites = [site];
        const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
        spyOn(siteService, 'addSiteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ siteConfiguration });
        comp.ngOnInit();

        expect(siteService.query).toHaveBeenCalled();
        expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(siteCollection, ...additionalSites);
        expect(comp.sitesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const siteConfiguration: ISiteConfiguration = { id: 456 };
        const site: ISite = { id: 10902 };
        siteConfiguration.site = site;

        activatedRoute.data = of({ siteConfiguration });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(siteConfiguration));
        expect(comp.sitesSharedCollection).toContain(site);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const siteConfiguration = { id: 123 };
        spyOn(siteConfigurationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ siteConfiguration });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: siteConfiguration }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(siteConfigurationService.update).toHaveBeenCalledWith(siteConfiguration);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const siteConfiguration = new SiteConfiguration();
        spyOn(siteConfigurationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ siteConfiguration });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: siteConfiguration }));
        saveSubject.complete();

        // THEN
        expect(siteConfigurationService.create).toHaveBeenCalledWith(siteConfiguration);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const siteConfiguration = { id: 123 };
        spyOn(siteConfigurationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ siteConfiguration });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(siteConfigurationService.update).toHaveBeenCalledWith(siteConfiguration);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSiteById', () => {
        it('Should return tracked Site primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSiteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
