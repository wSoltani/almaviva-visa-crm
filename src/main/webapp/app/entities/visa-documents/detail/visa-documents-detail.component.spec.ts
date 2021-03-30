import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VisaDocumentsDetailComponent } from './visa-documents-detail.component';

describe('Component Tests', () => {
  describe('VisaDocuments Management Detail Component', () => {
    let comp: VisaDocumentsDetailComponent;
    let fixture: ComponentFixture<VisaDocumentsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [VisaDocumentsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ visaDocuments: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(VisaDocumentsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VisaDocumentsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load visaDocuments on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.visaDocuments).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
