import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPhoneActivation } from '../phone-activation.model';
import { PhoneActivationService } from '../service/phone-activation.service';
import { PhoneActivationDeleteDialogComponent } from '../delete/phone-activation-delete-dialog.component';

@Component({
  selector: 'jhi-phone-activation',
  templateUrl: './phone-activation.component.html',
})
export class PhoneActivationComponent implements OnInit {
  phoneActivations?: IPhoneActivation[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected phoneActivationService: PhoneActivationService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.phoneActivationService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IPhoneActivation[]>) => {
            this.isLoading = false;
            this.phoneActivations = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.phoneActivationService.query().subscribe(
      (res: HttpResponse<IPhoneActivation[]>) => {
        this.isLoading = false;
        this.phoneActivations = res.body ?? [];
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

  trackId(index: number, item: IPhoneActivation): number {
    return item.id!;
  }

  delete(phoneActivation: IPhoneActivation): void {
    const modalRef = this.modalService.open(PhoneActivationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.phoneActivation = phoneActivation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
