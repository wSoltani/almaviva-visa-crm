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
import { IPerson, getPersonIdentifier } from '../person.model';

export type EntityResponseType = HttpResponse<IPerson>;
export type EntityArrayResponseType = HttpResponse<IPerson[]>;

@Injectable({ providedIn: 'root' })
export class PersonService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/people');
  public resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/people');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(person: IPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(person);
    return this.http
      .post<IPerson>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(person: IPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(person);
    return this.http
      .put<IPerson>(`${this.resourceUrl}/${getPersonIdentifier(person) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(person: IPerson): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(person);
    return this.http
      .patch<IPerson>(`${this.resourceUrl}/${getPersonIdentifier(person) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPerson>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPerson[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPerson[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPersonToCollectionIfMissing(personCollection: IPerson[], ...peopleToCheck: (IPerson | null | undefined)[]): IPerson[] {
    const people: IPerson[] = peopleToCheck.filter(isPresent);
    if (people.length > 0) {
      const personCollectionIdentifiers = personCollection.map(personItem => getPersonIdentifier(personItem)!);
      const peopleToAdd = people.filter(personItem => {
        const personIdentifier = getPersonIdentifier(personItem);
        if (personIdentifier == null || personCollectionIdentifiers.includes(personIdentifier)) {
          return false;
        }
        personCollectionIdentifiers.push(personIdentifier);
        return true;
      });
      return [...peopleToAdd, ...personCollection];
    }
    return personCollection;
  }

  protected convertDateFromClient(person: IPerson): IPerson {
    return Object.assign({}, person, {
      dateDelivDoc: person.dateDelivDoc?.isValid() ? person.dateDelivDoc.format(DATE_FORMAT) : undefined,
      dateExpDoc: person.dateExpDoc?.isValid() ? person.dateExpDoc.format(DATE_FORMAT) : undefined,
      dateExpSejour: person.dateExpSejour?.isValid() ? person.dateExpSejour.format(DATE_FORMAT) : undefined,
      dateDelivDebut: person.dateDelivDebut?.isValid() ? person.dateDelivDebut.format(DATE_FORMAT) : undefined,
      dateDelivFin: person.dateDelivFin?.isValid() ? person.dateDelivFin.format(DATE_FORMAT) : undefined,
      dateEmpreint: person.dateEmpreint?.isValid() ? person.dateEmpreint.format(DATE_FORMAT) : undefined,
      dateDelivAutor: person.dateDelivAutor?.isValid() ? person.dateDelivAutor.format(DATE_FORMAT) : undefined,
      dateValideAtorDebut: person.dateValideAtorDebut?.isValid() ? person.dateValideAtorDebut.format(DATE_FORMAT) : undefined,
      dateValideAutorFin: person.dateValideAutorFin?.isValid() ? person.dateValideAutorFin.format(DATE_FORMAT) : undefined,
      dateArrivPrevu: person.dateArrivPrevu?.isValid() ? person.dateArrivPrevu.format(DATE_FORMAT) : undefined,
      dateDepartPrevu: person.dateDepartPrevu?.isValid() ? person.dateDepartPrevu.format(DATE_FORMAT) : undefined,
      dateNaissCit: person.dateNaissCit?.isValid() ? person.dateNaissCit.format(DATE_FORMAT) : undefined,
      dateForm: person.dateForm?.isValid() ? person.dateForm.format(DATE_FORMAT) : undefined,
      dateaut: person.dateaut?.isValid() ? person.dateaut.format(DATE_FORMAT) : undefined,
      datecreatefo: person.datecreatefo?.isValid() ? person.datecreatefo.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDelivDoc = res.body.dateDelivDoc ? dayjs(res.body.dateDelivDoc) : undefined;
      res.body.dateExpDoc = res.body.dateExpDoc ? dayjs(res.body.dateExpDoc) : undefined;
      res.body.dateExpSejour = res.body.dateExpSejour ? dayjs(res.body.dateExpSejour) : undefined;
      res.body.dateDelivDebut = res.body.dateDelivDebut ? dayjs(res.body.dateDelivDebut) : undefined;
      res.body.dateDelivFin = res.body.dateDelivFin ? dayjs(res.body.dateDelivFin) : undefined;
      res.body.dateEmpreint = res.body.dateEmpreint ? dayjs(res.body.dateEmpreint) : undefined;
      res.body.dateDelivAutor = res.body.dateDelivAutor ? dayjs(res.body.dateDelivAutor) : undefined;
      res.body.dateValideAtorDebut = res.body.dateValideAtorDebut ? dayjs(res.body.dateValideAtorDebut) : undefined;
      res.body.dateValideAutorFin = res.body.dateValideAutorFin ? dayjs(res.body.dateValideAutorFin) : undefined;
      res.body.dateArrivPrevu = res.body.dateArrivPrevu ? dayjs(res.body.dateArrivPrevu) : undefined;
      res.body.dateDepartPrevu = res.body.dateDepartPrevu ? dayjs(res.body.dateDepartPrevu) : undefined;
      res.body.dateNaissCit = res.body.dateNaissCit ? dayjs(res.body.dateNaissCit) : undefined;
      res.body.dateForm = res.body.dateForm ? dayjs(res.body.dateForm) : undefined;
      res.body.dateaut = res.body.dateaut ? dayjs(res.body.dateaut) : undefined;
      res.body.datecreatefo = res.body.datecreatefo ? dayjs(res.body.datecreatefo) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((person: IPerson) => {
        person.dateDelivDoc = person.dateDelivDoc ? dayjs(person.dateDelivDoc) : undefined;
        person.dateExpDoc = person.dateExpDoc ? dayjs(person.dateExpDoc) : undefined;
        person.dateExpSejour = person.dateExpSejour ? dayjs(person.dateExpSejour) : undefined;
        person.dateDelivDebut = person.dateDelivDebut ? dayjs(person.dateDelivDebut) : undefined;
        person.dateDelivFin = person.dateDelivFin ? dayjs(person.dateDelivFin) : undefined;
        person.dateEmpreint = person.dateEmpreint ? dayjs(person.dateEmpreint) : undefined;
        person.dateDelivAutor = person.dateDelivAutor ? dayjs(person.dateDelivAutor) : undefined;
        person.dateValideAtorDebut = person.dateValideAtorDebut ? dayjs(person.dateValideAtorDebut) : undefined;
        person.dateValideAutorFin = person.dateValideAutorFin ? dayjs(person.dateValideAutorFin) : undefined;
        person.dateArrivPrevu = person.dateArrivPrevu ? dayjs(person.dateArrivPrevu) : undefined;
        person.dateDepartPrevu = person.dateDepartPrevu ? dayjs(person.dateDepartPrevu) : undefined;
        person.dateNaissCit = person.dateNaissCit ? dayjs(person.dateNaissCit) : undefined;
        person.dateForm = person.dateForm ? dayjs(person.dateForm) : undefined;
        person.dateaut = person.dateaut ? dayjs(person.dateaut) : undefined;
        person.datecreatefo = person.datecreatefo ? dayjs(person.datecreatefo) : undefined;
      });
    }
    return res;
  }
}
