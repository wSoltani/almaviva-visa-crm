import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVisaDocuments, VisaDocuments } from '../visa-documents.model';

import { VisaDocumentsService } from './visa-documents.service';

describe('Service Tests', () => {
  describe('VisaDocuments Service', () => {
    let service: VisaDocumentsService;
    let httpMock: HttpTestingController;
    let elemDefault: IVisaDocuments;
    let expectedResult: IVisaDocuments | IVisaDocuments[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VisaDocumentsService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
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

      it('should create a VisaDocuments', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new VisaDocuments()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a VisaDocuments', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
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

      it('should partial update a VisaDocuments', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
          },
          new VisaDocuments()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of VisaDocuments', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
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

      it('should delete a VisaDocuments', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVisaDocumentsToCollectionIfMissing', () => {
        it('should add a VisaDocuments to an empty array', () => {
          const visaDocuments: IVisaDocuments = { id: 123 };
          expectedResult = service.addVisaDocumentsToCollectionIfMissing([], visaDocuments);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(visaDocuments);
        });

        it('should not add a VisaDocuments to an array that contains it', () => {
          const visaDocuments: IVisaDocuments = { id: 123 };
          const visaDocumentsCollection: IVisaDocuments[] = [
            {
              ...visaDocuments,
            },
            { id: 456 },
          ];
          expectedResult = service.addVisaDocumentsToCollectionIfMissing(visaDocumentsCollection, visaDocuments);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a VisaDocuments to an array that doesn't contain it", () => {
          const visaDocuments: IVisaDocuments = { id: 123 };
          const visaDocumentsCollection: IVisaDocuments[] = [{ id: 456 }];
          expectedResult = service.addVisaDocumentsToCollectionIfMissing(visaDocumentsCollection, visaDocuments);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(visaDocuments);
        });

        it('should add only unique VisaDocuments to an array', () => {
          const visaDocumentsArray: IVisaDocuments[] = [{ id: 123 }, { id: 456 }, { id: 70840 }];
          const visaDocumentsCollection: IVisaDocuments[] = [{ id: 123 }];
          expectedResult = service.addVisaDocumentsToCollectionIfMissing(visaDocumentsCollection, ...visaDocumentsArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const visaDocuments: IVisaDocuments = { id: 123 };
          const visaDocuments2: IVisaDocuments = { id: 456 };
          expectedResult = service.addVisaDocumentsToCollectionIfMissing([], visaDocuments, visaDocuments2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(visaDocuments);
          expect(expectedResult).toContain(visaDocuments2);
        });

        it('should accept null and undefined values', () => {
          const visaDocuments: IVisaDocuments = { id: 123 };
          expectedResult = service.addVisaDocumentsToCollectionIfMissing([], null, visaDocuments, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(visaDocuments);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
