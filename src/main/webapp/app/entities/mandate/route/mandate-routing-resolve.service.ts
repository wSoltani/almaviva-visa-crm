import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMandate, Mandate } from '../mandate.model';
import { MandateService } from '../service/mandate.service';

@Injectable({ providedIn: 'root' })
export class MandateRoutingResolveService implements Resolve<IMandate> {
  constructor(protected service: MandateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMandate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mandate: HttpResponse<Mandate>) => {
          if (mandate.body) {
            return of(mandate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mandate());
  }
}
