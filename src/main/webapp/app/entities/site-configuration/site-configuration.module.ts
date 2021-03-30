import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SiteConfigurationComponent } from './list/site-configuration.component';
import { SiteConfigurationDetailComponent } from './detail/site-configuration-detail.component';
import { SiteConfigurationUpdateComponent } from './update/site-configuration-update.component';
import { SiteConfigurationDeleteDialogComponent } from './delete/site-configuration-delete-dialog.component';
import { SiteConfigurationRoutingModule } from './route/site-configuration-routing.module';

@NgModule({
  imports: [SharedModule, SiteConfigurationRoutingModule],
  declarations: [
    SiteConfigurationComponent,
    SiteConfigurationDetailComponent,
    SiteConfigurationUpdateComponent,
    SiteConfigurationDeleteDialogComponent,
  ],
  entryComponents: [SiteConfigurationDeleteDialogComponent],
})
export class SiteConfigurationModule {}
