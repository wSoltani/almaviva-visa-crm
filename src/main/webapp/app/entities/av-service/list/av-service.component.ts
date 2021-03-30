import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAVService } from '../av-service.model';
import { AVServiceService } from '../service/av-service.service';
import { AVServiceDeleteDialogComponent } from '../delete/av-service-delete-dialog.component';

@Component({
  selector: 'jhi-av-service',
  templateUrl: './av-service.component.html',
})
export class AVServiceComponent implements OnInit {
  aVServices?: IAVService[];
  isLoading = false;
  currentSearch: string;

  constructor(protected aVServiceService: AVServiceService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.aVServiceService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IAVService[]>) => {
            this.isLoading = false;
            this.aVServices = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.aVServiceService.query().subscribe(
      (res: HttpResponse<IAVService[]>) => {
        this.isLoading = false;
        this.aVServices = res.body ?? [];
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

  trackId(index: number, item: IAVService): number {
    return item.id!;
  }

  delete(aVService: IAVService): void {
    const modalRef = this.modalService.open(AVServiceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aVService = aVService;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
