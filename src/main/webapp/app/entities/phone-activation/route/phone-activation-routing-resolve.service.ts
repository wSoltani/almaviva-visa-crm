import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPhoneActivation, PhoneActivation } from '../phone-activation.model';
import { PhoneActivationService } from '../service/phone-activation.service';

@Injectable({ providedIn: 'root' })
export class PhoneActivationRoutingResolveService implements Resolve<IPhoneActivation> {
  constructor(protected service: PhoneActivationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPhoneActivation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((phoneActivation: HttpResponse<PhoneActivation>) => {
          if (phoneActivation.body) {
            return of(phoneActivation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PhoneActivation());
  }
}
