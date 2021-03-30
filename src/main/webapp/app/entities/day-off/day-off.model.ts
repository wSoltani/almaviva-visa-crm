import * as dayjs from 'dayjs';
import { ISiteConfiguration } from 'app/entities/site-configuration/site-configuration.model';

export interface IDayOff {
  id?: number;
  title?: string | null;
  description?: string | null;
  date?: dayjs.Dayjs | null;
  isHoliday?: boolean | null;
  deleted?: boolean | null;
  siteConfiguration?: ISiteConfiguration | null;
}

export class DayOff implements IDayOff {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
    public date?: dayjs.Dayjs | null,
    public isHoliday?: boolean | null,
    public deleted?: boolean | null,
    public siteConfiguration?: ISiteConfiguration | null
  ) {
    this.isHoliday = this.isHoliday ?? false;
    this.deleted = this.deleted ?? false;
  }
}

export function getDayOffIdentifier(dayOff: IDayOff): number | undefined {
  return dayOff.id;
}
