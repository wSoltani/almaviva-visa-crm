import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAVService, AVService } from '../av-service.model';

import { AVServiceService } from './av-service.service';

describe('Service Tests', () => {
  describe('AVService Service', () => {
    let service: AVServiceService;
    let httpMock: HttpTestingController;
    let elemDefault: IAVService;
    let expectedResult: IAVService | IAVService[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AVServiceService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        description: 'AAAAAAA',
        price: 0,
        quantity: 0,
        isPrincipal: false,
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

      it('should create a AVService', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AVService()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AVService', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
            price: 1,
            quantity: 1,
            isPrincipal: true,
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

      it('should partial update a AVService', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            quantity: 1,
            isPrincipal: true,
          },
          new AVService()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AVService', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
            price: 1,
            quantity: 1,
            isPrincipal: true,
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

      it('should delete a AVService', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAVServiceToCollectionIfMissing', () => {
        it('should add a AVService to an empty array', () => {
          const aVService: IAVService = { id: 123 };
          expectedResult = service.addAVServiceToCollectionIfMissing([], aVService);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aVService);
        });

        it('should not add a AVService to an array that contains it', () => {
          const aVService: IAVService = { id: 123 };
          const aVServiceCollection: IAVService[] = [
            {
              ...aVService,
            },
            { id: 456 },
          ];
          expectedResult = service.addAVServiceToCollectionIfMissing(aVServiceCollection, aVService);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AVService to an array that doesn't contain it", () => {
          const aVService: IAVService = { id: 123 };
          const aVServiceCollection: IAVService[] = [{ id: 456 }];
          expectedResult = service.addAVServiceToCollectionIfMissing(aVServiceCollection, aVService);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aVService);
        });

        it('should add only unique AVService to an array', () => {
          const aVServiceArray: IAVService[] = [{ id: 123 }, { id: 456 }, { id: 30857 }];
          const aVServiceCollection: IAVService[] = [{ id: 123 }];
          expectedResult = service.addAVServiceToCollectionIfMissing(aVServiceCollection, ...aVServiceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const aVService: IAVService = { id: 123 };
          const aVService2: IAVService = { id: 456 };
          expectedResult = service.addAVServiceToCollectionIfMissing([], aVService, aVService2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(aVService);
          expect(expectedResult).toContain(aVService2);
        });

        it('should accept null and undefined values', () => {
          const aVService: IAVService = { id: 123 };
          expectedResult = service.addAVServiceToCollectionIfMissing([], null, aVService, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(aVService);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
