import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClient, Client } from '../client.model';
import { ClientService } from '../service/client.service';
import { IPhoneActivation } from 'app/entities/phone-activation/phone-activation.model';
import { PhoneActivationService } from 'app/entities/phone-activation/service/phone-activation.service';
import { IEmailActivation } from 'app/entities/email-activation/email-activation.model';
import { EmailActivationService } from 'app/entities/email-activation/service/email-activation.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html',
})
export class ClientUpdateComponent implements OnInit {
  isSaving = false;

  phoneActivationsCollection: IPhoneActivation[] = [];
  emailActivationsCollection: IEmailActivation[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    phone: [],
    email: [],
    password: [],
    firstName: [],
    lastName: [],
    deleted: [],
    phoneActivation: [],
    emailActivation: [],
    internalUser: [],
  });

  constructor(
    protected clientService: ClientService,
    protected phoneActivationService: PhoneActivationService,
    protected emailActivationService: EmailActivationService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      this.updateForm(client);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const client = this.createFromForm();
    if (client.id !== undefined) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  trackPhoneActivationById(index: number, item: IPhoneActivation): number {
    return item.id!;
  }

  trackEmailActivationById(index: number, item: IEmailActivation): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(client: IClient): void {
    this.editForm.patchValue({
      id: client.id,
      phone: client.phone,
      email: client.email,
      password: client.password,
      firstName: client.firstName,
      lastName: client.lastName,
      deleted: client.deleted,
      phoneActivation: client.phoneActivation,
      emailActivation: client.emailActivation,
      internalUser: client.internalUser,
    });

    this.phoneActivationsCollection = this.phoneActivationService.addPhoneActivationToCollectionIfMissing(
      this.phoneActivationsCollection,
      client.phoneActivation
    );
    this.emailActivationsCollection = this.emailActivationService.addEmailActivationToCollectionIfMissing(
      this.emailActivationsCollection,
      client.emailActivation
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, client.internalUser);
  }

  protected loadRelationshipsOptions(): void {
    this.phoneActivationService
      .query({ filter: 'client-is-null' })
      .pipe(map((res: HttpResponse<IPhoneActivation[]>) => res.body ?? []))
      .pipe(
        map((phoneActivations: IPhoneActivation[]) =>
          this.phoneActivationService.addPhoneActivationToCollectionIfMissing(phoneActivations, this.editForm.get('phoneActivation')!.value)
        )
      )
      .subscribe((phoneActivations: IPhoneActivation[]) => (this.phoneActivationsCollection = phoneActivations));

    this.emailActivationService
      .query({ filter: 'client-is-null' })
      .pipe(map((res: HttpResponse<IEmailActivation[]>) => res.body ?? []))
      .pipe(
        map((emailActivations: IEmailActivation[]) =>
          this.emailActivationService.addEmailActivationToCollectionIfMissing(emailActivations, this.editForm.get('emailActivation')!.value)
        )
      )
      .subscribe((emailActivations: IEmailActivation[]) => (this.emailActivationsCollection = emailActivations));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('internalUser')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IClient {
    return {
      ...new Client(),
      id: this.editForm.get(['id'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      email: this.editForm.get(['email'])!.value,
      password: this.editForm.get(['password'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      phoneActivation: this.editForm.get(['phoneActivation'])!.value,
      emailActivation: this.editForm.get(['emailActivation'])!.value,
      internalUser: this.editForm.get(['internalUser'])!.value,
    };
  }
}
