jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClientService } from '../service/client.service';
import { IClient, Client } from '../client.model';
import { IPhoneActivation } from 'app/entities/phone-activation/phone-activation.model';
import { PhoneActivationService } from 'app/entities/phone-activation/service/phone-activation.service';
import { IEmailActivation } from 'app/entities/email-activation/email-activation.model';
import { EmailActivationService } from 'app/entities/email-activation/service/email-activation.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ClientUpdateComponent } from './client-update.component';

describe('Component Tests', () => {
  describe('Client Management Update Component', () => {
    let comp: ClientUpdateComponent;
    let fixture: ComponentFixture<ClientUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let clientService: ClientService;
    let phoneActivationService: PhoneActivationService;
    let emailActivationService: EmailActivationService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClientUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClientUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClientUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      clientService = TestBed.inject(ClientService);
      phoneActivationService = TestBed.inject(PhoneActivationService);
      emailActivationService = TestBed.inject(EmailActivationService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call phoneActivation query and add missing value', () => {
        const client: IClient = { id: 456 };
        const phoneActivation: IPhoneActivation = { id: 47270 };
        client.phoneActivation = phoneActivation;

        const phoneActivationCollection: IPhoneActivation[] = [{ id: 76676 }];
        spyOn(phoneActivationService, 'query').and.returnValue(of(new HttpResponse({ body: phoneActivationCollection })));
        const expectedCollection: IPhoneActivation[] = [phoneActivation, ...phoneActivationCollection];
        spyOn(phoneActivationService, 'addPhoneActivationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(phoneActivationService.query).toHaveBeenCalled();
        expect(phoneActivationService.addPhoneActivationToCollectionIfMissing).toHaveBeenCalledWith(
          phoneActivationCollection,
          phoneActivation
        );
        expect(comp.phoneActivationsCollection).toEqual(expectedCollection);
      });

      it('Should call emailActivation query and add missing value', () => {
        const client: IClient = { id: 456 };
        const emailActivation: IEmailActivation = { id: 68766 };
        client.emailActivation = emailActivation;

        const emailActivationCollection: IEmailActivation[] = [{ id: 13728 }];
        spyOn(emailActivationService, 'query').and.returnValue(of(new HttpResponse({ body: emailActivationCollection })));
        const expectedCollection: IEmailActivation[] = [emailActivation, ...emailActivationCollection];
        spyOn(emailActivationService, 'addEmailActivationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(emailActivationService.query).toHaveBeenCalled();
        expect(emailActivationService.addEmailActivationToCollectionIfMissing).toHaveBeenCalledWith(
          emailActivationCollection,
          emailActivation
        );
        expect(comp.emailActivationsCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const client: IClient = { id: 456 };
        const internalUser: IUser = { id: 83931 };
        client.internalUser = internalUser;

        const userCollection: IUser[] = [{ id: 30839 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [internalUser];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const client: IClient = { id: 456 };
        const phoneActivation: IPhoneActivation = { id: 8567 };
        client.phoneActivation = phoneActivation;
        const emailActivation: IEmailActivation = { id: 65355 };
        client.emailActivation = emailActivation;
        const internalUser: IUser = { id: 77851 };
        client.internalUser = internalUser;

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(client));
        expect(comp.phoneActivationsCollection).toContain(phoneActivation);
        expect(comp.emailActivationsCollection).toContain(emailActivation);
        expect(comp.usersSharedCollection).toContain(internalUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const client = { id: 123 };
        spyOn(clientService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: client }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(clientService.update).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const client = new Client();
        spyOn(clientService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: client }));
        saveSubject.complete();

        // THEN
        expect(clientService.create).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const client = { id: 123 };
        spyOn(clientService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(clientService.update).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPhoneActivationById', () => {
        it('Should return tracked PhoneActivation primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPhoneActivationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEmailActivationById', () => {
        it('Should return tracked EmailActivation primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEmailActivationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
