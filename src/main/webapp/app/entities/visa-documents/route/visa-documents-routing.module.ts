import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VisaDocumentsComponent } from '../list/visa-documents.component';
import { VisaDocumentsDetailComponent } from '../detail/visa-documents-detail.component';
import { VisaDocumentsUpdateComponent } from '../update/visa-documents-update.component';
import { VisaDocumentsRoutingResolveService } from './visa-documents-routing-resolve.service';

const visaDocumentsRoute: Routes = [
  {
    path: '',
    component: VisaDocumentsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VisaDocumentsDetailComponent,
    resolve: {
      visaDocuments: VisaDocumentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VisaDocumentsUpdateComponent,
    resolve: {
      visaDocuments: VisaDocumentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VisaDocumentsUpdateComponent,
    resolve: {
      visaDocuments: VisaDocumentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(visaDocumentsRoute)],
  exports: [RouterModule],
})
export class VisaDocumentsRoutingModule {}
