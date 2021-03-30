import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVisaDocuments, VisaDocuments } from '../visa-documents.model';
import { VisaDocumentsService } from '../service/visa-documents.service';

@Injectable({ providedIn: 'root' })
export class VisaDocumentsRoutingResolveService implements Resolve<IVisaDocuments> {
  constructor(protected service: VisaDocumentsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVisaDocuments> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((visaDocuments: HttpResponse<VisaDocuments>) => {
          if (visaDocuments.body) {
            return of(visaDocuments.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VisaDocuments());
  }
}
