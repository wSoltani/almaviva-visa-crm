jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MandateService } from '../service/mandate.service';
import { IMandate, Mandate } from '../mandate.model';
import { IFolder } from 'app/entities/folder/folder.model';
import { FolderService } from 'app/entities/folder/service/folder.service';

import { MandateUpdateComponent } from './mandate-update.component';

describe('Component Tests', () => {
  describe('Mandate Management Update Component', () => {
    let comp: MandateUpdateComponent;
    let fixture: ComponentFixture<MandateUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mandateService: MandateService;
    let folderService: FolderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MandateUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MandateUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MandateUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mandateService = TestBed.inject(MandateService);
      folderService = TestBed.inject(FolderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Folder query and add missing value', () => {
        const mandate: IMandate = { id: 456 };
        const folder: IFolder = { id: 10435 };
        mandate.folder = folder;

        const folderCollection: IFolder[] = [{ id: 71449 }];
        spyOn(folderService, 'query').and.returnValue(of(new HttpResponse({ body: folderCollection })));
        const additionalFolders = [folder];
        const expectedCollection: IFolder[] = [...additionalFolders, ...folderCollection];
        spyOn(folderService, 'addFolderToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ mandate });
        comp.ngOnInit();

        expect(folderService.query).toHaveBeenCalled();
        expect(folderService.addFolderToCollectionIfMissing).toHaveBeenCalledWith(folderCollection, ...additionalFolders);
        expect(comp.foldersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const mandate: IMandate = { id: 456 };
        const folder: IFolder = { id: 43719 };
        mandate.folder = folder;

        activatedRoute.data = of({ mandate });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mandate));
        expect(comp.foldersSharedCollection).toContain(folder);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mandate = { id: 123 };
        spyOn(mandateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mandate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mandate }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mandateService.update).toHaveBeenCalledWith(mandate);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mandate = new Mandate();
        spyOn(mandateService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mandate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mandate }));
        saveSubject.complete();

        // THEN
        expect(mandateService.create).toHaveBeenCalledWith(mandate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const mandate = { id: 123 };
        spyOn(mandateService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ mandate });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mandateService.update).toHaveBeenCalledWith(mandate);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFolderById', () => {
        it('Should return tracked Folder primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFolderById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
