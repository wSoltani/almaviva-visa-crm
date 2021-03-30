jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVisa, Visa } from '../visa.model';
import { VisaService } from '../service/visa.service';

import { VisaRoutingResolveService } from './visa-routing-resolve.service';

describe('Service Tests', () => {
  describe('Visa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VisaRoutingResolveService;
    let service: VisaService;
    let resultVisa: IVisa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VisaRoutingResolveService);
      service = TestBed.inject(VisaService);
      resultVisa = undefined;
    });

    describe('resolve', () => {
      it('should return IVisa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVisa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVisa).toEqual({ id: 123 });
      });

      it('should return new IVisa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVisa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVisa).toEqual(new Visa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVisa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVisa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
