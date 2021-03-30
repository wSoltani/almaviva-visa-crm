import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVisaDocuments } from '../visa-documents.model';
import { VisaDocumentsService } from '../service/visa-documents.service';
import { VisaDocumentsDeleteDialogComponent } from '../delete/visa-documents-delete-dialog.component';

@Component({
  selector: 'jhi-visa-documents',
  templateUrl: './visa-documents.component.html',
})
export class VisaDocumentsComponent implements OnInit {
  visaDocuments?: IVisaDocuments[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected visaDocumentsService: VisaDocumentsService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.visaDocumentsService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IVisaDocuments[]>) => {
            this.isLoading = false;
            this.visaDocuments = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.visaDocumentsService.query().subscribe(
      (res: HttpResponse<IVisaDocuments[]>) => {
        this.isLoading = false;
        this.visaDocuments = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IVisaDocuments): number {
    return item.id!;
  }

  delete(visaDocuments: IVisaDocuments): void {
    const modalRef = this.modalService.open(VisaDocumentsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.visaDocuments = visaDocuments;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
