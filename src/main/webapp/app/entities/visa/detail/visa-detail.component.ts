import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVisa } from '../visa.model';

@Component({
  selector: 'jhi-visa-detail',
  templateUrl: './visa-detail.component.html',
})
export class VisaDetailComponent implements OnInit {
  visa: IVisa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visa }) => {
      this.visa = visa;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
