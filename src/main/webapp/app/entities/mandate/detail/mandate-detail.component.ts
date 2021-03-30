import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMandate } from '../mandate.model';

@Component({
  selector: 'jhi-mandate-detail',
  templateUrl: './mandate-detail.component.html',
})
export class MandateDetailComponent implements OnInit {
  mandate: IMandate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mandate }) => {
      this.mandate = mandate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
