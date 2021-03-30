import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPhoneActivation, PhoneActivation } from '../phone-activation.model';
import { PhoneActivationService } from '../service/phone-activation.service';

@Component({
  selector: 'jhi-phone-activation-update',
  templateUrl: './phone-activation-update.component.html',
})
export class PhoneActivationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    isActivated: [],
    activationKey: [],
    expirationDate: [],
    deleted: [],
  });

  constructor(
    protected phoneActivationService: PhoneActivationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ phoneActivation }) => {
      this.updateForm(phoneActivation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const phoneActivation = this.createFromForm();
    if (phoneActivation.id !== undefined) {
      this.subscribeToSaveResponse(this.phoneActivationService.update(phoneActivation));
    } else {
      this.subscribeToSaveResponse(this.phoneActivationService.create(phoneActivation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhoneActivation>>): void {
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

  protected updateForm(phoneActivation: IPhoneActivation): void {
    this.editForm.patchValue({
      id: phoneActivation.id,
      isActivated: phoneActivation.isActivated,
      activationKey: phoneActivation.activationKey,
      expirationDate: phoneActivation.expirationDate,
      deleted: phoneActivation.deleted,
    });
  }

  protected createFromForm(): IPhoneActivation {
    return {
      ...new PhoneActivation(),
      id: this.editForm.get(['id'])!.value,
      isActivated: this.editForm.get(['isActivated'])!.value,
      activationKey: this.editForm.get(['activationKey'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
    };
  }
}
