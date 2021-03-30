import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IEmailActivation, getEmailActivationIdentifier } from '../email-activation.model';

export type EntityResponseType = HttpResponse<IEmailActivation>;
export type EntityArrayResponseType = HttpResponse<IEmailActivation[]>;

@Injectable({ providedIn: 'root' })
export class EmailActivationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/email-activations');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/email-activations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(emailActivation: IEmailActivation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailActivation);
    return this.http
      .post<IEmailActivation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(emailActivation: IEmailActivation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailActivation);
    return this.http
      .put<IEmailActivation>(`${this.resourceUrl}/${getEmailActivationIdentifier(emailActivation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(emailActivation: IEmailActivation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(emailActivation);
    return this.http
      .patch<IEmailActivation>(`${this.resourceUrl}/${getEmailActivationIdentifier(emailActivation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmailActivation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmailActivation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmailActivation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addEmailActivationToCollectionIfMissing(
    emailActivationCollection: IEmailActivation[],
    ...emailActivationsToCheck: (IEmailActivation | null | undefined)[]
  ): IEmailActivation[] {
    const emailActivations: IEmailActivation[] = emailActivationsToCheck.filter(isPresent);
    if (emailActivations.length > 0) {
      const emailActivationCollectionIdentifiers = emailActivationCollection.map(
        emailActivationItem => getEmailActivationIdentifier(emailActivationItem)!
      );
      const emailActivationsToAdd = emailActivations.filter(emailActivationItem => {
        const emailActivationIdentifier = getEmailActivationIdentifier(emailActivationItem);
        if (emailActivationIdentifier == null || emailActivationCollectionIdentifiers.includes(emailActivationIdentifier)) {
          return false;
        }
        emailActivationCollectionIdentifiers.push(emailActivationIdentifier);
        return true;
      });
      return [...emailActivationsToAdd, ...emailActivationCollection];
    }
    return emailActivationCollection;
  }

  protected convertDateFromClient(emailActivation: IEmailActivation): IEmailActivation {
    return Object.assign({}, emailActivation, {
      expirationDate: emailActivation.expirationDate?.isValid() ? emailActivation.expirationDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expirationDate = res.body.expirationDate ? dayjs(res.body.expirationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((emailActivation: IEmailActivation) => {
        emailActivation.expirationDate = emailActivation.expirationDate ? dayjs(emailActivation.expirationDate) : undefined;
      });
    }
    return res;
  }
}
