import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAVService } from '../av-service.model';
import { AVServiceService } from '../service/av-service.service';

@Component({
  templateUrl: './av-service-delete-dialog.component.html',
})
export class AVServiceDeleteDialogComponent {
  aVService?: IAVService;

  constructor(protected aVServiceService: AVServiceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aVServiceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
