import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IVisa, getVisaIdentifier } from '../visa.model';

export type EntityResponseType = HttpResponse<IVisa>;
export type EntityArrayResponseType = HttpResponse<IVisa[]>;

@Injectable({ providedIn: 'root' })
export class VisaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/visas');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/visas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(visa: IVisa): Observable<EntityResponseType> {
    return this.http.post<IVisa>(this.resourceUrl, visa, { observe: 'response' });
  }

  update(visa: IVisa): Observable<EntityResponseType> {
    return this.http.put<IVisa>(`${this.resourceUrl}/${getVisaIdentifier(visa) as number}`, visa, { observe: 'response' });
  }

  partialUpdate(visa: IVisa): Observable<EntityResponseType> {
    return this.http.patch<IVisa>(`${this.resourceUrl}/${getVisaIdentifier(visa) as number}`, visa, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVisa>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVisa[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVisa[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addVisaToCollectionIfMissing(visaCollection: IVisa[], ...visasToCheck: (IVisa | null | undefined)[]): IVisa[] {
    const visas: IVisa[] = visasToCheck.filter(isPresent);
    if (visas.length > 0) {
      const visaCollectionIdentifiers = visaCollection.map(visaItem => getVisaIdentifier(visaItem)!);
      const visasToAdd = visas.filter(visaItem => {
        const visaIdentifier = getVisaIdentifier(visaItem);
        if (visaIdentifier == null || visaCollectionIdentifiers.includes(visaIdentifier)) {
          return false;
        }
        visaCollectionIdentifiers.push(visaIdentifier);
        return true;
      });
      return [...visasToAdd, ...visaCollection];
    }
    return visaCollection;
  }
}
