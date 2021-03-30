jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FolderService } from '../service/folder.service';
import { IFolder, Folder } from '../folder.model';
import { IAppointment } from 'app/entities/appointment/appointment.model';
import { AppointmentService } from 'app/entities/appointment/service/appointment.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { IAVService } from 'app/entities/av-service/av-service.model';
import { AVServiceService } from 'app/entities/av-service/service/av-service.service';
import { IVisa } from 'app/entities/visa/visa.model';
import { VisaService } from 'app/entities/visa/service/visa.service';

import { FolderUpdateComponent } from './folder-update.component';

describe('Component Tests', () => {
  describe('Folder Management Update Component', () => {
    let comp: FolderUpdateComponent;
    let fixture: ComponentFixture<FolderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let folderService: FolderService;
    let appointmentService: AppointmentService;
    let siteService: SiteService;
    let aVServiceService: AVServiceService;
    let visaService: VisaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FolderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FolderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FolderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      folderService = TestBed.inject(FolderService);
      appointmentService = TestBed.inject(AppointmentService);
      siteService = TestBed.inject(SiteService);
      aVServiceService = TestBed.inject(AVServiceService);
      visaService = TestBed.inject(VisaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call appointment query and add missing value', () => {
        const folder: IFolder = { id: 456 };
        const appointment: IAppointment = { id: 83484 };
        folder.appointment = appointment;

        const appointmentCollection: IAppointment[] = [{ id: 85762 }];
        spyOn(appointmentService, 'query').and.returnValue(of(new HttpResponse({ body: appointmentCollection })));
        const expectedCollection: IAppointment[] = [appointment, ...appointmentCollection];
        spyOn(appointmentService, 'addAppointmentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ folder });
        comp.ngOnInit();

        expect(appointmentService.query).toHaveBeenCalled();
        expect(appointmentService.addAppointmentToCollectionIfMissing).toHaveBeenCalledWith(appointmentCollection, appointment);
        expect(comp.appointmentsCollection).toEqual(expectedCollection);
      });

      it('Should call Site query and add missing value', () => {
        const folder: IFolder = { id: 456 };
        const site: ISite = { id: 54044 };
        folder.site = site;

        const siteCollection: ISite[] = [{ id: 30291 }];
        spyOn(siteService, 'query').and.returnValue(of(new HttpResponse({ body: siteCollection })));
        const additionalSites = [site];
        const expectedCollection: ISite[] = [...additionalSites, ...siteCollection];
        spyOn(siteService, 'addSiteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ folder });
        comp.ngOnInit();

        expect(siteService.query).toHaveBeenCalled();
        expect(siteService.addSiteToCollectionIfMissing).toHaveBeenCalledWith(siteCollection, ...additionalSites);
        expect(comp.sitesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call AVService query and add missing value', () => {
        const folder: IFolder = { id: 456 };
        const services: IAVService[] = [{ id: 69766 }];
        folder.services = services;

        const aVServiceCollection: IAVService[] = [{ id: 39040 }];
        spyOn(aVServiceService, 'query').and.returnValue(of(new HttpResponse({ body: aVServiceCollection })));
        const additionalAVServices = [...services];
        const expectedCollection: IAVService[] = [...additionalAVServices, ...aVServiceCollection];
        spyOn(aVServiceService, 'addAVServiceToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ folder });
        comp.ngOnInit();

        expect(aVServiceService.query).toHaveBeenCalled();
        expect(aVServiceService.addAVServiceToCollectionIfMissing).toHaveBeenCalledWith(aVServiceCollection, ...additionalAVServices);
        expect(comp.aVServicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Visa query and add missing value', () => {
        const folder: IFolder = { id: 456 };
        const visa: IVisa = { id: 21638 };
        folder.visa = visa;

        const visaCollection: IVisa[] = [{ id: 41602 }];
        spyOn(visaService, 'query').and.returnValue(of(new HttpResponse({ body: visaCollection })));
        const additionalVisas = [visa];
        const expectedCollection: IVisa[] = [...additionalVisas, ...visaCollection];
        spyOn(visaService, 'addVisaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ folder });
        comp.ngOnInit();

        expect(visaService.query).toHaveBeenCalled();
        expect(visaService.addVisaToCollectionIfMissing).toHaveBeenCalledWith(visaCollection, ...additionalVisas);
        expect(comp.visasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const folder: IFolder = { id: 456 };
        const appointment: IAppointment = { id: 55680 };
        folder.appointment = appointment;
        const site: ISite = { id: 79320 };
        folder.site = site;
        const services: IAVService = { id: 48997 };
        folder.services = [services];
        const visa: IVisa = { id: 21801 };
        folder.visa = visa;

        activatedRoute.data = of({ folder });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(folder));
        expect(comp.appointmentsCollection).toContain(appointment);
        expect(comp.sitesSharedCollection).toContain(site);
        expect(comp.aVServicesSharedCollection).toContain(services);
        expect(comp.visasSharedCollection).toContain(visa);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const folder = { id: 123 };
        spyOn(folderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ folder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: folder }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(folderService.update).toHaveBeenCalledWith(folder);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const folder = new Folder();
        spyOn(folderService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ folder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: folder }));
        saveSubject.complete();

        // THEN
        expect(folderService.create).toHaveBeenCalledWith(folder);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const folder = { id: 123 };
        spyOn(folderService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ folder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(folderService.update).toHaveBeenCalledWith(folder);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAppointmentById', () => {
        it('Should return tracked Appointment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAppointmentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSiteById', () => {
        it('Should return tracked Site primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSiteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAVServiceById', () => {
        it('Should return tracked AVService primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAVServiceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackVisaById', () => {
        it('Should return tracked Visa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVisaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedAVService', () => {
        it('Should return option if no AVService is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedAVService(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected AVService for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedAVService(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this AVService is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedAVService(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
