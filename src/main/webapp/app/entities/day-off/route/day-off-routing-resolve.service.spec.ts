jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDayOff, DayOff } from '../day-off.model';
import { DayOffService } from '../service/day-off.service';

import { DayOffRoutingResolveService } from './day-off-routing-resolve.service';

describe('Service Tests', () => {
  describe('DayOff routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DayOffRoutingResolveService;
    let service: DayOffService;
    let resultDayOff: IDayOff | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DayOffRoutingResolveService);
      service = TestBed.inject(DayOffService);
      resultDayOff = undefined;
    });

    describe('resolve', () => {
      it('should return IDayOff returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDayOff = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDayOff).toEqual({ id: 123 });
      });

      it('should return new IDayOff if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDayOff = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDayOff).toEqual(new DayOff());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDayOff = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDayOff).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
