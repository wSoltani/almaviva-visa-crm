import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AVServiceComponent } from './list/av-service.component';
import { AVServiceDetailComponent } from './detail/av-service-detail.component';
import { AVServiceUpdateComponent } from './update/av-service-update.component';
import { AVServiceDeleteDialogComponent } from './delete/av-service-delete-dialog.component';
import { AVServiceRoutingModule } from './route/av-service-routing.module';

@NgModule({
  imports: [SharedModule, AVServiceRoutingModule],
  declarations: [AVServiceComponent, AVServiceDetailComponent, AVServiceUpdateComponent, AVServiceDeleteDialogComponent],
  entryComponents: [AVServiceDeleteDialogComponent],
})
export class AVServiceModule {}
