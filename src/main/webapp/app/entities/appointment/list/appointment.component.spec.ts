jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppointmentService } from '../service/appointment.service';

import { AppointmentComponent } from './appointment.component';

describe('Component Tests', () => {
  describe('Appointment Management Component', () => {
    let comp: AppointmentComponent;
    let fixture: ComponentFixture<AppointmentComponent>;
    let service: AppointmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AppointmentComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(AppointmentComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AppointmentComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AppointmentService);

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
      expect(comp.appointments?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
