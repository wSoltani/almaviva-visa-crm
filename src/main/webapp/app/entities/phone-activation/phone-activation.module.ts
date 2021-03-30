import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PhoneActivationComponent } from './list/phone-activation.component';
import { PhoneActivationDetailComponent } from './detail/phone-activation-detail.component';
import { PhoneActivationUpdateComponent } from './update/phone-activation-update.component';
import { PhoneActivationDeleteDialogComponent } from './delete/phone-activation-delete-dialog.component';
import { PhoneActivationRoutingModule } from './route/phone-activation-routing.module';

@NgModule({
  imports: [SharedModule, PhoneActivationRoutingModule],
  declarations: [
    PhoneActivationComponent,
    PhoneActivationDetailComponent,
    PhoneActivationUpdateComponent,
    PhoneActivationDeleteDialogComponent,
  ],
  entryComponents: [PhoneActivationDeleteDialogComponent],
})
export class PhoneActivationModule {}
