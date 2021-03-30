import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVisa, Visa } from '../visa.model';
import { VisaService } from '../service/visa.service';

@Injectable({ providedIn: 'root' })
export class VisaRoutingResolveService implements Resolve<IVisa> {
  constructor(protected service: VisaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVisa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((visa: HttpResponse<Visa>) => {
          if (visa.body) {
            return of(visa.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Visa());
  }
}
