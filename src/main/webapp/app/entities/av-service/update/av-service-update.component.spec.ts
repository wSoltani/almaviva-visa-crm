jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AVServiceService } from '../service/av-service.service';
import { IAVService, AVService } from '../av-service.model';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

import { AVServiceUpdateComponent } from './av-service-update.component';

describe('Component Tests', () => {
  describe('AVService Management Update Component', () => {
    let comp: AVServiceUpdateComponent;
    let fixture: ComponentFixture<AVServiceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let aVServiceService: AVServiceService;
    let siteService: SiteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AVServiceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AVServiceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AVServiceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      aVServiceService = TestBed.inject(AVServiceService);
      siteService = TestBed.inject(SiteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Site query and add missing value', () => {
        const aVService: IAVService = { id: 456 };
        const site: ISite = { id: 60231 };
        aVService.site = site;

        const siteCollection: ISite[] = [{ id: 42854 }];
        spyOn(siteService, 'query').and.returnValue(of(new HttpResponse({ body: siteCollection })));
        const additionalSites = [site];
        const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
        spyOn(siteService, 'addSiteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ aVService });
        comp.ngOnInit();

        expect(siteService.query).toHaveBeenCalled();
        expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(siteCollection, ...additionalSites);
        expect(comp.sitesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const aVService: IAVService = { id: 456 };
        const site: ISite = { id: 24779 };
        aVService.site = site;

        activatedRoute.data = of({ aVService });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(aVService));
        expect(comp.sitesSharedCollection).toContain(site);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const aVService = { id: 123 };
        spyOn(aVServiceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ aVService });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aVService }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(aVServiceService.update).toHaveBeenCalledWith(aVService);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const aVService = new AVService();
        spyOn(aVServiceService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ aVService });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: aVService }));
        saveSubject.complete();

        // THEN
        expect(aVServiceService.create).toHaveBeenCalledWith(aVService);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const aVService = { id: 123 };
        spyOn(aVServiceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ aVService });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(aVServiceService.update).toHaveBeenCalledWith(aVService);
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
