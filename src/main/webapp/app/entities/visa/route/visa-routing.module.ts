import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VisaComponent } from '../list/visa.component';
import { VisaDetailComponent } from '../detail/visa-detail.component';
import { VisaUpdateComponent } from '../update/visa-update.component';
import { VisaRoutingResolveService } from './visa-routing-resolve.service';

const visaRoute: Routes = [
  {
    path: '',
    component: VisaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VisaDetailComponent,
    resolve: {
      visa: VisaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VisaUpdateComponent,
    resolve: {
      visa: VisaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VisaUpdateComponent,
    resolve: {
      visa: VisaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(visaRoute)],
  exports: [RouterModule],
})
export class VisaRoutingModule {}
