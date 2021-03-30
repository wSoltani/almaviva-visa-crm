import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDayOff } from '../day-off.model';

@Component({
  selector: 'jhi-day-off-detail',
  templateUrl: './day-off-detail.component.html',
})
export class DayOffDetailComponent implements OnInit {
  dayOff: IDayOff | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dayOff }) => {
      this.dayOff = dayOff;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
