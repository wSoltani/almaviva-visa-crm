import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPerson, Person } from '../person.model';
import { PersonService } from '../service/person.service';
import { IFolder } from 'app/entities/folder/folder.model';
import { FolderService } from 'app/entities/folder/service/folder.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html',
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;

  foldersCollection: IFolder[] = [];

  editForm = this.fb.group({
    id: [],
    idMoyenSub: [],
    nomPrec: [],
    lieuNaissance: [],
    etatCivilWeb: [],
    nationPrec: [],
    nationActuel: [],
    nomAutor: [],
    prenomAutor: [],
    adresAutor: [],
    natiAutor: [],
    numCin: [],
    numDoc: [],
    dateDelivDoc: [],
    dateExpDoc: [],
    delivParDoc: [],
    adresseDomicile: [],
    adresseEmail: [],
    isResident: [],
    titreSejour: [],
    dateExpSejour: [],
    numTel: [],
    adressEmp: [],
    telEmp: [],
    nomEtab: [],
    adressEtablis: [],
    dureeSejour: [],
    etatMemDes: [],
    etatMemPremier: [],
    nombreEntre: [],
    oldVisaExiste: [],
    dateDelivDebut: [],
    dateDelivFin: [],
    isEmprDegit: [],
    dateEmpreint: [],
    dateDelivAutor: [],
    dateValideAtorDebut: [],
    dateValideAutorFin: [],
    dateArrivPrevu: [],
    dateDepartPrevu: [],
    descInvite: [],
    adresseInvit: [],
    telInvite: [],
    emailInvite: [],
    nomEntreprise: [],
    adresseEntreprise: [],
    contactEntreprise: [],
    descContactEntreprise: [],
    financeFraisVoyage: [],
    nomCit: [],
    prenomCit: [],
    dateNaissCit: [],
    nationCit: [],
    numDocCit: [],
    lieuForm: [],
    dateForm: [],
    descPhotoForm: [],
    descForm1: [],
    descForm2: [],
    emailComp: [],
    nomForm: [],
    prenomForm: [],
    idPaysForm: [],
    descForm3: [],
    descForm4: [],
    autreetatcivil: [],
    autredoc: [],
    autreobj: [],
    autrefris: [],
    descpromot: [],
    descinvit: [],
    isVisa: [],
    dateaut: [],
    datecreatefo: [],
    telForm: [],
    professionForm: [],
    referenceForm: [],
    otherNation: [],
    telAutori: [],
    emailAutori: [],
    numSejour: [],
    nomEmp: [],
    numEmp: [],
    villeDestination: [],
    infoObjeVoyage: [],
    autoDelivrePar: [],
    otherMoyen: [],
    otherLien: [],
    faxInvite: [],
    faxEntreprise: [],
    natActuel: [],
    natDiffer: [],
    natMineur: [],
    natCitoyen: [],
    moysub1: [],
    moysub2: [],
    moysub3: [],
    moysub4: [],
    moysub5: [],
    moysub8: [],
    moysubs1: [],
    moysubs5: [],
    moysubs6: [],
    moysubs7: [],
    moysubs8: [],
    autreFrais: [],
    etatCivil: [],
    idLienF: [],
    objPerson: [],
    idSexeAvs: [],
    typeDocV: [],
    typeFinance: [],
    deleted: [],
    folder: [],
  });

  constructor(
    protected personService: PersonService,
    protected folderService: FolderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.updateForm(person);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  trackFolderById(index: number, item: IFolder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      idMoyenSub: person.idMoyenSub,
      nomPrec: person.nomPrec,
      lieuNaissance: person.lieuNaissance,
      etatCivilWeb: person.etatCivilWeb,
      nationPrec: person.nationPrec,
      nationActuel: person.nationActuel,
      nomAutor: person.nomAutor,
      prenomAutor: person.prenomAutor,
      adresAutor: person.adresAutor,
      natiAutor: person.natiAutor,
      numCin: person.numCin,
      numDoc: person.numDoc,
      dateDelivDoc: person.dateDelivDoc,
      dateExpDoc: person.dateExpDoc,
      delivParDoc: person.delivParDoc,
      adresseDomicile: person.adresseDomicile,
      adresseEmail: person.adresseEmail,
      isResident: person.isResident,
      titreSejour: person.titreSejour,
      dateExpSejour: person.dateExpSejour,
      numTel: person.numTel,
      adressEmp: person.adressEmp,
      telEmp: person.telEmp,
      nomEtab: person.nomEtab,
      adressEtablis: person.adressEtablis,
      dureeSejour: person.dureeSejour,
      etatMemDes: person.etatMemDes,
      etatMemPremier: person.etatMemPremier,
      nombreEntre: person.nombreEntre,
      oldVisaExiste: person.oldVisaExiste,
      dateDelivDebut: person.dateDelivDebut,
      dateDelivFin: person.dateDelivFin,
      isEmprDegit: person.isEmprDegit,
      dateEmpreint: person.dateEmpreint,
      dateDelivAutor: person.dateDelivAutor,
      dateValideAtorDebut: person.dateValideAtorDebut,
      dateValideAutorFin: person.dateValideAutorFin,
      dateArrivPrevu: person.dateArrivPrevu,
      dateDepartPrevu: person.dateDepartPrevu,
      descInvite: person.descInvite,
      adresseInvit: person.adresseInvit,
      telInvite: person.telInvite,
      emailInvite: person.emailInvite,
      nomEntreprise: person.nomEntreprise,
      adresseEntreprise: person.adresseEntreprise,
      contactEntreprise: person.contactEntreprise,
      descContactEntreprise: person.descContactEntreprise,
      financeFraisVoyage: person.financeFraisVoyage,
      nomCit: person.nomCit,
      prenomCit: person.prenomCit,
      dateNaissCit: person.dateNaissCit,
      nationCit: person.nationCit,
      numDocCit: person.numDocCit,
      lieuForm: person.lieuForm,
      dateForm: person.dateForm,
      descPhotoForm: person.descPhotoForm,
      descForm1: person.descForm1,
      descForm2: person.descForm2,
      emailComp: person.emailComp,
      nomForm: person.nomForm,
      prenomForm: person.prenomForm,
      idPaysForm: person.idPaysForm,
      descForm3: person.descForm3,
      descForm4: person.descForm4,
      autreetatcivil: person.autreetatcivil,
      autredoc: person.autredoc,
      autreobj: person.autreobj,
      autrefris: person.autrefris,
      descpromot: person.descpromot,
      descinvit: person.descinvit,
      isVisa: person.isVisa,
      dateaut: person.dateaut,
      datecreatefo: person.datecreatefo,
      telForm: person.telForm,
      professionForm: person.professionForm,
      referenceForm: person.referenceForm,
      otherNation: person.otherNation,
      telAutori: person.telAutori,
      emailAutori: person.emailAutori,
      numSejour: person.numSejour,
      nomEmp: person.nomEmp,
      numEmp: person.numEmp,
      villeDestination: person.villeDestination,
      infoObjeVoyage: person.infoObjeVoyage,
      autoDelivrePar: person.autoDelivrePar,
      otherMoyen: person.otherMoyen,
      otherLien: person.otherLien,
      faxInvite: person.faxInvite,
      faxEntreprise: person.faxEntreprise,
      natActuel: person.natActuel,
      natDiffer: person.natDiffer,
      natMineur: person.natMineur,
      natCitoyen: person.natCitoyen,
      moysub1: person.moysub1,
      moysub2: person.moysub2,
      moysub3: person.moysub3,
      moysub4: person.moysub4,
      moysub5: person.moysub5,
      moysub8: person.moysub8,
      moysubs1: person.moysubs1,
      moysubs5: person.moysubs5,
      moysubs6: person.moysubs6,
      moysubs7: person.moysubs7,
      moysubs8: person.moysubs8,
      autreFrais: person.autreFrais,
      etatCivil: person.etatCivil,
      idLienF: person.idLienF,
      objPerson: person.objPerson,
      idSexeAvs: person.idSexeAvs,
      typeDocV: person.typeDocV,
      typeFinance: person.typeFinance,
      deleted: person.deleted,
      folder: person.folder,
    });

    this.foldersCollection = this.folderService.addFolderToCollectionIfMissing(this.foldersCollection, person.folder);
  }

  protected loadRelationshipsOptions(): void {
    this.folderService
      .query({ filter: 'person-is-null' })
      .pipe(map((res: HttpResponse<IFolder[]>) => res.body ?? []))
      .pipe(map((folders: IFolder[]) => this.folderService.addFolderToCollectionIfMissing(folders, this.editForm.get('folder')!.value)))
      .subscribe((folders: IFolder[]) => (this.foldersCollection = folders));
  }

  protected createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      idMoyenSub: this.editForm.get(['idMoyenSub'])!.value,
      nomPrec: this.editForm.get(['nomPrec'])!.value,
      lieuNaissance: this.editForm.get(['lieuNaissance'])!.value,
      etatCivilWeb: this.editForm.get(['etatCivilWeb'])!.value,
      nationPrec: this.editForm.get(['nationPrec'])!.value,
      nationActuel: this.editForm.get(['nationActuel'])!.value,
      nomAutor: this.editForm.get(['nomAutor'])!.value,
      prenomAutor: this.editForm.get(['prenomAutor'])!.value,
      adresAutor: this.editForm.get(['adresAutor'])!.value,
      natiAutor: this.editForm.get(['natiAutor'])!.value,
      numCin: this.editForm.get(['numCin'])!.value,
      numDoc: this.editForm.get(['numDoc'])!.value,
      dateDelivDoc: this.editForm.get(['dateDelivDoc'])!.value,
      dateExpDoc: this.editForm.get(['dateExpDoc'])!.value,
      delivParDoc: this.editForm.get(['delivParDoc'])!.value,
      adresseDomicile: this.editForm.get(['adresseDomicile'])!.value,
      adresseEmail: this.editForm.get(['adresseEmail'])!.value,
      isResident: this.editForm.get(['isResident'])!.value,
      titreSejour: this.editForm.get(['titreSejour'])!.value,
      dateExpSejour: this.editForm.get(['dateExpSejour'])!.value,
      numTel: this.editForm.get(['numTel'])!.value,
      adressEmp: this.editForm.get(['adressEmp'])!.value,
      telEmp: this.editForm.get(['telEmp'])!.value,
      nomEtab: this.editForm.get(['nomEtab'])!.value,
      adressEtablis: this.editForm.get(['adressEtablis'])!.value,
      dureeSejour: this.editForm.get(['dureeSejour'])!.value,
      etatMemDes: this.editForm.get(['etatMemDes'])!.value,
      etatMemPremier: this.editForm.get(['etatMemPremier'])!.value,
      nombreEntre: this.editForm.get(['nombreEntre'])!.value,
      oldVisaExiste: this.editForm.get(['oldVisaExiste'])!.value,
      dateDelivDebut: this.editForm.get(['dateDelivDebut'])!.value,
      dateDelivFin: this.editForm.get(['dateDelivFin'])!.value,
      isEmprDegit: this.editForm.get(['isEmprDegit'])!.value,
      dateEmpreint: this.editForm.get(['dateEmpreint'])!.value,
      dateDelivAutor: this.editForm.get(['dateDelivAutor'])!.value,
      dateValideAtorDebut: this.editForm.get(['dateValideAtorDebut'])!.value,
      dateValideAutorFin: this.editForm.get(['dateValideAutorFin'])!.value,
      dateArrivPrevu: this.editForm.get(['dateArrivPrevu'])!.value,
      dateDepartPrevu: this.editForm.get(['dateDepartPrevu'])!.value,
      descInvite: this.editForm.get(['descInvite'])!.value,
      adresseInvit: this.editForm.get(['adresseInvit'])!.value,
      telInvite: this.editForm.get(['telInvite'])!.value,
      emailInvite: this.editForm.get(['emailInvite'])!.value,
      nomEntreprise: this.editForm.get(['nomEntreprise'])!.value,
      adresseEntreprise: this.editForm.get(['adresseEntreprise'])!.value,
      contactEntreprise: this.editForm.get(['contactEntreprise'])!.value,
      descContactEntreprise: this.editForm.get(['descContactEntreprise'])!.value,
      financeFraisVoyage: this.editForm.get(['financeFraisVoyage'])!.value,
      nomCit: this.editForm.get(['nomCit'])!.value,
      prenomCit: this.editForm.get(['prenomCit'])!.value,
      dateNaissCit: this.editForm.get(['dateNaissCit'])!.value,
      nationCit: this.editForm.get(['nationCit'])!.value,
      numDocCit: this.editForm.get(['numDocCit'])!.value,
      lieuForm: this.editForm.get(['lieuForm'])!.value,
      dateForm: this.editForm.get(['dateForm'])!.value,
      descPhotoForm: this.editForm.get(['descPhotoForm'])!.value,
      descForm1: this.editForm.get(['descForm1'])!.value,
      descForm2: this.editForm.get(['descForm2'])!.value,
      emailComp: this.editForm.get(['emailComp'])!.value,
      nomForm: this.editForm.get(['nomForm'])!.value,
      prenomForm: this.editForm.get(['prenomForm'])!.value,
      idPaysForm: this.editForm.get(['idPaysForm'])!.value,
      descForm3: this.editForm.get(['descForm3'])!.value,
      descForm4: this.editForm.get(['descForm4'])!.value,
      autreetatcivil: this.editForm.get(['autreetatcivil'])!.value,
      autredoc: this.editForm.get(['autredoc'])!.value,
      autreobj: this.editForm.get(['autreobj'])!.value,
      autrefris: this.editForm.get(['autrefris'])!.value,
      descpromot: this.editForm.get(['descpromot'])!.value,
      descinvit: this.editForm.get(['descinvit'])!.value,
      isVisa: this.editForm.get(['isVisa'])!.value,
      dateaut: this.editForm.get(['dateaut'])!.value,
      datecreatefo: this.editForm.get(['datecreatefo'])!.value,
      telForm: this.editForm.get(['telForm'])!.value,
      professionForm: this.editForm.get(['professionForm'])!.value,
      referenceForm: this.editForm.get(['referenceForm'])!.value,
      otherNation: this.editForm.get(['otherNation'])!.value,
      telAutori: this.editForm.get(['telAutori'])!.value,
      emailAutori: this.editForm.get(['emailAutori'])!.value,
      numSejour: this.editForm.get(['numSejour'])!.value,
      nomEmp: this.editForm.get(['nomEmp'])!.value,
      numEmp: this.editForm.get(['numEmp'])!.value,
      villeDestination: this.editForm.get(['villeDestination'])!.value,
      infoObjeVoyage: this.editForm.get(['infoObjeVoyage'])!.value,
      autoDelivrePar: this.editForm.get(['autoDelivrePar'])!.value,
      otherMoyen: this.editForm.get(['otherMoyen'])!.value,
      otherLien: this.editForm.get(['otherLien'])!.value,
      faxInvite: this.editForm.get(['faxInvite'])!.value,
      faxEntreprise: this.editForm.get(['faxEntreprise'])!.value,
      natActuel: this.editForm.get(['natActuel'])!.value,
      natDiffer: this.editForm.get(['natDiffer'])!.value,
      natMineur: this.editForm.get(['natMineur'])!.value,
      natCitoyen: this.editForm.get(['natCitoyen'])!.value,
      moysub1: this.editForm.get(['moysub1'])!.value,
      moysub2: this.editForm.get(['moysub2'])!.value,
      moysub3: this.editForm.get(['moysub3'])!.value,
      moysub4: this.editForm.get(['moysub4'])!.value,
      moysub5: this.editForm.get(['moysub5'])!.value,
      moysub8: this.editForm.get(['moysub8'])!.value,
      moysubs1: this.editForm.get(['moysubs1'])!.value,
      moysubs5: this.editForm.get(['moysubs5'])!.value,
      moysubs6: this.editForm.get(['moysubs6'])!.value,
      moysubs7: this.editForm.get(['moysubs7'])!.value,
      moysubs8: this.editForm.get(['moysubs8'])!.value,
      autreFrais: this.editForm.get(['autreFrais'])!.value,
      etatCivil: this.editForm.get(['etatCivil'])!.value,
      idLienF: this.editForm.get(['idLienF'])!.value,
      objPerson: this.editForm.get(['objPerson'])!.value,
      idSexeAvs: this.editForm.get(['idSexeAvs'])!.value,
      typeDocV: this.editForm.get(['typeDocV'])!.value,
      typeFinance: this.editForm.get(['typeFinance'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      folder: this.editForm.get(['folder'])!.value,
    };
  }
}
