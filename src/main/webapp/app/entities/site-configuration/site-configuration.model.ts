import * as dayjs from 'dayjs';
import { ISite } from 'app/entities/site/site.model';

export interface ISiteConfiguration {
  id?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  openingTime?: string | null;
  closingTime?: string | null;
  appointmentTime?: number | null;
  appointmentQuota?: number | null;
  appointmentQuotaWeb?: number | null;
  information?: string | null;
  dailyMessage?: string | null;
  prestationMargin?: number | null;
  simultaneous?: number | null;
  isSpecific?: boolean | null;
  deleted?: boolean | null;
  site?: ISite | null;
}

export class SiteConfiguration implements ISiteConfiguration {
  constructor(
    public id?: number,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public openingTime?: string | null,
    public closingTime?: string | null,
    public appointmentTime?: number | null,
    public appointmentQuota?: number | null,
    public appointmentQuotaWeb?: number | null,
    public information?: string | null,
    public dailyMessage?: string | null,
    public prestationMargin?: number | null,
    public simultaneous?: number | null,
    public isSpecific?: boolean | null,
    public deleted?: boolean | null,
    public site?: ISite | null
  ) {
    this.isSpecific = this.isSpecific ?? false;
    this.deleted = this.deleted ?? false;
  }
}

export function getSiteConfigurationIdentifier(siteConfiguration: ISiteConfiguration): number | undefined {
  return siteConfiguration.id;
}
