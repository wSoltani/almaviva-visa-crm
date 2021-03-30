import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVisaDocuments } from '../visa-documents.model';

@Component({
  selector: 'jhi-visa-documents-detail',
  templateUrl: './visa-documents-detail.component.html',
})
export class VisaDocumentsDetailComponent implements OnInit {
  visaDocuments: IVisaDocuments | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visaDocuments }) => {
      this.visaDocuments = visaDocuments;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
