import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVisaDocuments } from '../visa-documents.model';
import { VisaDocumentsService } from '../service/visa-documents.service';

@Component({
  templateUrl: './visa-documents-delete-dialog.component.html',
})
export class VisaDocumentsDeleteDialogComponent {
  visaDocuments?: IVisaDocuments;

  constructor(protected visaDocumentsService: VisaDocumentsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.visaDocumentsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
