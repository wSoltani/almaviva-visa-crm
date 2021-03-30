import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISiteConfiguration, SiteConfiguration } from '../site-configuration.model';
import { SiteConfigurationService } from '../service/site-configuration.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';

@Component({
  selector: 'jhi-site-configuration-update',
  templateUrl: './site-configuration-update.component.html',
})
export class SiteConfigurationUpdateComponent implements OnInit {
  isSaving = false;

  sitesSharedCollection: ISite[] = [];

  editForm = this.fb.group({
    id: [],
    startDate: [],
    endDate: [],
    openingTime: [],
    closingTime: [],
    appointmentTime: [],
    appointmentQuota: [],
    appointmentQuotaWeb: [],
    information: [],
    dailyMessage: [],
    prestationMargin: [],
    simultaneous: [],
    isSpecific: [],
    deleted: [],
    site: [],
  });

  constructor(
    protected siteConfigurationService: SiteConfigurationService,
    protected siteService: SiteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ siteConfiguration }) => {
      if (siteConfiguration.id === undefined) {
        const today = dayjs().startOf('day');
        siteConfiguration.startDate = today;
        siteConfiguration.endDate = today;
      }

      this.updateForm(siteConfiguration);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const siteConfiguration = this.createFromForm();
    if (siteConfiguration.id !== undefined) {
      this.subscribeToSaveResponse(this.siteConfigurationService.update(siteConfiguration));
    } else {
      this.subscribeToSaveResponse(this.siteConfigurationService.create(siteConfiguration));
    }
  }

  trackSiteById(index: number, item: ISite): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISiteConfiguration>>): void {
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

  protected updateForm(siteConfiguration: ISiteConfiguration): void {
    this.editForm.patchValue({
      id: siteConfiguration.id,
      startDate: siteConfiguration.startDate ? siteConfiguration.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: siteConfiguration.endDate ? siteConfiguration.endDate.format(DATE_TIME_FORMAT) : null,
      openingTime: siteConfiguration.openingTime,
      closingTime: siteConfiguration.closingTime,
      appointmentTime: siteConfiguration.appointmentTime,
      appointmentQuota: siteConfiguration.appointmentQuota,
      appointmentQuotaWeb: siteConfiguration.appointmentQuotaWeb,
      information: siteConfiguration.information,
      dailyMessage: siteConfiguration.dailyMessage,
      prestationMargin: siteConfiguration.prestationMargin,
      simultaneous: siteConfiguration.simultaneous,
      isSpecific: siteConfiguration.isSpecific,
      deleted: siteConfiguration.deleted,
      site: siteConfiguration.site,
    });

    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing(this.sitesSharedCollection, siteConfiguration.site);
  }

  protected loadRelationshipsOptions(): void {
    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing(sites, this.editForm.get('site')!.value)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }

  protected createFromForm(): ISiteConfiguration {
    return {
      ...new SiteConfiguration(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? dayjs(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      openingTime: this.editForm.get(['openingTime'])!.value,
      closingTime: this.editForm.get(['closingTime'])!.value,
      appointmentTime: this.editForm.get(['appointmentTime'])!.value,
      appointmentQuota: this.editForm.get(['appointmentQuota'])!.value,
      appointmentQuotaWeb: this.editForm.get(['appointmentQuotaWeb'])!.value,
      information: this.editForm.get(['information'])!.value,
      dailyMessage: this.editForm.get(['dailyMessage'])!.value,
      prestationMargin: this.editForm.get(['prestationMargin'])!.value,
      simultaneous: this.editForm.get(['simultaneous'])!.value,
      isSpecific: this.editForm.get(['isSpecific'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      site: this.editForm.get(['site'])!.value,
    };
  }
}
