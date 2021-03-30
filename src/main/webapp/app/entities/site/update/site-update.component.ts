import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISite, Site } from '../site.model';
import { SiteService } from '../service/site.service';

@Component({
  selector: 'jhi-site-update',
  templateUrl: './site-update.component.html',
})
export class SiteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    imgUrl: [],
    address: [],
    deleted: [],
  });

  constructor(protected siteService: SiteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ site }) => {
      this.updateForm(site);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const site = this.createFromForm();
    if (site.id !== undefined) {
      this.subscribeToSaveResponse(this.siteService.update(site));
    } else {
      this.subscribeToSaveResponse(this.siteService.create(site));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISite>>): void {
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

  protected updateForm(site: ISite): void {
    this.editForm.patchValue({
      id: site.id,
      name: site.name,
      imgUrl: site.imgUrl,
      address: site.address,
      deleted: site.deleted,
    });
  }

  protected createFromForm(): ISite {
    return {
      ...new Site(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      imgUrl: this.editForm.get(['imgUrl'])!.value,
      address: this.editForm.get(['address'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
    };
  }
}
