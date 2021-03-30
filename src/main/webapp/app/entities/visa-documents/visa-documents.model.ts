import { IVisa } from 'app/entities/visa/visa.model';

export interface IVisaDocuments {
  id?: number;
  title?: string | null;
  description?: string | null;
  deleted?: boolean | null;
  visas?: IVisa[] | null;
}

export class VisaDocuments implements IVisaDocuments {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
    public deleted?: boolean | null,
    public visas?: IVisa[] | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getVisaDocumentsIdentifier(visaDocuments: IVisaDocuments): number | undefined {
  return visaDocuments.id;
}
