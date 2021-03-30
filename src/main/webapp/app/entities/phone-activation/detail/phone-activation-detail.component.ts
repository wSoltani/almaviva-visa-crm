import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPhoneActivation } from '../phone-activation.model';

@Component({
  selector: 'jhi-phone-activation-detail',
  templateUrl: './phone-activation-detail.component.html',
})
export class PhoneActivationDetailComponent implements OnInit {
  phoneActivation: IPhoneActivation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ phoneActivation }) => {
      this.phoneActivation = phoneActivation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
