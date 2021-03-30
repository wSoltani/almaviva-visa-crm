import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmailActivation } from '../email-activation.model';
import { EmailActivationService } from '../service/email-activation.service';
import { EmailActivationDeleteDialogComponent } from '../delete/email-activation-delete-dialog.component';

@Component({
  selector: 'jhi-email-activation',
  templateUrl: './email-activation.component.html',
})
export class EmailActivationComponent implements OnInit {
  emailActivations?: IEmailActivation[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected emailActivationService: EmailActivationService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.emailActivationService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IEmailActivation[]>) => {
            this.isLoading = false;
            this.emailActivations = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.emailActivationService.query().subscribe(
      (res: HttpResponse<IEmailActivation[]>) => {
        this.isLoading = false;
        this.emailActivations = res.body ?? [];
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

  trackId(index: number, item: IEmailActivation): number {
    return item.id!;
  }

  delete(emailActivation: IEmailActivation): void {
    const modalRef = this.modalService.open(EmailActivationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.emailActivation = emailActivation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
