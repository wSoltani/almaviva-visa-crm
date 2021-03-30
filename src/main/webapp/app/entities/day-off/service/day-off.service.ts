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
import { IDayOff, getDayOffIdentifier } from '../day-off.model';

export type EntityResponseType = HttpResponse<IDayOff>;
export type EntityArrayResponseType = HttpResponse<IDayOff[]>;

@Injectable({ providedIn: 'root' })
export class DayOffService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/day-offs');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/day-offs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(dayOff: IDayOff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dayOff);
    return this.http
      .post<IDayOff>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dayOff: IDayOff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dayOff);
    return this.http
      .put<IDayOff>(`${this.resourceUrl}/${getDayOffIdentifier(dayOff) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dayOff: IDayOff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dayOff);
    return this.http
      .patch<IDayOff>(`${this.resourceUrl}/${getDayOffIdentifier(dayOff) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDayOff>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDayOff[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDayOff[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addDayOffToCollectionIfMissing(dayOffCollection: IDayOff[], ...dayOffsToCheck: (IDayOff | null | undefined)[]): IDayOff[] {
    const dayOffs: IDayOff[] = dayOffsToCheck.filter(isPresent);
    if (dayOffs.length > 0) {
      const dayOffCollectionIdentifiers = dayOffCollection.map(dayOffItem => getDayOffIdentifier(dayOffItem)!);
      const dayOffsToAdd = dayOffs.filter(dayOffItem => {
        const dayOffIdentifier = getDayOffIdentifier(dayOffItem);
        if (dayOffIdentifier == null || dayOffCollectionIdentifiers.includes(dayOffIdentifier)) {
          return false;
        }
        dayOffCollectionIdentifiers.push(dayOffIdentifier);
        return true;
      });
      return [...dayOffsToAdd, ...dayOffCollection];
    }
    return dayOffCollection;
  }

  protected convertDateFromClient(dayOff: IDayOff): IDayOff {
    return Object.assign({}, dayOff, {
      date: dayOff.date?.isValid() ? dayOff.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((dayOff: IDayOff) => {
        dayOff.date = dayOff.date ? dayjs(dayOff.date) : undefined;
      });
    }
    return res;
  }
}
