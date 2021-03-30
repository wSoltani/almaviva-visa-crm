import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDayOff, DayOff } from '../day-off.model';
import { DayOffService } from '../service/day-off.service';
import { ISiteConfiguration } from 'app/entities/site-configuration/site-configuration.model';
import { SiteConfigurationService } from 'app/entities/site-configuration/service/site-configuration.service';

@Component({
  selector: 'jhi-day-off-update',
  templateUrl: './day-off-update.component.html',
})
export class DayOffUpdateComponent implements OnInit {
  isSaving = false;

  siteConfigurationsSharedCollection: ISiteConfiguration[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    description: [],
    date: [],
    isHoliday: [],
    deleted: [],
    siteConfiguration: [],
  });

  constructor(
    protected dayOffService: DayOffService,
    protected siteConfigurationService: SiteConfigurationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dayOff }) => {
      this.updateForm(dayOff);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dayOff = this.createFromForm();
    if (dayOff.id !== undefined) {
      this.subscribeToSaveResponse(this.dayOffService.update(dayOff));
    } else {
      this.subscribeToSaveResponse(this.dayOffService.create(dayOff));
    }
  }

  trackSiteConfigurationById(index: number, item: ISiteConfiguration): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDayOff>>): void {
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

  protected updateForm(dayOff: IDayOff): void {
    this.editForm.patchValue({
      id: dayOff.id,
      title: dayOff.title,
      description: dayOff.description,
      date: dayOff.date,
      isHoliday: dayOff.isHoliday,
      deleted: dayOff.deleted,
      siteConfiguration: dayOff.siteConfiguration,
    });

    this.siteConfigurationsSharedCollection = this.siteConfigurationService.addSiteConfigurationToCollectionIfMissing(
      this.siteConfigurationsSharedCollection,
      dayOff.siteConfiguration
    );
  }

  protected loadRelationshipsOptions(): void {
    this.siteConfigurationService
      .query()
      .pipe(map((res: HttpResponse<ISiteConfiguration[]>) => res.body ?? []))
      .pipe(
        map((siteConfigurations: ISiteConfiguration[]) =>
          this.siteConfigurationService.addSiteConfigurationToCollectionIfMissing(
            siteConfigurations,
            this.editForm.get('siteConfiguration')!.value
          )
        )
      )
      .subscribe((siteConfigurations: ISiteConfiguration[]) => (this.siteConfigurationsSharedCollection = siteConfigurations));
  }

  protected createFromForm(): IDayOff {
    return {
      ...new DayOff(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      date: this.editForm.get(['date'])!.value,
      isHoliday: this.editForm.get(['isHoliday'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      siteConfiguration: this.editForm.get(['siteConfiguration'])!.value,
    };
  }
}
