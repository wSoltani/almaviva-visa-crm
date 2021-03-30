import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DayOffComponent } from './list/day-off.component';
import { DayOffDetailComponent } from './detail/day-off-detail.component';
import { DayOffUpdateComponent } from './update/day-off-update.component';
import { DayOffDeleteDialogComponent } from './delete/day-off-delete-dialog.component';
import { DayOffRoutingModule } from './route/day-off-routing.module';

@NgModule({
  imports: [SharedModule, DayOffRoutingModule],
  declarations: [DayOffComponent, DayOffDetailComponent, DayOffUpdateComponent, DayOffDeleteDialogComponent],
  entryComponents: [DayOffDeleteDialogComponent],
})
export class DayOffModule {}
