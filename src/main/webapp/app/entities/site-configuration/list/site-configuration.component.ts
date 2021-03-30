import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISiteConfiguration } from '../site-configuration.model';
import { SiteConfigurationService } from '../service/site-configuration.service';
import { SiteConfigurationDeleteDialogComponent } from '../delete/site-configuration-delete-dialog.component';

@Component({
  selector: 'jhi-site-configuration',
  templateUrl: './site-configuration.component.html',
})
export class SiteConfigurationComponent implements OnInit {
  siteConfigurations?: ISiteConfiguration[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected siteConfigurationService: SiteConfigurationService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.siteConfigurationService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<ISiteConfiguration[]>) => {
            this.isLoading = false;
            this.siteConfigurations = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.siteConfigurationService.query().subscribe(
      (res: HttpResponse<ISiteConfiguration[]>) => {
        this.isLoading = false;
        this.siteConfigurations = res.body ?? [];
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

  trackId(index: number, item: ISiteConfiguration): number {
    return item.id!;
  }

  delete(siteConfiguration: ISiteConfiguration): void {
    const modalRef = this.modalService.open(SiteConfigurationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.siteConfiguration = siteConfiguration;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
