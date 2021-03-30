import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISiteConfiguration } from '../site-configuration.model';

@Component({
  selector: 'jhi-site-configuration-detail',
  templateUrl: './site-configuration-detail.component.html',
})
export class SiteConfigurationDetailComponent implements OnInit {
  siteConfiguration: ISiteConfiguration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ siteConfiguration }) => {
      this.siteConfiguration = siteConfiguration;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
