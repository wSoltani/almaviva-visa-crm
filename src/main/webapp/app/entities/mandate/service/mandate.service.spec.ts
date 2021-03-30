import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMandate, Mandate } from '../mandate.model';

import { MandateService } from './mandate.service';

describe('Service Tests', () => {
  describe('Mandate Service', () => {
    let service: MandateService;
    let httpMock: HttpTestingController;
    let elemDefault: IMandate;
    let expectedResult: IMandate | IMandate[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MandateService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        code: 0,
        location: 'AAAAAAA',
        amount: 0,
        date: currentDate,
        isAVSPaiment: false,
        deleted: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Mandate', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new Mandate()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Mandate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 1,
            location: 'BBBBBB',
            amount: 1,
            date: currentDate.format(DATE_FORMAT),
            isAVSPaiment: true,
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Mandate', () => {
        const patchObject = Object.assign(
          {
            code: 1,
            location: 'BBBBBB',
            amount: 1,
            date: currentDate.format(DATE_FORMAT),
            isAVSPaiment: true,
          },
          new Mandate()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Mandate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 1,
            location: 'BBBBBB',
            amount: 1,
            date: currentDate.format(DATE_FORMAT),
            isAVSPaiment: true,
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Mandate', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMandateToCollectionIfMissing', () => {
        it('should add a Mandate to an empty array', () => {
          const mandate: IMandate = { id: 123 };
          expectedResult = service.addMandateToCollectionIfMissing([], mandate);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mandate);
        });

        it('should not add a Mandate to an array that contains it', () => {
          const mandate: IMandate = { id: 123 };
          const mandateCollection: IMandate[] = [
            {
              ...mandate,
            },
            { id: 456 },
          ];
          expectedResult = service.addMandateToCollectionIfMissing(mandateCollection, mandate);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Mandate to an array that doesn't contain it", () => {
          const mandate: IMandate = { id: 123 };
          const mandateCollection: IMandate[] = [{ id: 456 }];
          expectedResult = service.addMandateToCollectionIfMissing(mandateCollection, mandate);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mandate);
        });

        it('should add only unique Mandate to an array', () => {
          const mandateArray: IMandate[] = [{ id: 123 }, { id: 456 }, { id: 22105 }];
          const mandateCollection: IMandate[] = [{ id: 123 }];
          expectedResult = service.addMandateToCollectionIfMissing(mandateCollection, ...mandateArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mandate: IMandate = { id: 123 };
          const mandate2: IMandate = { id: 456 };
          expectedResult = service.addMandateToCollectionIfMissing([], mandate, mandate2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mandate);
          expect(expectedResult).toContain(mandate2);
        });

        it('should accept null and undefined values', () => {
          const mandate: IMandate = { id: 123 };
          expectedResult = service.addMandateToCollectionIfMissing([], null, mandate, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mandate);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
