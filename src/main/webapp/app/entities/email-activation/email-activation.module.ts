import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EmailActivationComponent } from './list/email-activation.component';
import { EmailActivationDetailComponent } from './detail/email-activation-detail.component';
import { EmailActivationUpdateComponent } from './update/email-activation-update.component';
import { EmailActivationDeleteDialogComponent } from './delete/email-activation-delete-dialog.component';
import { EmailActivationRoutingModule } from './route/email-activation-routing.module';

@NgModule({
  imports: [SharedModule, EmailActivationRoutingModule],
  declarations: [
    EmailActivationComponent,
    EmailActivationDetailComponent,
    EmailActivationUpdateComponent,
    EmailActivationDeleteDialogComponent,
  ],
  entryComponents: [EmailActivationDeleteDialogComponent],
})
export class EmailActivationModule {}
