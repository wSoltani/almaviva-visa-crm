import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISiteConfiguration } from '../site-configuration.model';
import { SiteConfigurationService } from '../service/site-configuration.service';

@Component({
  templateUrl: './site-configuration-delete-dialog.component.html',
})
export class SiteConfigurationDeleteDialogComponent {
  siteConfiguration?: ISiteConfiguration;

  constructor(protected siteConfigurationService: SiteConfigurationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.siteConfigurationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
