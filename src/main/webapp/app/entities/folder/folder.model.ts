import { IAppointment } from 'app/entities/appointment/appointment.model';
import { ISite } from 'app/entities/site/site.model';
import { IAVService } from 'app/entities/av-service/av-service.model';
import { IVisa } from 'app/entities/visa/visa.model';

export interface IFolder {
  id?: number;
  folderId?: number | null;
  status?: string | null;
  paymentMethod?: string | null;
  waitingRoom?: string | null;
  serviceType?: string | null;
  isAvsFree?: boolean | null;
  deleted?: boolean | null;
  appointment?: IAppointment | null;
  site?: ISite | null;
  services?: IAVService[] | null;
  visa?: IVisa | null;
}

export class Folder implements IFolder {
  constructor(
    public id?: number,
    public folderId?: number | null,
    public status?: string | null,
    public paymentMethod?: string | null,
    public waitingRoom?: string | null,
    public serviceType?: string | null,
    public isAvsFree?: boolean | null,
    public deleted?: boolean | null,
    public appointment?: IAppointment | null,
    public site?: ISite | null,
    public services?: IAVService[] | null,
    public visa?: IVisa | null
  ) {
    this.isAvsFree = this.isAvsFree ?? false;
    this.deleted = this.deleted ?? false;
  }
}

export function getFolderIdentifier(folder: IFolder): number | undefined {
  return folder.id;
}
