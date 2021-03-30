import * as dayjs from 'dayjs';
import { IFolder } from 'app/entities/folder/folder.model';

export interface IPerson {
  id?: number;
  idMoyenSub?: number | null;
  nomPrec?: string | null;
  lieuNaissance?: string | null;
  etatCivilWeb?: number | null;
  nationPrec?: number | null;
  nationActuel?: number | null;
  nomAutor?: string | null;
  prenomAutor?: string | null;
  adresAutor?: string | null;
  natiAutor?: number | null;
  numCin?: string | null;
  numDoc?: string | null;
  dateDelivDoc?: dayjs.Dayjs | null;
  dateExpDoc?: dayjs.Dayjs | null;
  delivParDoc?: string | null;
  adresseDomicile?: string | null;
  adresseEmail?: string | null;
  isResident?: boolean | null;
  titreSejour?: string | null;
  dateExpSejour?: dayjs.Dayjs | null;
  numTel?: string | null;
  adressEmp?: string | null;
  telEmp?: string | null;
  nomEtab?: string | null;
  adressEtablis?: string | null;
  dureeSejour?: number | null;
  etatMemDes?: string | null;
  etatMemPremier?: string | null;
  nombreEntre?: string | null;
  oldVisaExiste?: boolean | null;
  dateDelivDebut?: dayjs.Dayjs | null;
  dateDelivFin?: dayjs.Dayjs | null;
  isEmprDegit?: boolean | null;
  dateEmpreint?: dayjs.Dayjs | null;
  dateDelivAutor?: dayjs.Dayjs | null;
  dateValideAtorDebut?: dayjs.Dayjs | null;
  dateValideAutorFin?: dayjs.Dayjs | null;
  dateArrivPrevu?: dayjs.Dayjs | null;
  dateDepartPrevu?: dayjs.Dayjs | null;
  descInvite?: string | null;
  adresseInvit?: string | null;
  telInvite?: string | null;
  emailInvite?: string | null;
  nomEntreprise?: string | null;
  adresseEntreprise?: string | null;
  contactEntreprise?: string | null;
  descContactEntreprise?: string | null;
  financeFraisVoyage?: string | null;
  nomCit?: string | null;
  prenomCit?: string | null;
  dateNaissCit?: dayjs.Dayjs | null;
  nationCit?: number | null;
  numDocCit?: string | null;
  lieuForm?: string | null;
  dateForm?: dayjs.Dayjs | null;
  descPhotoForm?: string | null;
  descForm1?: string | null;
  descForm2?: string | null;
  emailComp?: string | null;
  nomForm?: string | null;
  prenomForm?: string | null;
  idPaysForm?: string | null;
  descForm3?: string | null;
  descForm4?: string | null;
  autreetatcivil?: string | null;
  autredoc?: string | null;
  autreobj?: string | null;
  autrefris?: string | null;
  descpromot?: string | null;
  descinvit?: string | null;
  isVisa?: boolean | null;
  dateaut?: dayjs.Dayjs | null;
  datecreatefo?: dayjs.Dayjs | null;
  telForm?: string | null;
  professionForm?: string | null;
  referenceForm?: string | null;
  otherNation?: string | null;
  telAutori?: string | null;
  emailAutori?: string | null;
  numSejour?: string | null;
  nomEmp?: string | null;
  numEmp?: string | null;
  villeDestination?: string | null;
  infoObjeVoyage?: string | null;
  autoDelivrePar?: string | null;
  otherMoyen?: string | null;
  otherLien?: string | null;
  faxInvite?: string | null;
  faxEntreprise?: string | null;
  natActuel?: string | null;
  natDiffer?: string | null;
  natMineur?: string | null;
  natCitoyen?: string | null;
  moysub1?: boolean | null;
  moysub2?: boolean | null;
  moysub3?: boolean | null;
  moysub4?: boolean | null;
  moysub5?: boolean | null;
  moysub8?: boolean | null;
  moysubs1?: boolean | null;
  moysubs5?: boolean | null;
  moysubs6?: boolean | null;
  moysubs7?: boolean | null;
  moysubs8?: boolean | null;
  autreFrais?: string | null;
  etatCivil?: number | null;
  idLienF?: number | null;
  objPerson?: number | null;
  idSexeAvs?: number | null;
  typeDocV?: number | null;
  typeFinance?: number | null;
  deleted?: boolean | null;
  folder?: IFolder | null;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public idMoyenSub?: number | null,
    public nomPrec?: string | null,
    public lieuNaissance?: string | null,
    public etatCivilWeb?: number | null,
    public nationPrec?: number | null,
    public nationActuel?: number | null,
    public nomAutor?: string | null,
    public prenomAutor?: string | null,
    public adresAutor?: string | null,
    public natiAutor?: number | null,
    public numCin?: string | null,
    public numDoc?: string | null,
    public dateDelivDoc?: dayjs.Dayjs | null,
    public dateExpDoc?: dayjs.Dayjs | null,
    public delivParDoc?: string | null,
    public adresseDomicile?: string | null,
    public adresseEmail?: string | null,
    public isResident?: boolean | null,
    public titreSejour?: string | null,
    public dateExpSejour?: dayjs.Dayjs | null,
    public numTel?: string | null,
    public adressEmp?: string | null,
    public telEmp?: string | null,
    public nomEtab?: string | null,
    public adressEtablis?: string | null,
    public dureeSejour?: number | null,
    public etatMemDes?: string | null,
    public etatMemPremier?: string | null,
    public nombreEntre?: string | null,
    public oldVisaExiste?: boolean | null,
    public dateDelivDebut?: dayjs.Dayjs | null,
    public dateDelivFin?: dayjs.Dayjs | null,
    public isEmprDegit?: boolean | null,
    public dateEmpreint?: dayjs.Dayjs | null,
    public dateDelivAutor?: dayjs.Dayjs | null,
    public dateValideAtorDebut?: dayjs.Dayjs | null,
    public dateValideAutorFin?: dayjs.Dayjs | null,
    public dateArrivPrevu?: dayjs.Dayjs | null,
    public dateDepartPrevu?: dayjs.Dayjs | null,
    public descInvite?: string | null,
    public adresseInvit?: string | null,
    public telInvite?: string | null,
    public emailInvite?: string | null,
    public nomEntreprise?: string | null,
    public adresseEntreprise?: string | null,
    public contactEntreprise?: string | null,
    public descContactEntreprise?: string | null,
    public financeFraisVoyage?: string | null,
    public nomCit?: string | null,
    public prenomCit?: string | null,
    public dateNaissCit?: dayjs.Dayjs | null,
    public nationCit?: number | null,
    public numDocCit?: string | null,
    public lieuForm?: string | null,
    public dateForm?: dayjs.Dayjs | null,
    public descPhotoForm?: string | null,
    public descForm1?: string | null,
    public descForm2?: string | null,
    public emailComp?: string | null,
    public nomForm?: string | null,
    public prenomForm?: string | null,
    public idPaysForm?: string | null,
    public descForm3?: string | null,
    public descForm4?: string | null,
    public autreetatcivil?: string | null,
    public autredoc?: string | null,
    public autreobj?: string | null,
    public autrefris?: string | null,
    public descpromot?: string | null,
    public descinvit?: string | null,
    public isVisa?: boolean | null,
    public dateaut?: dayjs.Dayjs | null,
    public datecreatefo?: dayjs.Dayjs | null,
    public telForm?: string | null,
    public professionForm?: string | null,
    public referenceForm?: string | null,
    public otherNation?: string | null,
    public telAutori?: string | null,
    public emailAutori?: string | null,
    public numSejour?: string | null,
    public nomEmp?: string | null,
    public numEmp?: string | null,
    public villeDestination?: string | null,
    public infoObjeVoyage?: string | null,
    public autoDelivrePar?: string | null,
    public otherMoyen?: string | null,
    public otherLien?: string | null,
    public faxInvite?: string | null,
    public faxEntreprise?: string | null,
    public natActuel?: string | null,
    public natDiffer?: string | null,
    public natMineur?: string | null,
    public natCitoyen?: string | null,
    public moysub1?: boolean | null,
    public moysub2?: boolean | null,
    public moysub3?: boolean | null,
    public moysub4?: boolean | null,
    public moysub5?: boolean | null,
    public moysub8?: boolean | null,
    public moysubs1?: boolean | null,
    public moysubs5?: boolean | null,
    public moysubs6?: boolean | null,
    public moysubs7?: boolean | null,
    public moysubs8?: boolean | null,
    public autreFrais?: string | null,
    public etatCivil?: number | null,
    public idLienF?: number | null,
    public objPerson?: number | null,
    public idSexeAvs?: number | null,
    public typeDocV?: number | null,
    public typeFinance?: number | null,
    public deleted?: boolean | null,
    public folder?: IFolder | null
  ) {
    this.isResident = this.isResident ?? false;
    this.oldVisaExiste = this.oldVisaExiste ?? false;
    this.isEmprDegit = this.isEmprDegit ?? false;
    this.isVisa = this.isVisa ?? false;
    this.moysub1 = this.moysub1 ?? false;
    this.moysub2 = this.moysub2 ?? false;
    this.moysub3 = this.moysub3 ?? false;
    this.moysub4 = this.moysub4 ?? false;
    this.moysub5 = this.moysub5 ?? false;
    this.moysub8 = this.moysub8 ?? false;
    this.moysubs1 = this.moysubs1 ?? false;
    this.moysubs5 = this.moysubs5 ?? false;
    this.moysubs6 = this.moysubs6 ?? false;
    this.moysubs7 = this.moysubs7 ?? false;
    this.moysubs8 = this.moysubs8 ?? false;
    this.deleted = this.deleted ?? false;
  }
}

export function getPersonIdentifier(person: IPerson): number | undefined {
  return person.id;
}
