jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonService } from '../service/person.service';
import { IPerson, Person } from '../person.model';
import { IFolder } from 'app/entities/folder/folder.model';
import { FolderService } from 'app/entities/folder/service/folder.service';

import { PersonUpdateComponent } from './person-update.component';

describe('Component Tests', () => {
  describe('Person Management Update Component', () => {
    let comp: PersonUpdateComponent;
    let fixture: ComponentFixture<PersonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personService: PersonService;
    let folderService: FolderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personService = TestBed.inject(PersonService);
      folderService = TestBed.inject(FolderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call folder query and add missing value', () => {
        const person: IPerson = { id: 456 };
        const folder: IFolder = { id: 24824 };
        person.folder = folder;

        const folderCollection: IFolder[] = [{ id: 79640 }];
        spyOn(folderService, 'query').and.returnValue(of(new HttpResponse({ body: folderCollection })));
        const expectedCollection: IFolder[] = [folder, ...folderCollection];
        spyOn(folderService, 'addFolderToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(folderService.query).toHaveBeenCalled();
        expect(folderService.addFolderToCollectionIfMissing).toHaveBeenCalledWith(folderCollection, folder);
        expect(comp.foldersCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const person: IPerson = { id: 456 };
        const folder: IFolder = { id: 72149 };
        person.folder = folder;

        activatedRoute.data = of({ person });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(person));
        expect(comp.foldersCollection).toContain(folder);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const person = { id: 123 };
        spyOn(personService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: person }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personService.update).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const person = new Person();
        spyOn(personService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: person }));
        saveSubject.complete();

        // THEN
        expect(personService.create).toHaveBeenCalledWith(person);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const person = { id: 123 };
        spyOn(personService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ person });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personService.update).toHaveBeenCalledWith(person);
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
