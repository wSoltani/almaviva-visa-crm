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
import { IPhoneActivation, getPhoneActivationIdentifier } from '../phone-activation.model';

export type EntityResponseType = HttpResponse<IPhoneActivation>;
export type EntityArrayResponseType = HttpResponse<IPhoneActivation[]>;

@Injectable({ providedIn: 'root' })
export class PhoneActivationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/phone-activations');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/phone-activations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(phoneActivation: IPhoneActivation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(phoneActivation);
    return this.http
      .post<IPhoneActivation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(phoneActivation: IPhoneActivation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(phoneActivation);
    return this.http
      .put<IPhoneActivation>(`${this.resourceUrl}/${getPhoneActivationIdentifier(phoneActivation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(phoneActivation: IPhoneActivation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(phoneActivation);
    return this.http
      .patch<IPhoneActivation>(`${this.resourceUrl}/${getPhoneActivationIdentifier(phoneActivation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPhoneActivation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPhoneActivation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPhoneActivation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPhoneActivationToCollectionIfMissing(
    phoneActivationCollection: IPhoneActivation[],
    ...phoneActivationsToCheck: (IPhoneActivation | null | undefined)[]
  ): IPhoneActivation[] {
    const phoneActivations: IPhoneActivation[] = phoneActivationsToCheck.filter(isPresent);
    if (phoneActivations.length > 0) {
      const phoneActivationCollectionIdentifiers = phoneActivationCollection.map(
        phoneActivationItem => getPhoneActivationIdentifier(phoneActivationItem)!
      );
      const phoneActivationsToAdd = phoneActivations.filter(phoneActivationItem => {
        const phoneActivationIdentifier = getPhoneActivationIdentifier(phoneActivationItem);
        if (phoneActivationIdentifier == null || phoneActivationCollectionIdentifiers.includes(phoneActivationIdentifier)) {
          return false;
        }
        phoneActivationCollectionIdentifiers.push(phoneActivationIdentifier);
        return true;
      });
      return [...phoneActivationsToAdd, ...phoneActivationCollection];
    }
    return phoneActivationCollection;
  }

  protected convertDateFromClient(phoneActivation: IPhoneActivation): IPhoneActivation {
    return Object.assign({}, phoneActivation, {
      expirationDate: phoneActivation.expirationDate?.isValid() ? phoneActivation.expirationDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((phoneActivation: IPhoneActivation) => {
        phoneActivation.expirationDate = phoneActivation.expirationDate ? dayjs(phoneActivation.expirationDate) : undefined;
      });
    }
    return res;
  }
}
