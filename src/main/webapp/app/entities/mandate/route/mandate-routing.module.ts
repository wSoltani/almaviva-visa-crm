import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MandateComponent } from '../list/mandate.component';
import { MandateDetailComponent } from '../detail/mandate-detail.component';
import { MandateUpdateComponent } from '../update/mandate-update.component';
import { MandateRoutingResolveService } from './mandate-routing-resolve.service';

const mandateRoute: Routes = [
  {
    path: '',
    component: MandateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MandateDetailComponent,
    resolve: {
      mandate: MandateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MandateUpdateComponent,
    resolve: {
      mandate: MandateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MandateUpdateComponent,
    resolve: {
      mandate: MandateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mandateRoute)],
  exports: [RouterModule],
})
export class MandateRoutingModule {}
