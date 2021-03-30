import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDayOff, DayOff } from '../day-off.model';

import { DayOffService } from './day-off.service';

describe('Service Tests', () => {
  describe('DayOff Service', () => {
    let service: DayOffService;
    let httpMock: HttpTestingController;
    let elemDefault: IDayOff;
    let expectedResult: IDayOff | IDayOff[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DayOffService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        description: 'AAAAAAA',
        date: currentDate,
        isHoliday: false,
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

      it('should create a DayOff', () => {
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

        service.create(new DayOff()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DayOff', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            isHoliday: true,
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

      it('should partial update a DayOff', () => {
        const patchObject = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            isHoliday: true,
          },
          new DayOff()
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

      it('should return a list of DayOff', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
            isHoliday: true,
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

      it('should delete a DayOff', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDayOffToCollectionIfMissing', () => {
        it('should add a DayOff to an empty array', () => {
          const dayOff: IDayOff = { id: 123 };
          expectedResult = service.addDayOffToCollectionIfMissing([], dayOff);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dayOff);
        });

        it('should not add a DayOff to an array that contains it', () => {
          const dayOff: IDayOff = { id: 123 };
          const dayOffCollection: IDayOff[] = [
            {
              ...dayOff,
            },
            { id: 456 },
          ];
          expectedResult = service.addDayOffToCollectionIfMissing(dayOffCollection, dayOff);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DayOff to an array that doesn't contain it", () => {
          const dayOff: IDayOff = { id: 123 };
          const dayOffCollection: IDayOff[] = [{ id: 456 }];
          expectedResult = service.addDayOffToCollectionIfMissing(dayOffCollection, dayOff);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dayOff);
        });

        it('should add only unique DayOff to an array', () => {
          const dayOffArray: IDayOff[] = [{ id: 123 }, { id: 456 }, { id: 86939 }];
          const dayOffCollection: IDayOff[] = [{ id: 123 }];
          expectedResult = service.addDayOffToCollectionIfMissing(dayOffCollection, ...dayOffArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dayOff: IDayOff = { id: 123 };
          const dayOff2: IDayOff = { id: 456 };
          expectedResult = service.addDayOffToCollectionIfMissing([], dayOff, dayOff2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dayOff);
          expect(expectedResult).toContain(dayOff2);
        });

        it('should accept null and undefined values', () => {
          const dayOff: IDayOff = { id: 123 };
          expectedResult = service.addDayOffToCollectionIfMissing([], null, dayOff, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dayOff);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
