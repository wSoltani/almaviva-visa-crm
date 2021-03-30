jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EmailActivationService } from '../service/email-activation.service';
import { IEmailActivation, EmailActivation } from '../email-activation.model';

import { EmailActivationUpdateComponent } from './email-activation-update.component';

describe('Component Tests', () => {
  describe('EmailActivation Management Update Component', () => {
    let comp: EmailActivationUpdateComponent;
    let fixture: ComponentFixture<EmailActivationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let emailActivationService: EmailActivationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EmailActivationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EmailActivationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EmailActivationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      emailActivationService = TestBed.inject(EmailActivationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const emailActivation: IEmailActivation = { id: 456 };

        activatedRoute.data = of({ emailActivation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(emailActivation));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const emailActivation = { id: 123 };
        spyOn(emailActivationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ emailActivation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: emailActivation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(emailActivationService.update).toHaveBeenCalledWith(emailActivation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const emailActivation = new EmailActivation();
        spyOn(emailActivationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ emailActivation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: emailActivation }));
        saveSubject.complete();

        // THEN
        expect(emailActivationService.create).toHaveBeenCalledWith(emailActivation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const emailActivation = { id: 123 };
        spyOn(emailActivationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ emailActivation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(emailActivationService.update).toHaveBeenCalledWith(emailActivation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
