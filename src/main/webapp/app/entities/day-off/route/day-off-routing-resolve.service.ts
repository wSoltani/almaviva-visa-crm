import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDayOff, DayOff } from '../day-off.model';
import { DayOffService } from '../service/day-off.service';

@Injectable({ providedIn: 'root' })
export class DayOffRoutingResolveService implements Resolve<IDayOff> {
  constructor(protected service: DayOffService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDayOff> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dayOff: HttpResponse<DayOff>) => {
          if (dayOff.body) {
            return of(dayOff.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DayOff());
  }
}
