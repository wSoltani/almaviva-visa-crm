import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IAVService, getAVServiceIdentifier } from '../av-service.model';

export type EntityResponseType = HttpResponse<IAVService>;
export type EntityArrayResponseType = HttpResponse<IAVService[]>;

@Injectable({ providedIn: 'root' })
export class AVServiceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/av-services');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/av-services');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(aVService: IAVService): Observable<EntityResponseType> {
    return this.http.post<IAVService>(this.resourceUrl, aVService, { observe: 'response' });
  }

  update(aVService: IAVService): Observable<EntityResponseType> {
    return this.http.put<IAVService>(`${this.resourceUrl}/${getAVServiceIdentifier(aVService) as number}`, aVService, {
      observe: 'response',
    });
  }

  partialUpdate(aVService: IAVService): Observable<EntityResponseType> {
    return this.http.patch<IAVService>(`${this.resourceUrl}/${getAVServiceIdentifier(aVService) as number}`, aVService, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAVService>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAVService[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAVService[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAVServiceToCollectionIfMissing(
    aVServiceCollection: IAVService[],
    ...aVServicesToCheck: (IAVService | null | undefined)[]
  ): IAVService[] {
    const aVServices: IAVService[] = aVServicesToCheck.filter(isPresent);
    if (aVServices.length > 0) {
      const aVServiceCollectionIdentifiers = aVServiceCollection.map(aVServiceItem => getAVServiceIdentifier(aVServiceItem)!);
      const aVServicesToAdd = aVServices.filter(aVServiceItem => {
        const aVServiceIdentifier = getAVServiceIdentifier(aVServiceItem);
        if (aVServiceIdentifier == null || aVServiceCollectionIdentifiers.includes(aVServiceIdentifier)) {
          return false;
        }
        aVServiceCollectionIdentifiers.push(aVServiceIdentifier);
        return true;
      });
      return [...aVServicesToAdd, ...aVServiceCollection];
    }
    return aVServiceCollection;
  }
}
