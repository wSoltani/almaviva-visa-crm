import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { VisaDocumentsComponent } from './list/visa-documents.component';
import { VisaDocumentsDetailComponent } from './detail/visa-documents-detail.component';
import { VisaDocumentsUpdateComponent } from './update/visa-documents-update.component';
import { VisaDocumentsDeleteDialogComponent } from './delete/visa-documents-delete-dialog.component';
import { VisaDocumentsRoutingModule } from './route/visa-documents-routing.module';

@NgModule({
  imports: [SharedModule, VisaDocumentsRoutingModule],
  declarations: [VisaDocumentsComponent, VisaDocumentsDetailComponent, VisaDocumentsUpdateComponent, VisaDocumentsDeleteDialogComponent],
  entryComponents: [VisaDocumentsDeleteDialogComponent],
})
export class VisaDocumentsModule {}
