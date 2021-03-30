import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVisaDocuments, VisaDocuments } from '../visa-documents.model';
import { VisaDocumentsService } from '../service/visa-documents.service';
import { IVisa } from 'app/entities/visa/visa.model';
import { VisaService } from 'app/entities/visa/service/visa.service';

@Component({
  selector: 'jhi-visa-documents-update',
  templateUrl: './visa-documents-update.component.html',
})
export class VisaDocumentsUpdateComponent implements OnInit {
  isSaving = false;

  visasSharedCollection: IVisa[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    description: [],
    deleted: [],
    visas: [],
  });

  constructor(
    protected visaDocumentsService: VisaDocumentsService,
    protected visaService: VisaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visaDocuments }) => {
      this.updateForm(visaDocuments);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const visaDocuments = this.createFromForm();
    if (visaDocuments.id !== undefined) {
      this.subscribeToSaveResponse(this.visaDocumentsService.update(visaDocuments));
    } else {
      this.subscribeToSaveResponse(this.visaDocumentsService.create(visaDocuments));
    }
  }

  trackVisaById(index: number, item: IVisa): number {
    return item.id!;
  }

  getSelectedVisa(option: IVisa, selectedVals?: IVisa[]): IVisa {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisaDocuments>>): void {
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

  protected updateForm(visaDocuments: IVisaDocuments): void {
    this.editForm.patchValue({
      id: visaDocuments.id,
      title: visaDocuments.title,
      description: visaDocuments.description,
      deleted: visaDocuments.deleted,
      visas: visaDocuments.visas,
    });

    this.visasSharedCollection = this.visaService.addVisaToCollectionIfMissing(this.visasSharedCollection, ...(visaDocuments.visas ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.visaService
      .query()
      .pipe(map((res: HttpResponse<IVisa[]>) => res.body ?? []))
      .pipe(map((visas: IVisa[]) => this.visaService.addVisaToCollectionIfMissing(visas, ...(this.editForm.get('visas')!.value ?? []))))
      .subscribe((visas: IVisa[]) => (this.visasSharedCollection = visas));
  }

  protected createFromForm(): IVisaDocuments {
    return {
      ...new VisaDocuments(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      visas: this.editForm.get(['visas'])!.value,
    };
  }
}
