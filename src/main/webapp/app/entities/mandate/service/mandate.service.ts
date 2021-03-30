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
import { IMandate, getMandateIdentifier } from '../mandate.model';

export type EntityResponseType = HttpResponse<IMandate>;
export type EntityArrayResponseType = HttpResponse<IMandate[]>;

@Injectable({ providedIn: 'root' })
export class MandateService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/mandates');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/mandates');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(mandate: IMandate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mandate);
    return this.http
      .post<IMandate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mandate: IMandate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mandate);
    return this.http
      .put<IMandate>(`${this.resourceUrl}/${getMandateIdentifier(mandate) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(mandate: IMandate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mandate);
    return this.http
      .patch<IMandate>(`${this.resourceUrl}/${getMandateIdentifier(mandate) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMandate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMandate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMandate[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addMandateToCollectionIfMissing(mandateCollection: IMandate[], ...mandatesToCheck: (IMandate | null | undefined)[]): IMandate[] {
    const mandates: IMandate[] = mandatesToCheck.filter(isPresent);
    if (mandates.length > 0) {
      const mandateCollectionIdentifiers = mandateCollection.map(mandateItem => getMandateIdentifier(mandateItem)!);
      const mandatesToAdd = mandates.filter(mandateItem => {
        const mandateIdentifier = getMandateIdentifier(mandateItem);
        if (mandateIdentifier == null || mandateCollectionIdentifiers.includes(mandateIdentifier)) {
          return false;
        }
        mandateCollectionIdentifiers.push(mandateIdentifier);
        return true;
      });
      return [...mandatesToAdd, ...mandateCollection];
    }
    return mandateCollection;
  }

  protected convertDateFromClient(mandate: IMandate): IMandate {
    return Object.assign({}, mandate, {
      date: mandate.date?.isValid() ? mandate.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((mandate: IMandate) => {
        mandate.date = mandate.date ? dayjs(mandate.date) : undefined;
      });
    }
    return res;
  }
}
