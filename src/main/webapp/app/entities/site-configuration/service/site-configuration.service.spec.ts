import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISiteConfiguration, SiteConfiguration } from '../site-configuration.model';

import { SiteConfigurationService } from './site-configuration.service';

describe('Service Tests', () => {
  describe('SiteConfiguration Service', () => {
    let service: SiteConfigurationService;
    let httpMock: HttpTestingController;
    let elemDefault: ISiteConfiguration;
    let expectedResult: ISiteConfiguration | ISiteConfiguration[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SiteConfigurationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        startDate: currentDate,
        endDate: currentDate,
        openingTime: 'AAAAAAA',
        closingTime: 'AAAAAAA',
        appointmentTime: 0,
        appointmentQuota: 0,
        appointmentQuotaWeb: 0,
        information: 'AAAAAAA',
        dailyMessage: 'AAAAAAA',
        prestationMargin: 0,
        simultaneous: 0,
        isSpecific: false,
        deleted: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SiteConfiguration', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.create(new SiteConfiguration()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SiteConfiguration', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            startDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
            openingTime: 'BBBBBB',
            closingTime: 'BBBBBB',
            appointmentTime: 1,
            appointmentQuota: 1,
            appointmentQuotaWeb: 1,
            information: 'BBBBBB',
            dailyMessage: 'BBBBBB',
            prestationMargin: 1,
            simultaneous: 1,
            isSpecific: true,
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SiteConfiguration', () => {
        const patchObject = Object.assign(
          {
            startDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
            closingTime: 'BBBBBB',
            appointmentQuota: 1,
            appointmentQuotaWeb: 1,
            simultaneous: 1,
            isSpecific: true,
          },
          new SiteConfiguration()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SiteConfiguration', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            startDate: currentDate.format(DATE_TIME_FORMAT),
            endDate: currentDate.format(DATE_TIME_FORMAT),
            openingTime: 'BBBBBB',
            closingTime: 'BBBBBB',
            appointmentTime: 1,
            appointmentQuota: 1,
            appointmentQuotaWeb: 1,
            information: 'BBBBBB',
            dailyMessage: 'BBBBBB',
            prestationMargin: 1,
            simultaneous: 1,
            isSpecific: true,
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SiteConfiguration', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSiteConfigurationToCollectionIfMissing', () => {
        it('should add a SiteConfiguration to an empty array', () => {
          const siteConfiguration: ISiteConfiguration = { id: 123 };
          expectedResult = service.addSiteConfigurationToCollectionIfMissing([], siteConfiguration);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(siteConfiguration);
        });

        it('should not add a SiteConfiguration to an array that contains it', () => {
          const siteConfiguration: ISiteConfiguration = { id: 123 };
          const siteConfigurationCollection: ISiteConfiguration[] = [
            {
              ...siteConfiguration,
            },
            { id: 456 },
          ];
          expectedResult = service.addSiteConfigurationToCollectionIfMissing(siteConfigurationCollection, siteConfiguration);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SiteConfiguration to an array that doesn't contain it", () => {
          const siteConfiguration: ISiteConfiguration = { id: 123 };
          const siteConfigurationCollection: ISiteConfiguration[] = [{ id: 456 }];
          expectedResult = service.addSiteConfigurationToCollectionIfMissing(siteConfigurationCollection, siteConfiguration);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(siteConfiguration);
        });

        it('should add only unique SiteConfiguration to an array', () => {
          const siteConfigurationArray: ISiteConfiguration[] = [{ id: 123 }, { id: 456 }, { id: 63783 }];
          const siteConfigurationCollection: ISiteConfiguration[] = [{ id: 123 }];
          expectedResult = service.addSiteConfigurationToCollectionIfMissing(siteConfigurationCollection, ...siteConfigurationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const siteConfiguration: ISiteConfiguration = { id: 123 };
          const siteConfiguration2: ISiteConfiguration = { id: 456 };
          expectedResult = service.addSiteConfigurationToCollectionIfMissing([], siteConfiguration, siteConfiguration2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(siteConfiguration);
          expect(expectedResult).toContain(siteConfiguration2);
        });

        it('should accept null and undefined values', () => {
          const siteConfiguration: ISiteConfiguration = { id: 123 };
          expectedResult = service.addSiteConfigurationToCollectionIfMissing([], null, siteConfiguration, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(siteConfiguration);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
