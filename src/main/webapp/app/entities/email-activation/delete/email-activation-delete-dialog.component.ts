import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmailActivation } from '../email-activation.model';
import { EmailActivationService } from '../service/email-activation.service';

@Component({
  templateUrl: './email-activation-delete-dialog.component.html',
})
export class EmailActivationDeleteDialogComponent {
  emailActivation?: IEmailActivation;

  constructor(protected emailActivationService: EmailActivationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.emailActivationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
