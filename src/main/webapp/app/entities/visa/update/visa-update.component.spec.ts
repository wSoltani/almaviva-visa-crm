jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VisaService } from '../service/visa.service';
import { IVisa, Visa } from '../visa.model';

import { VisaUpdateComponent } from './visa-update.component';

describe('Component Tests', () => {
  describe('Visa Management Update Component', () => {
    let comp: VisaUpdateComponent;
    let fixture: ComponentFixture<VisaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let visaService: VisaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VisaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VisaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VisaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      visaService = TestBed.inject(VisaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const visa: IVisa = { id: 456 };

        activatedRoute.data = of({ visa });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(visa));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const visa = { id: 123 };
        spyOn(visaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ visa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: visa }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(visaService.update).toHaveBeenCalledWith(visa);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const visa = new Visa();
        spyOn(visaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ visa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: visa }));
        saveSubject.complete();

        // THEN
        expect(visaService.create).toHaveBeenCalledWith(visa);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const visa = { id: 123 };
        spyOn(visaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ visa });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(visaService.update).toHaveBeenCalledWith(visa);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
