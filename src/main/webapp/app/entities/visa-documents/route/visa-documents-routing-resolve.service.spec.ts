jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVisaDocuments, VisaDocuments } from '../visa-documents.model';
import { VisaDocumentsService } from '../service/visa-documents.service';

import { VisaDocumentsRoutingResolveService } from './visa-documents-routing-resolve.service';

describe('Service Tests', () => {
  describe('VisaDocuments routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VisaDocumentsRoutingResolveService;
    let service: VisaDocumentsService;
    let resultVisaDocuments: IVisaDocuments | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VisaDocumentsRoutingResolveService);
      service = TestBed.inject(VisaDocumentsService);
      resultVisaDocuments = undefined;
    });

    describe('resolve', () => {
      it('should return IVisaDocuments returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVisaDocuments = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVisaDocuments).toEqual({ id: 123 });
      });

      it('should return new IVisaDocuments if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVisaDocuments = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVisaDocuments).toEqual(new VisaDocuments());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVisaDocuments = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVisaDocuments).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
