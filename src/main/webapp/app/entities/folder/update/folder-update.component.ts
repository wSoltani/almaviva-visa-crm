import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFolder, Folder } from '../folder.model';
import { FolderService } from '../service/folder.service';
import { IAppointment } from 'app/entities/appointment/appointment.model';
import { AppointmentService } from 'app/entities/appointment/service/appointment.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { IAVService } from 'app/entities/av-service/av-service.model';
import { AVServiceService } from 'app/entities/av-service/service/av-service.service';
import { IVisa } from 'app/entities/visa/visa.model';
import { VisaService } from 'app/entities/visa/service/visa.service';

@Component({
  selector: 'jhi-folder-update',
  templateUrl: './folder-update.component.html',
})
export class FolderUpdateComponent implements OnInit {
  isSaving = false;

  appointmentsCollection: IAppointment[] = [];
  sitesSharedCollection: ISite[] = [];
  aVServicesSharedCollection: IAVService[] = [];
  visasSharedCollection: IVisa[] = [];

  editForm = this.fb.group({
    id: [],
    folderId: [],
    status: [],
    paymentMethod: [],
    waitingRoom: [],
    serviceType: [],
    isAvsFree: [],
    deleted: [],
    appointment: [],
    site: [],
    services: [],
    visa: [],
  });

  constructor(
    protected folderService: FolderService,
    protected appointmentService: AppointmentService,
    protected siteService: SiteService,
    protected aVServiceService: AVServiceService,
    protected visaService: VisaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ folder }) => {
      this.updateForm(folder);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const folder = this.createFromForm();
    if (folder.id !== undefined) {
      this.subscribeToSaveResponse(this.folderService.update(folder));
    } else {
      this.subscribeToSaveResponse(this.folderService.create(folder));
    }
  }

  trackAppointmentById(index: number, item: IAppointment): number {
    return item.id!;
  }

  trackSiteById(index: number, item: ISite): number {
    return item.id!;
  }

  trackAVServiceById(index: number, item: IAVService): number {
    return item.id!;
  }

  trackVisaById(index: number, item: IVisa): number {
    return item.id!;
  }

  getSelectedAVService(option: IAVService, selectedVals?: IAVService[]): IAVService {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFolder>>): void {
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

  protected updateForm(folder: IFolder): void {
    this.editForm.patchValue({
      id: folder.id,
      folderId: folder.folderId,
      status: folder.status,
      paymentMethod: folder.paymentMethod,
      waitingRoom: folder.waitingRoom,
      serviceType: folder.serviceType,
      isAvsFree: folder.isAvsFree,
      deleted: folder.deleted,
      appointment: folder.appointment,
      site: folder.site,
      services: folder.services,
      visa: folder.visa,
    });

    this.appointmentsCollection = this.appointmentService.addAppointmentToCollectionIfMissing(
      this.appointmentsCollection,
      folder.appointment
    );
    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing(this.sitesSharedCollection, folder.site);
    this.aVServicesSharedCollection = this.aVServiceService.addAVServiceToCollectionIfMissing(
      this.aVServicesSharedCollection,
      ...(folder.services ?? [])
    );
    this.visasSharedCollection = this.visaService.addVisaToCollectionIfMissing(this.visasSharedCollection, folder.visa);
  }

  protected loadRelationshipsOptions(): void {
    this.appointmentService
      .query({ filter: 'folder-is-null' })
      .pipe(map((res: HttpResponse<IAppointment[]>) => res.body ?? []))
      .pipe(
        map((appointments: IAppointment[]) =>
          this.appointmentService.addAppointmentToCollectionIfMissing(appointments, this.editForm.get('appointment')!.value)
        )
      )
      .subscribe((appointments: IAppointment[]) => (this.appointmentsCollection = appointments));

    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(map((sites: ISite[]) => this.siteService.addSiteToCollectionIfMissing(sites, this.editForm.get('site')!.value)))
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));

    this.aVServiceService
      .query()
      .pipe(map((res: HttpResponse<IAVService[]>) => res.body ?? []))
      .pipe(
        map((aVServices: IAVService[]) =>
          this.aVServiceService.addAVServiceToCollectionIfMissing(aVServices, ...(this.editForm.get('services')!.value ?? []))
        )
      )
      .subscribe((aVServices: IAVService[]) => (this.aVServicesSharedCollection = aVServices));

    this.visaService
      .query()
      .pipe(map((res: HttpResponse<IVisa[]>) => res.body ?? []))
      .pipe(map((visas: IVisa[]) => this.visaService.addVisaToCollectionIfMissing(visas, this.editForm.get('visa')!.value)))
      .subscribe((visas: IVisa[]) => (this.visasSharedCollection = visas));
  }

  protected createFromForm(): IFolder {
    return {
      ...new Folder(),
      id: this.editForm.get(['id'])!.value,
      folderId: this.editForm.get(['folderId'])!.value,
      status: this.editForm.get(['status'])!.value,
      paymentMethod: this.editForm.get(['paymentMethod'])!.value,
      waitingRoom: this.editForm.get(['waitingRoom'])!.value,
      serviceType: this.editForm.get(['serviceType'])!.value,
      isAvsFree: this.editForm.get(['isAvsFree'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      appointment: this.editForm.get(['appointment'])!.value,
      site: this.editForm.get(['site'])!.value,
      services: this.editForm.get(['services'])!.value,
      visa: this.editForm.get(['visa'])!.value,
    };
  }
}
