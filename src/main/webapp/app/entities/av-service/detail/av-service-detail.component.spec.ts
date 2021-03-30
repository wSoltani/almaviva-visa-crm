import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AVServiceDetailComponent } from './av-service-detail.component';

describe('Component Tests', () => {
  describe('AVService Management Detail Component', () => {
    let comp: AVServiceDetailComponent;
    let fixture: ComponentFixture<AVServiceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AVServiceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ aVService: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AVServiceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AVServiceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load aVService on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.aVService).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
