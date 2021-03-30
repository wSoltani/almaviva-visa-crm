jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPhoneActivation, PhoneActivation } from '../phone-activation.model';
import { PhoneActivationService } from '../service/phone-activation.service';

import { PhoneActivationRoutingResolveService } from './phone-activation-routing-resolve.service';

describe('Service Tests', () => {
  describe('PhoneActivation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PhoneActivationRoutingResolveService;
    let service: PhoneActivationService;
    let resultPhoneActivation: IPhoneActivation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PhoneActivationRoutingResolveService);
      service = TestBed.inject(PhoneActivationService);
      resultPhoneActivation = undefined;
    });

    describe('resolve', () => {
      it('should return IPhoneActivation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPhoneActivation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPhoneActivation).toEqual({ id: 123 });
      });

      it('should return new IPhoneActivation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPhoneActivation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPhoneActivation).toEqual(new PhoneActivation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPhoneActivation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPhoneActivation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
