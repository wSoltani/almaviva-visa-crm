import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDayOff } from '../day-off.model';
import { DayOffService } from '../service/day-off.service';
import { DayOffDeleteDialogComponent } from '../delete/day-off-delete-dialog.component';

@Component({
  selector: 'jhi-day-off',
  templateUrl: './day-off.component.html',
})
export class DayOffComponent implements OnInit {
  dayOffs?: IDayOff[];
  isLoading = false;
  currentSearch: string;

  constructor(protected dayOffService: DayOffService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.dayOffService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IDayOff[]>) => {
            this.isLoading = false;
            this.dayOffs = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.dayOffService.query().subscribe(
      (res: HttpResponse<IDayOff[]>) => {
        this.isLoading = false;
        this.dayOffs = res.body ?? [];
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

  trackId(index: number, item: IDayOff): number {
    return item.id!;
  }

  delete(dayOff: IDayOff): void {
    const modalRef = this.modalService.open(DayOffDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dayOff = dayOff;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
