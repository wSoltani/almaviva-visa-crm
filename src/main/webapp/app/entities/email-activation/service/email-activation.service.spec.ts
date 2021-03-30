import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEmailActivation, EmailActivation } from '../email-activation.model';

import { EmailActivationService } from './email-activation.service';

describe('Service Tests', () => {
  describe('EmailActivation Service', () => {
    let service: EmailActivationService;
    let httpMock: HttpTestingController;
    let elemDefault: IEmailActivation;
    let expectedResult: IEmailActivation | IEmailActivation[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EmailActivationService);
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

      it('should create a EmailActivation', () => {
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

        service.create(new EmailActivation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EmailActivation', () => {
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

      it('should partial update a EmailActivation', () => {
        const patchObject = Object.assign(
          {
            expirationDate: currentDate.format(DATE_FORMAT),
          },
          new EmailActivation()
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

      it('should return a list of EmailActivation', () => {
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

      it('should delete a EmailActivation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEmailActivationToCollectionIfMissing', () => {
        it('should add a EmailActivation to an empty array', () => {
          const emailActivation: IEmailActivation = { id: 123 };
          expectedResult = service.addEmailActivationToCollectionIfMissing([], emailActivation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(emailActivation);
        });

        it('should not add a EmailActivation to an array that contains it', () => {
          const emailActivation: IEmailActivation = { id: 123 };
          const emailActivationCollection: IEmailActivation[] = [
            {
              ...emailActivation,
            },
            { id: 456 },
          ];
          expectedResult = service.addEmailActivationToCollectionIfMissing(emailActivationCollection, emailActivation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EmailActivation to an array that doesn't contain it", () => {
          const emailActivation: IEmailActivation = { id: 123 };
          const emailActivationCollection: IEmailActivation[] = [{ id: 456 }];
          expectedResult = service.addEmailActivationToCollectionIfMissing(emailActivationCollection, emailActivation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(emailActivation);
        });

        it('should add only unique EmailActivation to an array', () => {
          const emailActivationArray: IEmailActivation[] = [{ id: 123 }, { id: 456 }, { id: 25949 }];
          const emailActivationCollection: IEmailActivation[] = [{ id: 123 }];
          expectedResult = service.addEmailActivationToCollectionIfMissing(emailActivationCollection, ...emailActivationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const emailActivation: IEmailActivation = { id: 123 };
          const emailActivation2: IEmailActivation = { id: 456 };
          expectedResult = service.addEmailActivationToCollectionIfMissing([], emailActivation, emailActivation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(emailActivation);
          expect(expectedResult).toContain(emailActivation2);
        });

        it('should accept null and undefined values', () => {
          const emailActivation: IEmailActivation = { id: 123 };
          expectedResult = service.addEmailActivationToCollectionIfMissing([], null, emailActivation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(emailActivation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
