import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMandate } from '../mandate.model';
import { MandateService } from '../service/mandate.service';
import { MandateDeleteDialogComponent } from '../delete/mandate-delete-dialog.component';

@Component({
  selector: 'jhi-mandate',
  templateUrl: './mandate.component.html',
})
export class MandateComponent implements OnInit {
  mandates?: IMandate[];
  isLoading = false;
  currentSearch: string;

  constructor(protected mandateService: MandateService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.mandateService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IMandate[]>) => {
            this.isLoading = false;
            this.mandates = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.mandateService.query().subscribe(
      (res: HttpResponse<IMandate[]>) => {
        this.isLoading = false;
        this.mandates = res.body ?? [];
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

  trackId(index: number, item: IMandate): number {
    return item.id!;
  }

  delete(mandate: IMandate): void {
    const modalRef = this.modalService.open(MandateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mandate = mandate;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
