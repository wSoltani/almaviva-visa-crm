import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PhoneActivationComponent } from '../list/phone-activation.component';
import { PhoneActivationDetailComponent } from '../detail/phone-activation-detail.component';
import { PhoneActivationUpdateComponent } from '../update/phone-activation-update.component';
import { PhoneActivationRoutingResolveService } from './phone-activation-routing-resolve.service';

const phoneActivationRoute: Routes = [
  {
    path: '',
    component: PhoneActivationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PhoneActivationDetailComponent,
    resolve: {
      phoneActivation: PhoneActivationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PhoneActivationUpdateComponent,
    resolve: {
      phoneActivation: PhoneActivationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PhoneActivationUpdateComponent,
    resolve: {
      phoneActivation: PhoneActivationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(phoneActivationRoute)],
  exports: [RouterModule],
})
export class PhoneActivationRoutingModule {}
