import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAVService } from '../av-service.model';

@Component({
  selector: 'jhi-av-service-detail',
  templateUrl: './av-service-detail.component.html',
})
export class AVServiceDetailComponent implements OnInit {
  aVService: IAVService | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aVService }) => {
      this.aVService = aVService;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
