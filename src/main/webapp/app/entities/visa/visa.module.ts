import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { VisaComponent } from './list/visa.component';
import { VisaDetailComponent } from './detail/visa-detail.component';
import { VisaUpdateComponent } from './update/visa-update.component';
import { VisaDeleteDialogComponent } from './delete/visa-delete-dialog.component';
import { VisaRoutingModule } from './route/visa-routing.module';

@NgModule({
  imports: [SharedModule, VisaRoutingModule],
  declarations: [VisaComponent, VisaDetailComponent, VisaUpdateComponent, VisaDeleteDialogComponent],
  entryComponents: [VisaDeleteDialogComponent],
})
export class VisaModule {}
