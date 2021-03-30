import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVisa, Visa } from '../visa.model';

import { VisaService } from './visa.service';

describe('Service Tests', () => {
  describe('Visa Service', () => {
    let service: VisaService;
    let httpMock: HttpTestingController;
    let elemDefault: IVisa;
    let expectedResult: IVisa | IVisa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VisaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        price: 0,
        description: 'AAAAAAA',
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

      it('should create a Visa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Visa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Visa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            price: 1,
            description: 'BBBBBB',
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

      it('should partial update a Visa', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            price: 1,
            description: 'BBBBBB',
            deleted: true,
          },
          new Visa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Visa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            price: 1,
            description: 'BBBBBB',
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

      it('should delete a Visa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVisaToCollectionIfMissing', () => {
        it('should add a Visa to an empty array', () => {
          const visa: IVisa = { id: 123 };
          expectedResult = service.addVisaToCollectionIfMissing([], visa);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(visa);
        });

        it('should not add a Visa to an array that contains it', () => {
          const visa: IVisa = { id: 123 };
          const visaCollection: IVisa[] = [
            {
              ...visa,
            },
            { id: 456 },
          ];
          expectedResult = service.addVisaToCollectionIfMissing(visaCollection, visa);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Visa to an array that doesn't contain it", () => {
          const visa: IVisa = { id: 123 };
          const visaCollection: IVisa[] = [{ id: 456 }];
          expectedResult = service.addVisaToCollectionIfMissing(visaCollection, visa);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(visa);
        });

        it('should add only unique Visa to an array', () => {
          const visaArray: IVisa[] = [{ id: 123 }, { id: 456 }, { id: 14760 }];
          const visaCollection: IVisa[] = [{ id: 123 }];
          expectedResult = service.addVisaToCollectionIfMissing(visaCollection, ...visaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const visa: IVisa = { id: 123 };
          const visa2: IVisa = { id: 456 };
          expectedResult = service.addVisaToCollectionIfMissing([], visa, visa2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(visa);
          expect(expectedResult).toContain(visa2);
        });

        it('should accept null and undefined values', () => {
          const visa: IVisa = { id: 123 };
          expectedResult = service.addVisaToCollectionIfMissing([], null, visa, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(visa);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
