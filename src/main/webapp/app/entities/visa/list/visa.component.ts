import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVisa } from '../visa.model';
import { VisaService } from '../service/visa.service';
import { VisaDeleteDialogComponent } from '../delete/visa-delete-dialog.component';

@Component({
  selector: 'jhi-visa',
  templateUrl: './visa.component.html',
})
export class VisaComponent implements OnInit {
  visas?: IVisa[];
  isLoading = false;
  currentSearch: string;

  constructor(protected visaService: VisaService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.visaService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IVisa[]>) => {
            this.isLoading = false;
            this.visas = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.visaService.query().subscribe(
      (res: HttpResponse<IVisa[]>) => {
        this.isLoading = false;
        this.visas = res.body ?? [];
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

  trackId(index: number, item: IVisa): number {
    return item.id!;
  }

  delete(visa: IVisa): void {
    const modalRef = this.modalService.open(VisaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.visa = visa;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
