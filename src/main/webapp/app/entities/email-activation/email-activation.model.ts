import * as dayjs from 'dayjs';

export interface IEmailActivation {
  id?: number;
  isActivated?: boolean | null;
  activationKey?: string | null;
  expirationDate?: dayjs.Dayjs | null;
  deleted?: boolean | null;
}

export class EmailActivation implements IEmailActivation {
  constructor(
    public id?: number,
    public isActivated?: boolean | null,
    public activationKey?: string | null,
    public expirationDate?: dayjs.Dayjs | null,
    public deleted?: boolean | null
  ) {
    this.isActivated = this.isActivated ?? false;
    this.deleted = this.deleted ?? false;
  }
}

export function getEmailActivationIdentifier(emailActivation: IEmailActivation): number | undefined {
  return emailActivation.id;
}
