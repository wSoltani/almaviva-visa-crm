import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVisa, Visa } from '../visa.model';
import { VisaService } from '../service/visa.service';

@Component({
  selector: 'jhi-visa-update',
  templateUrl: './visa-update.component.html',
})
export class VisaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
    price: [],
    description: [],
    deleted: [],
  });

  constructor(protected visaService: VisaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visa }) => {
      this.updateForm(visa);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const visa = this.createFromForm();
    if (visa.id !== undefined) {
      this.subscribeToSaveResponse(this.visaService.update(visa));
    } else {
      this.subscribeToSaveResponse(this.visaService.create(visa));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisa>>): void {
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

  protected updateForm(visa: IVisa): void {
    this.editForm.patchValue({
      id: visa.id,
      title: visa.title,
      price: visa.price,
      description: visa.description,
      deleted: visa.deleted,
    });
  }

  protected createFromForm(): IVisa {
    return {
      ...new Visa(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      price: this.editForm.get(['price'])!.value,
      description: this.editForm.get(['description'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
    };
  }
}
