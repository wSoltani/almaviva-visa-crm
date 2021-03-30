import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMandate } from '../mandate.model';
import { MandateService } from '../service/mandate.service';

@Component({
  templateUrl: './mandate-delete-dialog.component.html',
})
export class MandateDeleteDialogComponent {
  mandate?: IMandate;

  constructor(protected mandateService: MandateService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mandateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
