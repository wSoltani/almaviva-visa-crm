import { IFolder } from 'app/entities/folder/folder.model';
import { IVisaDocuments } from 'app/entities/visa-documents/visa-documents.model';

export interface IVisa {
  id?: number;
  title?: string | null;
  price?: number | null;
  description?: string | null;
  deleted?: boolean | null;
  folders?: IFolder[] | null;
  documents?: IVisaDocuments[] | null;
}

export class Visa implements IVisa {
  constructor(
    public id?: number,
    public title?: string | null,
    public price?: number | null,
    public description?: string | null,
    public deleted?: boolean | null,
    public folders?: IFolder[] | null,
    public documents?: IVisaDocuments[] | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getVisaIdentifier(visa: IVisa): number | undefined {
  return visa.id;
}
