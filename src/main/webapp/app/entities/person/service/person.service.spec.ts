import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPerson, Person } from '../person.model';

import { PersonService } from './person.service';

describe('Service Tests', () => {
  describe('Person Service', () => {
    let service: PersonService;
    let httpMock: HttpTestingController;
    let elemDefault: IPerson;
    let expectedResult: IPerson | IPerson[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        idMoyenSub: 0,
        nomPrec: 'AAAAAAA',
        lieuNaissance: 'AAAAAAA',
        etatCivilWeb: 0,
        nationPrec: 0,
        nationActuel: 0,
        nomAutor: 'AAAAAAA',
        prenomAutor: 'AAAAAAA',
        adresAutor: 'AAAAAAA',
        natiAutor: 0,
        numCin: 'AAAAAAA',
        numDoc: 'AAAAAAA',
        dateDelivDoc: currentDate,
        dateExpDoc: currentDate,
        delivParDoc: 'AAAAAAA',
        adresseDomicile: 'AAAAAAA',
        adresseEmail: 'AAAAAAA',
        isResident: false,
        titreSejour: 'AAAAAAA',
        dateExpSejour: currentDate,
        numTel: 'AAAAAAA',
        adressEmp: 'AAAAAAA',
        telEmp: 'AAAAAAA',
        nomEtab: 'AAAAAAA',
        adressEtablis: 'AAAAAAA',
        dureeSejour: 0,
        etatMemDes: 'AAAAAAA',
        etatMemPremier: 'AAAAAAA',
        nombreEntre: 'AAAAAAA',
        oldVisaExiste: false,
        dateDelivDebut: currentDate,
        dateDelivFin: currentDate,
        isEmprDegit: false,
        dateEmpreint: currentDate,
        dateDelivAutor: currentDate,
        dateValideAtorDebut: currentDate,
        dateValideAutorFin: currentDate,
        dateArrivPrevu: currentDate,
        dateDepartPrevu: currentDate,
        descInvite: 'AAAAAAA',
        adresseInvit: 'AAAAAAA',
        telInvite: 'AAAAAAA',
        emailInvite: 'AAAAAAA',
        nomEntreprise: 'AAAAAAA',
        adresseEntreprise: 'AAAAAAA',
        contactEntreprise: 'AAAAAAA',
        descContactEntreprise: 'AAAAAAA',
        financeFraisVoyage: 'AAAAAAA',
        nomCit: 'AAAAAAA',
        prenomCit: 'AAAAAAA',
        dateNaissCit: currentDate,
        nationCit: 0,
        numDocCit: 'AAAAAAA',
        lieuForm: 'AAAAAAA',
        dateForm: currentDate,
        descPhotoForm: 'AAAAAAA',
        descForm1: 'AAAAAAA',
        descForm2: 'AAAAAAA',
        emailComp: 'AAAAAAA',
        nomForm: 'AAAAAAA',
        prenomForm: 'AAAAAAA',
        idPaysForm: 'AAAAAAA',
        descForm3: 'AAAAAAA',
        descForm4: 'AAAAAAA',
        autreetatcivil: 'AAAAAAA',
        autredoc: 'AAAAAAA',
        autreobj: 'AAAAAAA',
        autrefris: 'AAAAAAA',
        descpromot: 'AAAAAAA',
        descinvit: 'AAAAAAA',
        isVisa: false,
        dateaut: currentDate,
        datecreatefo: currentDate,
        telForm: 'AAAAAAA',
        professionForm: 'AAAAAAA',
        referenceForm: 'AAAAAAA',
        otherNation: 'AAAAAAA',
        telAutori: 'AAAAAAA',
        emailAutori: 'AAAAAAA',
        numSejour: 'AAAAAAA',
        nomEmp: 'AAAAAAA',
        numEmp: 'AAAAAAA',
        villeDestination: 'AAAAAAA',
        infoObjeVoyage: 'AAAAAAA',
        autoDelivrePar: 'AAAAAAA',
        otherMoyen: 'AAAAAAA',
        otherLien: 'AAAAAAA',
        faxInvite: 'AAAAAAA',
        faxEntreprise: 'AAAAAAA',
        natActuel: 'AAAAAAA',
        natDiffer: 'AAAAAAA',
        natMineur: 'AAAAAAA',
        natCitoyen: 'AAAAAAA',
        moysub1: false,
        moysub2: false,
        moysub3: false,
        moysub4: false,
        moysub5: false,
        moysub8: false,
        moysubs1: false,
        moysubs5: false,
        moysubs6: false,
        moysubs7: false,
        moysubs8: false,
        autreFrais: 'AAAAAAA',
        etatCivil: 0,
        idLienF: 0,
        objPerson: 0,
        idSexeAvs: 0,
        typeDocV: 0,
        typeFinance: 0,
        deleted: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateDelivDoc: currentDate.format(DATE_FORMAT),
            dateExpDoc: currentDate.format(DATE_FORMAT),
            dateExpSejour: currentDate.format(DATE_FORMAT),
            dateDelivDebut: currentDate.format(DATE_FORMAT),
            dateDelivFin: currentDate.format(DATE_FORMAT),
            dateEmpreint: currentDate.format(DATE_FORMAT),
            dateDelivAutor: currentDate.format(DATE_FORMAT),
            dateValideAtorDebut: currentDate.format(DATE_FORMAT),
            dateValideAutorFin: currentDate.format(DATE_FORMAT),
            dateArrivPrevu: currentDate.format(DATE_FORMAT),
            dateDepartPrevu: currentDate.format(DATE_FORMAT),
            dateNaissCit: currentDate.format(DATE_FORMAT),
            dateForm: currentDate.format(DATE_FORMAT),
            dateaut: currentDate.format(DATE_FORMAT),
            datecreatefo: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Person', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDelivDoc: currentDate.format(DATE_FORMAT),
            dateExpDoc: currentDate.format(DATE_FORMAT),
            dateExpSejour: currentDate.format(DATE_FORMAT),
            dateDelivDebut: currentDate.format(DATE_FORMAT),
            dateDelivFin: currentDate.format(DATE_FORMAT),
            dateEmpreint: currentDate.format(DATE_FORMAT),
            dateDelivAutor: currentDate.format(DATE_FORMAT),
            dateValideAtorDebut: currentDate.format(DATE_FORMAT),
            dateValideAutorFin: currentDate.format(DATE_FORMAT),
            dateArrivPrevu: currentDate.format(DATE_FORMAT),
            dateDepartPrevu: currentDate.format(DATE_FORMAT),
            dateNaissCit: currentDate.format(DATE_FORMAT),
            dateForm: currentDate.format(DATE_FORMAT),
            dateaut: currentDate.format(DATE_FORMAT),
            datecreatefo: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDelivDoc: currentDate,
            dateExpDoc: currentDate,
            dateExpSejour: currentDate,
            dateDelivDebut: currentDate,
            dateDelivFin: currentDate,
            dateEmpreint: currentDate,
            dateDelivAutor: currentDate,
            dateValideAtorDebut: currentDate,
            dateValideAutorFin: currentDate,
            dateArrivPrevu: currentDate,
            dateDepartPrevu: currentDate,
            dateNaissCit: currentDate,
            dateForm: currentDate,
            dateaut: currentDate,
            datecreatefo: currentDate,
          },
          returnedFromService
        );

        service.create(new Person()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Person', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idMoyenSub: 1,
            nomPrec: 'BBBBBB',
            lieuNaissance: 'BBBBBB',
            etatCivilWeb: 1,
            nationPrec: 1,
            nationActuel: 1,
            nomAutor: 'BBBBBB',
            prenomAutor: 'BBBBBB',
            adresAutor: 'BBBBBB',
            natiAutor: 1,
            numCin: 'BBBBBB',
            numDoc: 'BBBBBB',
            dateDelivDoc: currentDate.format(DATE_FORMAT),
            dateExpDoc: currentDate.format(DATE_FORMAT),
            delivParDoc: 'BBBBBB',
            adresseDomicile: 'BBBBBB',
            adresseEmail: 'BBBBBB',
            isResident: true,
            titreSejour: 'BBBBBB',
            dateExpSejour: currentDate.format(DATE_FORMAT),
            numTel: 'BBBBBB',
            adressEmp: 'BBBBBB',
            telEmp: 'BBBBBB',
            nomEtab: 'BBBBBB',
            adressEtablis: 'BBBBBB',
            dureeSejour: 1,
            etatMemDes: 'BBBBBB',
            etatMemPremier: 'BBBBBB',
            nombreEntre: 'BBBBBB',
            oldVisaExiste: true,
            dateDelivDebut: currentDate.format(DATE_FORMAT),
            dateDelivFin: currentDate.format(DATE_FORMAT),
            isEmprDegit: true,
            dateEmpreint: currentDate.format(DATE_FORMAT),
            dateDelivAutor: currentDate.format(DATE_FORMAT),
            dateValideAtorDebut: currentDate.format(DATE_FORMAT),
            dateValideAutorFin: currentDate.format(DATE_FORMAT),
            dateArrivPrevu: currentDate.format(DATE_FORMAT),
            dateDepartPrevu: currentDate.format(DATE_FORMAT),
            descInvite: 'BBBBBB',
            adresseInvit: 'BBBBBB',
            telInvite: 'BBBBBB',
            emailInvite: 'BBBBBB',
            nomEntreprise: 'BBBBBB',
            adresseEntreprise: 'BBBBBB',
            contactEntreprise: 'BBBBBB',
            descContactEntreprise: 'BBBBBB',
            financeFraisVoyage: 'BBBBBB',
            nomCit: 'BBBBBB',
            prenomCit: 'BBBBBB',
            dateNaissCit: currentDate.format(DATE_FORMAT),
            nationCit: 1,
            numDocCit: 'BBBBBB',
            lieuForm: 'BBBBBB',
            dateForm: currentDate.format(DATE_FORMAT),
            descPhotoForm: 'BBBBBB',
            descForm1: 'BBBBBB',
            descForm2: 'BBBBBB',
            emailComp: 'BBBBBB',
            nomForm: 'BBBBBB',
            prenomForm: 'BBBBBB',
            idPaysForm: 'BBBBBB',
            descForm3: 'BBBBBB',
            descForm4: 'BBBBBB',
            autreetatcivil: 'BBBBBB',
            autredoc: 'BBBBBB',
            autreobj: 'BBBBBB',
            autrefris: 'BBBBBB',
            descpromot: 'BBBBBB',
            descinvit: 'BBBBBB',
            isVisa: true,
            dateaut: currentDate.format(DATE_FORMAT),
            datecreatefo: currentDate.format(DATE_FORMAT),
            telForm: 'BBBBBB',
            professionForm: 'BBBBBB',
            referenceForm: 'BBBBBB',
            otherNation: 'BBBBBB',
            telAutori: 'BBBBBB',
            emailAutori: 'BBBBBB',
            numSejour: 'BBBBBB',
            nomEmp: 'BBBBBB',
            numEmp: 'BBBBBB',
            villeDestination: 'BBBBBB',
            infoObjeVoyage: 'BBBBBB',
            autoDelivrePar: 'BBBBBB',
            otherMoyen: 'BBBBBB',
            otherLien: 'BBBBBB',
            faxInvite: 'BBBBBB',
            faxEntreprise: 'BBBBBB',
            natActuel: 'BBBBBB',
            natDiffer: 'BBBBBB',
            natMineur: 'BBBBBB',
            natCitoyen: 'BBBBBB',
            moysub1: true,
            moysub2: true,
            moysub3: true,
            moysub4: true,
            moysub5: true,
            moysub8: true,
            moysubs1: true,
            moysubs5: true,
            moysubs6: true,
            moysubs7: true,
            moysubs8: true,
            autreFrais: 'BBBBBB',
            etatCivil: 1,
            idLienF: 1,
            objPerson: 1,
            idSexeAvs: 1,
            typeDocV: 1,
            typeFinance: 1,
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDelivDoc: currentDate,
            dateExpDoc: currentDate,
            dateExpSejour: currentDate,
            dateDelivDebut: currentDate,
            dateDelivFin: currentDate,
            dateEmpreint: currentDate,
            dateDelivAutor: currentDate,
            dateValideAtorDebut: currentDate,
            dateValideAutorFin: currentDate,
            dateArrivPrevu: currentDate,
            dateDepartPrevu: currentDate,
            dateNaissCit: currentDate,
            dateForm: currentDate,
            dateaut: currentDate,
            datecreatefo: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Person', () => {
        const patchObject = Object.assign(
          {
            nomPrec: 'BBBBBB',
            etatCivilWeb: 1,
            adresAutor: 'BBBBBB',
            natiAutor: 1,
            numCin: 'BBBBBB',
            dateExpDoc: currentDate.format(DATE_FORMAT),
            adresseDomicile: 'BBBBBB',
            numTel: 'BBBBBB',
            etatMemDes: 'BBBBBB',
            isEmprDegit: true,
            dateEmpreint: currentDate.format(DATE_FORMAT),
            dateValideAutorFin: currentDate.format(DATE_FORMAT),
            dateArrivPrevu: currentDate.format(DATE_FORMAT),
            dateDepartPrevu: currentDate.format(DATE_FORMAT),
            adresseInvit: 'BBBBBB',
            nomEntreprise: 'BBBBBB',
            contactEntreprise: 'BBBBBB',
            descContactEntreprise: 'BBBBBB',
            financeFraisVoyage: 'BBBBBB',
            nomCit: 'BBBBBB',
            prenomCit: 'BBBBBB',
            dateNaissCit: currentDate.format(DATE_FORMAT),
            nationCit: 1,
            lieuForm: 'BBBBBB',
            descPhotoForm: 'BBBBBB',
            descForm2: 'BBBBBB',
            nomForm: 'BBBBBB',
            descForm3: 'BBBBBB',
            descForm4: 'BBBBBB',
            autreetatcivil: 'BBBBBB',
            autredoc: 'BBBBBB',
            descpromot: 'BBBBBB',
            descinvit: 'BBBBBB',
            dateaut: currentDate.format(DATE_FORMAT),
            professionForm: 'BBBBBB',
            numSejour: 'BBBBBB',
            nomEmp: 'BBBBBB',
            numEmp: 'BBBBBB',
            infoObjeVoyage: 'BBBBBB',
            autoDelivrePar: 'BBBBBB',
            otherMoyen: 'BBBBBB',
            otherLien: 'BBBBBB',
            faxEntreprise: 'BBBBBB',
            natActuel: 'BBBBBB',
            natMineur: 'BBBBBB',
            moysub2: true,
            moysub3: true,
            moysub5: true,
            moysubs6: true,
            autreFrais: 'BBBBBB',
            etatCivil: 1,
            idSexeAvs: 1,
            typeDocV: 1,
            typeFinance: 1,
            deleted: true,
          },
          new Person()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateDelivDoc: currentDate,
            dateExpDoc: currentDate,
            dateExpSejour: currentDate,
            dateDelivDebut: currentDate,
            dateDelivFin: currentDate,
            dateEmpreint: currentDate,
            dateDelivAutor: currentDate,
            dateValideAtorDebut: currentDate,
            dateValideAutorFin: currentDate,
            dateArrivPrevu: currentDate,
            dateDepartPrevu: currentDate,
            dateNaissCit: currentDate,
            dateForm: currentDate,
            dateaut: currentDate,
            datecreatefo: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Person', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            idMoyenSub: 1,
            nomPrec: 'BBBBBB',
            lieuNaissance: 'BBBBBB',
            etatCivilWeb: 1,
            nationPrec: 1,
            nationActuel: 1,
            nomAutor: 'BBBBBB',
            prenomAutor: 'BBBBBB',
            adresAutor: 'BBBBBB',
            natiAutor: 1,
            numCin: 'BBBBBB',
            numDoc: 'BBBBBB',
            dateDelivDoc: currentDate.format(DATE_FORMAT),
            dateExpDoc: currentDate.format(DATE_FORMAT),
            delivParDoc: 'BBBBBB',
            adresseDomicile: 'BBBBBB',
            adresseEmail: 'BBBBBB',
            isResident: true,
            titreSejour: 'BBBBBB',
            dateExpSejour: currentDate.format(DATE_FORMAT),
            numTel: 'BBBBBB',
            adressEmp: 'BBBBBB',
            telEmp: 'BBBBBB',
            nomEtab: 'BBBBBB',
            adressEtablis: 'BBBBBB',
            dureeSejour: 1,
            etatMemDes: 'BBBBBB',
            etatMemPremier: 'BBBBBB',
            nombreEntre: 'BBBBBB',
            oldVisaExiste: true,
            dateDelivDebut: currentDate.format(DATE_FORMAT),
            dateDelivFin: currentDate.format(DATE_FORMAT),
            isEmprDegit: true,
            dateEmpreint: currentDate.format(DATE_FORMAT),
            dateDelivAutor: currentDate.format(DATE_FORMAT),
            dateValideAtorDebut: currentDate.format(DATE_FORMAT),
            dateValideAutorFin: currentDate.format(DATE_FORMAT),
            dateArrivPrevu: currentDate.format(DATE_FORMAT),
            dateDepartPrevu: currentDate.format(DATE_FORMAT),
            descInvite: 'BBBBBB',
            adresseInvit: 'BBBBBB',
            telInvite: 'BBBBBB',
            emailInvite: 'BBBBBB',
            nomEntreprise: 'BBBBBB',
            adresseEntreprise: 'BBBBBB',
            contactEntreprise: 'BBBBBB',
            descContactEntreprise: 'BBBBBB',
            financeFraisVoyage: 'BBBBBB',
            nomCit: 'BBBBBB',
            prenomCit: 'BBBBBB',
            dateNaissCit: currentDate.format(DATE_FORMAT),
            nationCit: 1,
            numDocCit: 'BBBBBB',
            lieuForm: 'BBBBBB',
            dateForm: currentDate.format(DATE_FORMAT),
            descPhotoForm: 'BBBBBB',
            descForm1: 'BBBBBB',
            descForm2: 'BBBBBB',
            emailComp: 'BBBBBB',
            nomForm: 'BBBBBB',
            prenomForm: 'BBBBBB',
            idPaysForm: 'BBBBBB',
            descForm3: 'BBBBBB',
            descForm4: 'BBBBBB',
            autreetatcivil: 'BBBBBB',
            autredoc: 'BBBBBB',
            autreobj: 'BBBBBB',
            autrefris: 'BBBBBB',
            descpromot: 'BBBBBB',
            descinvit: 'BBBBBB',
            isVisa: true,
            dateaut: currentDate.format(DATE_FORMAT),
            datecreatefo: currentDate.format(DATE_FORMAT),
            telForm: 'BBBBBB',
            professionForm: 'BBBBBB',
            referenceForm: 'BBBBBB',
            otherNation: 'BBBBBB',
            telAutori: 'BBBBBB',
            emailAutori: 'BBBBBB',
            numSejour: 'BBBBBB',
            nomEmp: 'BBBBBB',
            numEmp: 'BBBBBB',
            villeDestination: 'BBBBBB',
            infoObjeVoyage: 'BBBBBB',
            autoDelivrePar: 'BBBBBB',
            otherMoyen: 'BBBBBB',
            otherLien: 'BBBBBB',
            faxInvite: 'BBBBBB',
            faxEntreprise: 'BBBBBB',
            natActuel: 'BBBBBB',
            natDiffer: 'BBBBBB',
            natMineur: 'BBBBBB',
            natCitoyen: 'BBBBBB',
            moysub1: true,
            moysub2: true,
            moysub3: true,
            moysub4: true,
            moysub5: true,
            moysub8: true,
            moysubs1: true,
            moysubs5: true,
            moysubs6: true,
            moysubs7: true,
            moysubs8: true,
            autreFrais: 'BBBBBB',
            etatCivil: 1,
            idLienF: 1,
            objPerson: 1,
            idSexeAvs: 1,
            typeDocV: 1,
            typeFinance: 1,
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDelivDoc: currentDate,
            dateExpDoc: currentDate,
            dateExpSejour: currentDate,
            dateDelivDebut: currentDate,
            dateDelivFin: currentDate,
            dateEmpreint: currentDate,
            dateDelivAutor: currentDate,
            dateValideAtorDebut: currentDate,
            dateValideAutorFin: currentDate,
            dateArrivPrevu: currentDate,
            dateDepartPrevu: currentDate,
            dateNaissCit: currentDate,
            dateForm: currentDate,
            dateaut: currentDate,
            datecreatefo: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Person', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonToCollectionIfMissing', () => {
        it('should add a Person to an empty array', () => {
          const person: IPerson = { id: 123 };
          expectedResult = service.addPersonToCollectionIfMissing([], person);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(person);
        });

        it('should not add a Person to an array that contains it', () => {
          const person: IPerson = { id: 123 };
          const personCollection: IPerson[] = [
            {
              ...person,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonToCollectionIfMissing(personCollection, person);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Person to an array that doesn't contain it", () => {
          const person: IPerson = { id: 123 };
          const personCollection: IPerson[] = [{ id: 456 }];
          expectedResult = service.addPersonToCollectionIfMissing(personCollection, person);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(person);
        });

        it('should add only unique Person to an array', () => {
          const personArray: IPerson[] = [{ id: 123 }, { id: 456 }, { id: 26005 }];
          const personCollection: IPerson[] = [{ id: 123 }];
          expectedResult = service.addPersonToCollectionIfMissing(personCollection, ...personArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const person: IPerson = { id: 123 };
          const person2: IPerson = { id: 456 };
          expectedResult = service.addPersonToCollectionIfMissing([], person, person2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(person);
          expect(expectedResult).toContain(person2);
        });

        it('should accept null and undefined values', () => {
          const person: IPerson = { id: 123 };
          expectedResult = service.addPersonToCollectionIfMissing([], null, person, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(person);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
