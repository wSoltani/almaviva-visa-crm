jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SiteService } from '../service/site.service';
import { ISite, Site } from '../site.model';

import { SiteUpdateComponent } from './site-update.component';

describe('Component Tests', () => {
  describe('Site Management Update Component', () => {
    let comp: SiteUpdateComponent;
    let fixture: ComponentFixture<SiteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let siteService: SiteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SiteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SiteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SiteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      siteService = TestBed.inject(SiteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const site: ISite = { id: 456 };

        activatedRoute.data = of({ site });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(site));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const site = { id: 123 };
        spyOn(siteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ site });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: site }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(siteService.update).toHaveBeenCalledWith(site);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const site = new Site();
        spyOn(siteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ site });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: site }));
        saveSubject.complete();

        // THEN
        expect(siteService.create).toHaveBeenCalledWith(site);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const site = { id: 123 };
        spyOn(siteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ site });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(siteService.update).toHaveBeenCalledWith(site);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
