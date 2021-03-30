import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmailActivation } from '../email-activation.model';

@Component({
  selector: 'jhi-email-activation-detail',
  templateUrl: './email-activation-detail.component.html',
})
export class EmailActivationDetailComponent implements OnInit {
  emailActivation: IEmailActivation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emailActivation }) => {
      this.emailActivation = emailActivation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
