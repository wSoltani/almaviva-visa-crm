jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VisaDocumentsService } from '../service/visa-documents.service';
import { IVisaDocuments, VisaDocuments } from '../visa-documents.model';
import { IVisa } from 'app/entities/visa/visa.model';
import { VisaService } from 'app/entities/visa/service/visa.service';

import { VisaDocumentsUpdateComponent } from './visa-documents-update.component';

describe('Component Tests', () => {
  describe('VisaDocuments Management Update Component', () => {
    let comp: VisaDocumentsUpdateComponent;
    let fixture: ComponentFixture<VisaDocumentsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let visaDocumentsService: VisaDocumentsService;
    let visaService: VisaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VisaDocumentsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VisaDocumentsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VisaDocumentsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      visaDocumentsService = TestBed.inject(VisaDocumentsService);
      visaService = TestBed.inject(VisaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Visa query and add missing value', () => {
        const visaDocuments: IVisaDocuments = { id: 456 };
        const visas: IVisa[] = [{ id: 35559 }];
        visaDocuments.visas = visas;

        const visaCollection: IVisa[] = [{ id: 11167 }];
        spyOn(visaService, 'query').and.returnValue(of(new HttpResponse({ body: visaCollection })));
        const additionalVisas = [...visas];
        const expectedCollection: IVisa[] = [...additionalVisas, ...visaCollection];
        spyOn(visaService, 'addVisaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ visaDocuments });
        comp.ngOnInit();

        expect(visaService.query).toHaveBeenCalled();
        expect(visaService.addVisaToCollectionIfMissing).toHaveBeenCalledWith(visaCollection, ...additionalVisas);
        expect(comp.visasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const visaDocuments: IVisaDocuments = { id: 456 };
        const visas: IVisa = { id: 59566 };
        visaDocuments.visas = [visas];

        activatedRoute.data = of({ visaDocuments });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(visaDocuments));
        expect(comp.visasSharedCollection).toContain(visas);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const visaDocuments = { id: 123 };
        spyOn(visaDocumentsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ visaDocuments });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: visaDocuments }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(visaDocumentsService.update).toHaveBeenCalledWith(visaDocuments);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const visaDocuments = new VisaDocuments();
        spyOn(visaDocumentsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ visaDocuments });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: visaDocuments }));
        saveSubject.complete();

        // THEN
        expect(visaDocumentsService.create).toHaveBeenCalledWith(visaDocuments);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const visaDocuments = { id: 123 };
        spyOn(visaDocumentsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ visaDocuments });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(visaDocumentsService.update).toHaveBeenCalledWith(visaDocuments);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVisaById', () => {
        it('Should return tracked Visa primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVisaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedVisa', () => {
        it('Should return option if no Visa is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedVisa(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Visa for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedVisa(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Visa is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedVisa(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
