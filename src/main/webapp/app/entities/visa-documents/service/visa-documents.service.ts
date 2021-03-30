import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IVisaDocuments, getVisaDocumentsIdentifier } from '../visa-documents.model';

export type EntityResponseType = HttpResponse<IVisaDocuments>;
export type EntityArrayResponseType = HttpResponse<IVisaDocuments[]>;

@Injectable({ providedIn: 'root' })
export class VisaDocumentsService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/visa-documents');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/visa-documents');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(visaDocuments: IVisaDocuments): Observable<EntityResponseType> {
    return this.http.post<IVisaDocuments>(this.resourceUrl, visaDocuments, { observe: 'response' });
  }

  update(visaDocuments: IVisaDocuments): Observable<EntityResponseType> {
    return this.http.put<IVisaDocuments>(`${this.resourceUrl}/${getVisaDocumentsIdentifier(visaDocuments) as number}`, visaDocuments, {
      observe: 'response',
    });
  }

  partialUpdate(visaDocuments: IVisaDocuments): Observable<EntityResponseType> {
    return this.http.patch<IVisaDocuments>(`${this.resourceUrl}/${getVisaDocumentsIdentifier(visaDocuments) as number}`, visaDocuments, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVisaDocuments>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVisaDocuments[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVisaDocuments[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addVisaDocumentsToCollectionIfMissing(
    visaDocumentsCollection: IVisaDocuments[],
    ...visaDocumentsToCheck: (IVisaDocuments | null | undefined)[]
  ): IVisaDocuments[] {
    const visaDocuments: IVisaDocuments[] = visaDocumentsToCheck.filter(isPresent);
    if (visaDocuments.length > 0) {
      const visaDocumentsCollectionIdentifiers = visaDocumentsCollection.map(
        visaDocumentsItem => getVisaDocumentsIdentifier(visaDocumentsItem)!
      );
      const visaDocumentsToAdd = visaDocuments.filter(visaDocumentsItem => {
        const visaDocumentsIdentifier = getVisaDocumentsIdentifier(visaDocumentsItem);
        if (visaDocumentsIdentifier == null || visaDocumentsCollectionIdentifiers.includes(visaDocumentsIdentifier)) {
          return false;
        }
        visaDocumentsCollectionIdentifiers.push(visaDocumentsIdentifier);
        return true;
      });
      return [...visaDocumentsToAdd, ...visaDocumentsCollection];
    }
    return visaDocumentsCollection;
  }
}
