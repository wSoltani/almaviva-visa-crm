import * as dayjs from 'dayjs';

export interface IAppointment {
  id?: number;
  date?: dayjs.Dayjs | null;
  time?: string | null;
  deleted?: boolean | null;
}

export class Appointment implements IAppointment {
  constructor(public id?: number, public date?: dayjs.Dayjs | null, public time?: string | null, public deleted?: boolean | null) {
    this.deleted = this.deleted ?? false;
  }
}

export function getAppointmentIdentifier(appointment: IAppointment): number | undefined {
  return appointment.id;
}
