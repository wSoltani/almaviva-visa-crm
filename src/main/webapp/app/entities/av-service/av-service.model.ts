import { ISite } from 'app/entities/site/site.model';
import { IFolder } from 'app/entities/folder/folder.model';

export interface IAVService {
  id?: number;
  title?: string | null;
  description?: string | null;
  price?: number | null;
  quantity?: number | null;
  isPrincipal?: boolean | null;
  deleted?: boolean | null;
  site?: ISite | null;
  folders?: IFolder[] | null;
}

export class AVService implements IAVService {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
    public price?: number | null,
    public quantity?: number | null,
    public isPrincipal?: boolean | null,
    public deleted?: boolean | null,
    public site?: ISite | null,
    public folders?: IFolder[] | null
  ) {
    this.isPrincipal = this.isPrincipal ?? false;
    this.deleted = this.deleted ?? false;
  }
}

export function getAVServiceIdentifier(aVService: IAVService): number | undefined {
  return aVService.id;
}
