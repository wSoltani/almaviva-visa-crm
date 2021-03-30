import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDayOff } from '../day-off.model';
import { DayOffService } from '../service/day-off.service';

@Component({
  templateUrl: './day-off-delete-dialog.component.html',
})
export class DayOffDeleteDialogComponent {
  dayOff?: IDayOff;

  constructor(protected dayOffService: DayOffService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dayOffService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
