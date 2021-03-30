import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DayOffComponent } from '../list/day-off.component';
import { DayOffDetailComponent } from '../detail/day-off-detail.component';
import { DayOffUpdateComponent } from '../update/day-off-update.component';
import { DayOffRoutingResolveService } from './day-off-routing-resolve.service';

const dayOffRoute: Routes = [
  {
    path: '',
    component: DayOffComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DayOffDetailComponent,
    resolve: {
      dayOff: DayOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DayOffUpdateComponent,
    resolve: {
      dayOff: DayOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DayOffUpdateComponent,
    resolve: {
      dayOff: DayOffRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dayOffRoute)],
  exports: [RouterModule],
})
export class DayOffRoutingModule {}
