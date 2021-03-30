import * as dayjs from 'dayjs';
import { IFolder } from 'app/entities/folder/folder.model';

export interface IMandate {
  id?: number;
  code?: number | null;
  location?: string | null;
  amount?: number | null;
  date?: dayjs.Dayjs | null;
  isAVSPaiment?: boolean | null;
  deleted?: boolean | null;
  folder?: IFolder | null;
}

export class Mandate implements IMandate {
  constructor(
    public id?: number,
    public code?: number | null,
    public location?: string | null,
    public amount?: number | null,
    public date?: dayjs.Dayjs | null,
    public isAVSPaiment?: boolean | null,
    public deleted?: boolean | null,
    public folder?: IFolder | null
  ) {
    this.isAVSPaiment = this.isAVSPaiment ?? false;
    this.deleted = this.deleted ?? false;
  }
}

export function getMandateIdentifier(mandate: IMandate): number | undefined {
  return mandate.id;
}
