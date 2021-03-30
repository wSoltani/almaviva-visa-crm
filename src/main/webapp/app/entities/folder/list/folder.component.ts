import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFolder } from '../folder.model';
import { FolderService } from '../service/folder.service';
import { FolderDeleteDialogComponent } from '../delete/folder-delete-dialog.component';

@Component({
  selector: 'jhi-folder',
  templateUrl: './folder.component.html',
})
export class FolderComponent implements OnInit {
  folders?: IFolder[];
  isLoading = false;
  currentSearch: string;

  constructor(protected folderService: FolderService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.folderService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IFolder[]>) => {
            this.isLoading = false;
            this.folders = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.folderService.query().subscribe(
      (res: HttpResponse<IFolder[]>) => {
        this.isLoading = false;
        this.folders = res.body ?? [];
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

  trackId(index: number, item: IFolder): number {
    return item.id!;
  }

  delete(folder: IFolder): void {
    const modalRef = this.modalService.open(FolderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.folder = folder;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
