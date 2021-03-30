import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAppointment, Appointment } from '../appointment.model';
import { AppointmentService } from '../service/appointment.service';

@Component({
  selector: 'jhi-appointment-update',
  templateUrl: './appointment-update.component.html',
})
export class AppointmentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    date: [],
    time: [],
    deleted: [],
  });

  constructor(protected appointmentService: AppointmentService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointment }) => {
      this.updateForm(appointment);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointment = this.createFromForm();
    if (appointment.id !== undefined) {
      this.subscribeToSaveResponse(this.appointmentService.update(appointment));
    } else {
      this.subscribeToSaveResponse(this.appointmentService.create(appointment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointment>>): void {
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

  protected updateForm(appointment: IAppointment): void {
    this.editForm.patchValue({
      id: appointment.id,
      date: appointment.date,
      time: appointment.time,
      deleted: appointment.deleted,
    });
  }

  protected createFromForm(): IAppointment {
    return {
      ...new Appointment(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      time: this.editForm.get(['time'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
    };
  }
}
