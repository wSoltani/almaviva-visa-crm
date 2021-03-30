import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMandate, Mandate } from '../mandate.model';
import { MandateService } from '../service/mandate.service';
import { IFolder } from 'app/entities/folder/folder.model';
import { FolderService } from 'app/entities/folder/service/folder.service';

@Component({
  selector: 'jhi-mandate-update',
  templateUrl: './mandate-update.component.html',
})
export class MandateUpdateComponent implements OnInit {
  isSaving = false;

  foldersSharedCollection: IFolder[] = [];

  editForm = this.fb.group({
    id: [],
    code: [],
    location: [],
    amount: [],
    date: [],
    isAVSPaiment: [],
    deleted: [],
    folder: [],
  });

  constructor(
    protected mandateService: MandateService,
    protected folderService: FolderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mandate }) => {
      this.updateForm(mandate);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mandate = this.createFromForm();
    if (mandate.id !== undefined) {
      this.subscribeToSaveResponse(this.mandateService.update(mandate));
    } else {
      this.subscribeToSaveResponse(this.mandateService.create(mandate));
    }
  }

  trackFolderById(index: number, item: IFolder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMandate>>): void {
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

  protected updateForm(mandate: IMandate): void {
    this.editForm.patchValue({
      id: mandate.id,
      code: mandate.code,
      location: mandate.location,
      amount: mandate.amount,
      date: mandate.date,
      isAVSPaiment: mandate.isAVSPaiment,
      deleted: mandate.deleted,
      folder: mandate.folder,
    });

    this.foldersSharedCollection = this.folderService.addFolderToCollectionIfMissing(this.foldersSharedCollection, mandate.folder);
  }

  protected loadRelationshipsOptions(): void {
    this.folderService
      .query()
      .pipe(map((res: HttpResponse<IFolder[]>) => res.body ?? []))
      .pipe(map((folders: IFolder[]) => this.folderService.addFolderToCollectionIfMissing(folders, this.editForm.get('folder')!.value)))
      .subscribe((folders: IFolder[]) => (this.foldersSharedCollection = folders));
  }

  protected createFromForm(): IMandate {
    return {
      ...new Mandate(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      location: this.editForm.get(['location'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      date: this.editForm.get(['date'])!.value,
      isAVSPaiment: this.editForm.get(['isAVSPaiment'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      folder: this.editForm.get(['folder'])!.value,
    };
  }
}
