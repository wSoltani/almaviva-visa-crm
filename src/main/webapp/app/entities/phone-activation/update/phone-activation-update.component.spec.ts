jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PhoneActivationService } from '../service/phone-activation.service';
import { IPhoneActivation, PhoneActivation } from '../phone-activation.model';

import { PhoneActivationUpdateComponent } from './phone-activation-update.component';

describe('Component Tests', () => {
  describe('PhoneActivation Management Update Component', () => {
    let comp: PhoneActivationUpdateComponent;
    let fixture: ComponentFixture<PhoneActivationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let phoneActivationService: PhoneActivationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PhoneActivationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PhoneActivationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PhoneActivationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      phoneActivationService = TestBed.inject(PhoneActivationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const phoneActivation: IPhoneActivation = { id: 456 };

        activatedRoute.data = of({ phoneActivation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(phoneActivation));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const phoneActivation = { id: 123 };
        spyOn(phoneActivationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ phoneActivation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: phoneActivation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(phoneActivationService.update).toHaveBeenCalledWith(phoneActivation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const phoneActivation = new PhoneActivation();
        spyOn(phoneActivationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ phoneActivation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: phoneActivation }));
        saveSubject.complete();

        // THEN
        expect(phoneActivationService.create).toHaveBeenCalledWith(phoneActivation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const phoneActivation = { id: 123 };
        spyOn(phoneActivationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ phoneActivation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(phoneActivationService.update).toHaveBeenCalledWith(phoneActivation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
