import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISiteConfiguration, SiteConfiguration } from '../site-configuration.model';
import { SiteConfigurationService } from '../service/site-configuration.service';

@Injectable({ providedIn: 'root' })
export class SiteConfigurationRoutingResolveService implements Resolve<ISiteConfiguration> {
  constructor(protected service: SiteConfigurationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISiteConfiguration> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((siteConfiguration: HttpResponse<SiteConfiguration>) => {
          if (siteConfiguration.body) {
            return of(siteConfiguration.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SiteConfiguration());
  }
}
