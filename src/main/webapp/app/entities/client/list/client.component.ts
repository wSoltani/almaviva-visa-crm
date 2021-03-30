import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClient } from '../client.model';
import { ClientService } from '../service/client.service';
import { ClientDeleteDialogComponent } from '../delete/client-delete-dialog.component';

@Component({
  selector: 'jhi-client',
  templateUrl: './client.component.html',
})
export class ClientComponent implements OnInit {
  clients?: IClient[];
  isLoading = false;
  currentSearch: string;

  constructor(protected clientService: ClientService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.clientService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IClient[]>) => {
            this.isLoading = false;
            this.clients = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.clientService.query().subscribe(
      (res: HttpResponse<IClient[]>) => {
        this.isLoading = false;
        this.clients = res.body ?? [];
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

  trackId(index: number, item: IClient): number {
    return item.id!;
  }

  delete(client: IClient): void {
    const modalRef = this.modalService.open(ClientDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.client = client;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
