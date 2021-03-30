import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmailActivationComponent } from '../list/email-activation.component';
import { EmailActivationDetailComponent } from '../detail/email-activation-detail.component';
import { EmailActivationUpdateComponent } from '../update/email-activation-update.component';
import { EmailActivationRoutingResolveService } from './email-activation-routing-resolve.service';

const emailActivationRoute: Routes = [
  {
    path: '',
    component: EmailActivationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmailActivationDetailComponent,
    resolve: {
      emailActivation: EmailActivationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmailActivationUpdateComponent,
    resolve: {
      emailActivation: EmailActivationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmailActivationUpdateComponent,
    resolve: {
      emailActivation: EmailActivationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(emailActivationRoute)],
  exports: [RouterModule],
})
export class EmailActivationRoutingModule {}
