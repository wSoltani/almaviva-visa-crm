import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SiteConfigurationComponent } from '../list/site-configuration.component';
import { SiteConfigurationDetailComponent } from '../detail/site-configuration-detail.component';
import { SiteConfigurationUpdateComponent } from '../update/site-configuration-update.component';
import { SiteConfigurationRoutingResolveService } from './site-configuration-routing-resolve.service';

const siteConfigurationRoute: Routes = [
  {
    path: '',
    component: SiteConfigurationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SiteConfigurationDetailComponent,
    resolve: {
      siteConfiguration: SiteConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SiteConfigurationUpdateComponent,
    resolve: {
      siteConfiguration: SiteConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SiteConfigurationUpdateComponent,
    resolve: {
      siteConfiguration: SiteConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(siteConfigurationRoute)],
  exports: [RouterModule],
})
export class SiteConfigurationRoutingModule {}
