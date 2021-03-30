import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEmailActivation, EmailActivation } from '../email-activation.model';
import { EmailActivationService } from '../service/email-activation.service';

@Component({
  selector: 'jhi-email-activation-update',
  templateUrl: './email-activation-update.component.html',
})
export class EmailActivationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    isActivated: [],
    activationKey: [],
    expirationDate: [],
    deleted: [],
  });

  constructor(
    protected emailActivationService: EmailActivationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailActivation }) => {
      this.updateForm(emailActivation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const emailActivation = this.createFromForm();
    if (emailActivation.id !== undefined) {
      this.subscribeToSaveResponse(this.emailActivationService.update(emailActivation));
    } else {
      this.subscribeToSaveResponse(this.emailActivationService.create(emailActivation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmailActivation>>): void {
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

  protected updateForm(emailActivation: IEmailActivation): void {
    this.editForm.patchValue({
      id: emailActivation.id,
      isActivated: emailActivation.isActivated,
      activationKey: emailActivation.activationKey,
      expirationDate: emailActivation.expirationDate,
      deleted: emailActivation.deleted,
    });
  }

  protected createFromForm(): IEmailActivation {
    return {
      ...new EmailActivation(),
      id: this.editForm.get(['id'])!.value,
      isActivated: this.editForm.get(['isActivated'])!.value,
      activationKey: this.editForm.get(['activationKey'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
    };
  }
}
