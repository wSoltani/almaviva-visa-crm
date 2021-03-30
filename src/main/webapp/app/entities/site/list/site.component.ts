import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISite } from '../site.model';
import { SiteService } from '../service/site.service';
import { SiteDeleteDialogComponent } from '../delete/site-delete-dialog.component';

@Component({
  selector: 'jhi-site',
  templateUrl: './site.component.html',
})
export class SiteComponent implements OnInit {
  sites?: ISite[];
  isLoading = false;
  currentSearch: string;

  constructor(protected siteService: SiteService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.siteService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<ISite[]>) => {
            this.isLoading = false;
            this.sites = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.siteService.query().subscribe(
      (res: HttpResponse<ISite[]>) => {
        this.isLoading = false;
        this.sites = res.body ?? [];
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

  trackId(index: number, item: ISite): number {
    return item.id!;
  }

  delete(site: ISite): void {
    const modalRef = this.modalService.open(SiteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.site = site;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
