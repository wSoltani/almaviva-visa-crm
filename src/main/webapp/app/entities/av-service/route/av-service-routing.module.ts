import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AVServiceComponent } from '../list/av-service.component';
import { AVServiceDetailComponent } from '../detail/av-service-detail.component';
import { AVServiceUpdateComponent } from '../update/av-service-update.component';
import { AVServiceRoutingResolveService } from './av-service-routing-resolve.service';

const aVServiceRoute: Routes = [
  {
    path: '',
    component: AVServiceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AVServiceDetailComponent,
    resolve: {
      aVService: AVServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AVServiceUpdateComponent,
    resolve: {
      aVService: AVServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AVServiceUpdateComponent,
    resolve: {
      aVService: AVServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aVServiceRoute)],
  exports: [RouterModule],
})
export class AVServiceRoutingModule {}
