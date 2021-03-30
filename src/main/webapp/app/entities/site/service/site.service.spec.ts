import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISite, Site } from '../site.model';

import { SiteService } from './site.service';

describe('Service Tests', () => {
  describe('Site Service', () => {
    let service: SiteService;
    let httpMock: HttpTestingController;
    let elemDefault: ISite;
    let expectedResult: ISite | ISite[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SiteService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        imgUrl: 'AAAAAAA',
        address: 'AAAAAAA',
        deleted: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Site', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Site()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Site', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            imgUrl: 'BBBBBB',
            address: 'BBBBBB',
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Site', () => {
        const patchObject = Object.assign(
          {
            imgUrl: 'BBBBBB',
            address: 'BBBBBB',
            deleted: true,
          },
          new Site()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Site', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            imgUrl: 'BBBBBB',
            address: 'BBBBBB',
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Site', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSiteToCollectionIfMissing', () => {
        it('should add a Site to an empty array', () => {
          const site: ISite = { id: 123 };
          expectedResult = service.addSiteToCollectionIfMissing([], site);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(site);
        });

        it('should not add a Site to an array that contains it', () => {
          const site: ISite = { id: 123 };
          const siteCollection: ISite[] = [
            {
              ...site,
            },
            { id: 456 },
          ];
          expectedResult = service.addSiteToCollectionIfMissing(siteCollection, site);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Site to an array that doesn't contain it", () => {
          const site: ISite = { id: 123 };
          const siteCollection: ISite[] = [{ id: 456 }];
          expectedResult = service.addSiteToCollectionIfMissing(siteCollection, site);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(site);
        });

        it('should add only unique Site to an array', () => {
          const siteArray: ISite[] = [{ id: 123 }, { id: 456 }, { id: 46914 }];
          const siteCollection: ISite[] = [{ id: 123 }];
          expectedResult = service.addSiteToCollectionIfMissing(siteCollection, ...siteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const site: ISite = { id: 123 };
          const site2: ISite = { id: 456 };
          expectedResult = service.addSiteToCollectionIfMissing([], site, site2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(site);
          expect(expectedResult).toContain(site2);
        });

        it('should accept null and undefined values', () => {
          const site: ISite = { id: 123 };
          expectedResult = service.addSiteToCollectionIfMissing([], null, site, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(site);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
