import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPhoneActivation } from '../phone-activation.model';
import { PhoneActivationService } from '../service/phone-activation.service';

@Component({
  templateUrl: './phone-activation-delete-dialog.component.html',
})
export class PhoneActivationDeleteDialogComponent {
  phoneActivation?: IPhoneActivation;

  constructor(protected phoneActivationService: PhoneActivationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.phoneActivationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
