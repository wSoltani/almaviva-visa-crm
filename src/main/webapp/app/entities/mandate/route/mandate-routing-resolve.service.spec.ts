jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMandate, Mandate } from '../mandate.model';
import { MandateService } from '../service/mandate.service';

import { MandateRoutingResolveService } from './mandate-routing-resolve.service';

describe('Service Tests', () => {
  describe('Mandate routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MandateRoutingResolveService;
    let service: MandateService;
    let resultMandate: IMandate | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MandateRoutingResolveService);
      service = TestBed.inject(MandateService);
      resultMandate = undefined;
    });

    describe('resolve', () => {
      it('should return IMandate returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMandate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMandate).toEqual({ id: 123 });
      });

      it('should return new IMandate if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMandate = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMandate).toEqual(new Mandate());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMandate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMandate).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
