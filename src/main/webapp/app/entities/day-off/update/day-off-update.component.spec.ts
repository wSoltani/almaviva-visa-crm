jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DayOffService } from '../service/day-off.service';
import { IDayOff, DayOff } from '../day-off.model';
import { ISiteConfiguration } from 'app/entities/site-configuration/site-configuration.model';
import { SiteConfigurationService } from 'app/entities/site-configuration/service/site-configuration.service';

import { DayOffUpdateComponent } from './day-off-update.component';

describe('Component Tests', () => {
  describe('DayOff Management Update Component', () => {
    let comp: DayOffUpdateComponent;
    let fixture: ComponentFixture<DayOffUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let dayOffService: DayOffService;
    let siteConfigurationService: SiteConfigurationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DayOffUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DayOffUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DayOffUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      dayOffService = TestBed.inject(DayOffService);
      siteConfigurationService = TestBed.inject(SiteConfigurationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call SiteConfiguration query and add missing value', () => {
        const dayOff: IDayOff = { id: 456 };
        const siteConfiguration: ISiteConfiguration = { id: 55025 };
        dayOff.siteConfiguration = siteConfiguration;

        const siteConfigurationCollection: ISiteConfiguration[] = [{ id: 34903 }];
        spyOn(siteConfigurationService, 'query').and.returnValue(of(new HttpResponse({ body: siteConfigurationCollection })));
        const additionalSiteConfigurations = [siteConfiguration];
        const expectedCollection: ISiteConfiguration[] = [...additionalSiteConfigurations, ...siteConfigurationCollection];
        spyOn(siteConfigurationService, 'addSiteConfigurationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ dayOff });
        comp.ngOnInit();

        expect(siteConfigurationService.query).toHaveBeenCalled();
        expect(siteConfigurationService.addSiteConfigurationToCollectionIfMissing).toHaveBeenCalledWith(
          siteConfigurationCollection,
          ...additionalSiteConfigurations
        );
        expect(comp.siteConfigurationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const dayOff: IDayOff = { id: 456 };
        const siteConfiguration: ISiteConfiguration = { id: 50552 };
        dayOff.siteConfiguration = siteConfiguration;

        activatedRoute.data = of({ dayOff });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(dayOff));
        expect(comp.siteConfigurationsSharedCollection).toContain(siteConfiguration);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dayOff = { id: 123 };
        spyOn(dayOffService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dayOff });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dayOff }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(dayOffService.update).toHaveBeenCalledWith(dayOff);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dayOff = new DayOff();
        spyOn(dayOffService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dayOff });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: dayOff }));
        saveSubject.complete();

        // THEN
        expect(dayOffService.create).toHaveBeenCalledWith(dayOff);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const dayOff = { id: 123 };
        spyOn(dayOffService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ dayOff });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(dayOffService.update).toHaveBeenCalledWith(dayOff);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackSiteConfigurationById', () => {
        it('Should return tracked SiteConfiguration primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSiteConfigurationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
