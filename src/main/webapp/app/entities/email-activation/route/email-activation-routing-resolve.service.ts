import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmailActivation, EmailActivation } from '../email-activation.model';
import { EmailActivationService } from '../service/email-activation.service';

@Injectable({ providedIn: 'root' })
export class EmailActivationRoutingResolveService implements Resolve<IEmailActivation> {
  constructor(protected service: EmailActivationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmailActivation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((emailActivation: HttpResponse<EmailActivation>) => {
          if (emailActivation.body) {
            return of(emailActivation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EmailActivation());
  }
}
