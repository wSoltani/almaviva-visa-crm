import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAVService, AVService } from '../av-service.model';
import { AVServiceService } from '../service/av-service.service';

@Injectable({ providedIn: 'root' })
export class AVServiceRoutingResolveService implements Resolve<IAVService> {
  constructor(protected service: AVServiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAVService> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aVService: HttpResponse<AVService>) => {
          if (aVService.body) {
            return of(aVService.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AVService());
  }
}
