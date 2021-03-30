import { ISiteConfiguration } from 'app/entities/site-configuration/site-configuration.model';
import { IAVService } from 'app/entities/av-service/av-service.model';

export interface ISite {
  id?: number;
  name?: string | null;
  imgUrl?: string | null;
  address?: string | null;
  deleted?: boolean | null;
  siteConfigurations?: ISiteConfiguration[] | null;
  siteServices?: IAVService[] | null;
}

export class Site implements ISite {
  constructor(
    public id?: number,
    public name?: string | null,
    public imgUrl?: string | null,
    public address?: string | null,
    public deleted?: boolean | null,
    public siteConfigurations?: ISiteConfiguration[] | null,
    public siteServices?: IAVService[] | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getSiteIdentifier(site: ISite): number | undefined {
  return site.id;
}
