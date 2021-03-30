import { IPhoneActivation } from 'app/entities/phone-activation/phone-activation.model';
import { IEmailActivation } from 'app/entities/email-activation/email-activation.model';
import { IUser } from 'app/entities/user/user.model';

export interface IClient {
  id?: number;
  phone?: string | null;
  email?: string | null;
  password?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  deleted?: boolean | null;
  phoneActivation?: IPhoneActivation | null;
  emailActivation?: IEmailActivation | null;
  internalUser?: IUser | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public phone?: string | null,
    public email?: string | null,
    public password?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public deleted?: boolean | null,
    public phoneActivation?: IPhoneActivation | null,
    public emailActivation?: IEmailActivation | null,
    public internalUser?: IUser | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
