import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAVService, AVService } from '../av-service.model';
import { AVServiceService } from '../service/av-service.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

@Component({
  selector: 'jhi-av-service-update',
  templateUrl: './av-service-update.component.html',
})
export class AVServiceUpdateComponent implements OnInit {
  isSaving = false;

  sitesSharedCollection: ISite[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    description: [],
    price: [],
    quantity: [],
    isPrincipal: [],
    deleted: [],
    site: [],
  });

  constructor(
    protected aVServiceService: AVServiceService,
    protected siteService: SiteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aVService }) => {
      this.updateForm(aVService);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aVService = this.createFromForm();
    if (aVService.id !== undefined) {
      this.subscribeToSaveResponse(this.aVServiceService.update(aVService));
    } else {
      this.subscribeToSaveResponse(this.aVServiceService.create(aVService));
    }
  }

  trackSiteById(index: number, item: ISite): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAVService>>): void {
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

  protected updateForm(aVService: IAVService): void {
    this.editForm.patchValue({
      id: aVService.id,
      title: aVService.title,
      description: aVService.description,
      price: aVService.price,
      quantity: aVService.quantity,
      isPrincipal: aVService.isPrincipal,
      deleted: aVService.deleted,
      site: aVService.site,
    });

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing(this.sitesSharedCollection, aVService.site);
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing(sites, this.editForm.get('site')!.value)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }

  protected createFromForm(): IAVService {
    return {
      ...new AVService(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      price: this.editForm.get(['price'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      isPrincipal: this.editForm.get(['isPrincipal'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      site: this.editForm.get(['site'])!.value,
    };
  }
}
