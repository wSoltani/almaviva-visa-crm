jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DayOffService } from '../service/day-off.service';

import { DayOffComponent } from './day-off.component';

describe('Component Tests', () => {
  describe('DayOff Management Component', () => {
    let comp: DayOffComponent;
    let fixture: ComponentFixture<DayOffComponent>;
    let service: DayOffService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DayOffComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(DayOffComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DayOffComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DayOffService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.dayOffs?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
