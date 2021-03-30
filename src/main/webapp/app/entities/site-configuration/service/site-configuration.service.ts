import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { ISiteConfiguration, getSiteConfigurationIdentifier } from '../site-configuration.model';

export type EntityResponseType = HttpResponse<ISiteConfiguration>;
export type EntityArrayResponseType = HttpResponse<ISiteConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class SiteConfigurationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/site-configurations');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/site-configurations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(siteConfiguration: ISiteConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(siteConfiguration);
    return this.http
      .post<ISiteConfiguration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(siteConfiguration: ISiteConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(siteConfiguration);
    return this.http
      .put<ISiteConfiguration>(`${this.resourceUrl}/${getSiteConfigurationIdentifier(siteConfiguration) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(siteConfiguration: ISiteConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(siteConfiguration);
    return this.http
      .patch<ISiteConfiguration>(`${this.resourceUrl}/${getSiteConfigurationIdentifier(siteConfiguration) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISiteConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISiteConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISiteConfiguration[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addSiteConfigurationToCollectionIfMissing(
    siteConfigurationCollection: ISiteConfiguration[],
    ...siteConfigurationsToCheck: (ISiteConfiguration | null | undefined)[]
  ): ISiteConfiguration[] {
    const siteConfigurations: ISiteConfiguration[] = siteConfigurationsToCheck.filter(isPresent);
    if (siteConfigurations.length > 0) {
      const siteConfigurationCollectionIdentifiers = siteConfigurationCollection.map(
        siteConfigurationItem => getSiteConfigurationIdentifier(siteConfigurationItem)!
      );
      const siteConfigurationsToAdd = siteConfigurations.filter(siteConfigurationItem => {
        const siteConfigurationIdentifier = getSiteConfigurationIdentifier(siteConfigurationItem);
        if (siteConfigurationIdentifier == null || siteConfigurationCollectionIdentifiers.includes(siteConfigurationIdentifier)) {
          return false;
        }
        siteConfigurationCollectionIdentifiers.push(siteConfigurationIdentifier);
        return true;
      });
      return [...siteConfigurationsToAdd, ...siteConfigurationCollection];
    }
    return siteConfigurationCollection;
  }

  protected convertDateFromClient(siteConfiguration: ISiteConfiguration): ISiteConfiguration {
    return Object.assign({}, siteConfiguration, {
      startDate: siteConfiguration.startDate?.isValid() ? siteConfiguration.startDate.toJSON() : undefined,
      endDate: siteConfiguration.endDate?.isValid() ? siteConfiguration.endDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((siteConfiguration: ISiteConfiguration) => {
        siteConfiguration.startDate = siteConfiguration.startDate ? dayjs(siteConfiguration.startDate) : undefined;
        siteConfiguration.endDate = siteConfiguration.endDate ? dayjs(siteConfiguration.endDate) : undefined;
      });
    }
    return res;
  }
}
