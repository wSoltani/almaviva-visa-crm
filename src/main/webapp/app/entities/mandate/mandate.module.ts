import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MandateComponent } from './list/mandate.component';
import { MandateDetailComponent } from './detail/mandate-detail.component';
import { MandateUpdateComponent } from './update/mandate-update.component';
import { MandateDeleteDialogComponent } from './delete/mandate-delete-dialog.component';
import { MandateRoutingModule } from './route/mandate-routing.module';

@NgModule({
  imports: [SharedModule, MandateRoutingModule],
  declarations: [MandateComponent, MandateDetailComponent, MandateUpdateComponent, MandateDeleteDialogComponent],
  entryComponents: [MandateDeleteDialogComponent],
})
export class MandateModule {}
