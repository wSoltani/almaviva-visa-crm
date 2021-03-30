jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonService } from '../service/person.service';

import { PersonComponent } from './person.component';

describe('Component Tests', () => {
  describe('Person Management Component', () => {
    let comp: PersonComponent;
    let fixture: ComponentFixture<PersonComponent>;
    let service: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(PersonComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PersonService);

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
      expect(comp.people?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
