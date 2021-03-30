import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVisa } from '../visa.model';
import { VisaService } from '../service/visa.service';

@Component({
  templateUrl: './visa-delete-dialog.component.html',
})
export class VisaDeleteDialogComponent {
  visa?: IVisa;

  constructor(protected visaService: VisaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.visaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
