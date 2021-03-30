import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPhoneActivation, PhoneActivation } from '../phone-activation.model';

import { PhoneActivationService } from './phone-activation.service';

describe('Service Tests', () => {
  describe('PhoneActivation Service', () => {
    let service: PhoneActivationService;
    let httpMock: HttpTestingController;
    let elemDefault: IPhoneActivation;
    let expectedResult: IPhoneActivation | IPhoneActivation[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PhoneActivationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        isActivated: false,
        activationKey: 'AAAAAAA',
        expirationDate: currentDate,
        deleted: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            expirationDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PhoneActivation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            expirationDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.create(new PhoneActivation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PhoneActivation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            isActivated: true,
            activationKey: 'BBBBBB',
            expirationDate: currentDate.format(DATE_FORMAT),
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PhoneActivation', () => {
        const patchObject = Object.assign(
          {
            isActivated: true,
            expirationDate: currentDate.format(DATE_FORMAT),
          },
          new PhoneActivation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PhoneActivation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            isActivated: true,
            activationKey: 'BBBBBB',
            expirationDate: currentDate.format(DATE_FORMAT),
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PhoneActivation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPhoneActivationToCollectionIfMissing', () => {
        it('should add a PhoneActivation to an empty array', () => {
          const phoneActivation: IPhoneActivation = { id: 123 };
          expectedResult = service.addPhoneActivationToCollectionIfMissing([], phoneActivation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(phoneActivation);
        });

        it('should not add a PhoneActivation to an array that contains it', () => {
          const phoneActivation: IPhoneActivation = { id: 123 };
          const phoneActivationCollection: IPhoneActivation[] = [
            {
              ...phoneActivation,
            },
            { id: 456 },
          ];
          expectedResult = service.addPhoneActivationToCollectionIfMissing(phoneActivationCollection, phoneActivation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PhoneActivation to an array that doesn't contain it", () => {
          const phoneActivation: IPhoneActivation = { id: 123 };
          const phoneActivationCollection: IPhoneActivation[] = [{ id: 456 }];
          expectedResult = service.addPhoneActivationToCollectionIfMissing(phoneActivationCollection, phoneActivation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(phoneActivation);
        });

        it('should add only unique PhoneActivation to an array', () => {
          const phoneActivationArray: IPhoneActivation[] = [{ id: 123 }, { id: 456 }, { id: 18522 }];
          const phoneActivationCollection: IPhoneActivation[] = [{ id: 123 }];
          expectedResult = service.addPhoneActivationToCollectionIfMissing(phoneActivationCollection, ...phoneActivationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const phoneActivation: IPhoneActivation = { id: 123 };
          const phoneActivation2: IPhoneActivation = { id: 456 };
          expectedResult = service.addPhoneActivationToCollectionIfMissing([], phoneActivation, phoneActivation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(phoneActivation);
          expect(expectedResult).toContain(phoneActivation2);
        });

        it('should accept null and undefined values', () => {
          const phoneActivation: IPhoneActivation = { id: 123 };
          expectedResult = service.addPhoneActivationToCollectionIfMissing([], null, phoneActivation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(phoneActivation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
