package com.almaviva.vs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_moyen_sub")
    private Integer idMoyenSub;

    @Column(name = "nom_prec")
    private String nomPrec;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "etat_civil_web")
    private Integer etatCivilWeb;

    @Column(name = "nation_prec")
    private Integer nationPrec;

    @Column(name = "nation_actuel")
    private Integer nationActuel;

    @Column(name = "nom_autor")
    private String nomAutor;

    @Column(name = "prenom_autor")
    private String prenomAutor;

    @Column(name = "adres_autor")
    private String adresAutor;

    @Column(name = "nati_autor")
    private Integer natiAutor;

    @Column(name = "num_cin")
    private String numCin;

    @Column(name = "num_doc")
    private String numDoc;

    @Column(name = "date_deliv_doc")
    private LocalDate dateDelivDoc;

    @Column(name = "date_exp_doc")
    private LocalDate dateExpDoc;

    @Column(name = "deliv_par_doc")
    private String delivParDoc;

    @Column(name = "adresse_domicile")
    private String adresseDomicile;

    @Column(name = "adresse_email")
    private String adresseEmail;

    @Column(name = "is_resident")
    private Boolean isResident;

    @Column(name = "titre_sejour")
    private String titreSejour;

    @Column(name = "date_exp_sejour")
    private LocalDate dateExpSejour;

    @Column(name = "num_tel")
    private String numTel;

    @Column(name = "adress_emp")
    private String adressEmp;

    @Column(name = "tel_emp")
    private String telEmp;

    @Column(name = "nom_etab")
    private String nomEtab;

    @Column(name = "adress_etablis")
    private String adressEtablis;

    @Column(name = "duree_sejour")
    private Integer dureeSejour;

    @Column(name = "etat_mem_des")
    private String etatMemDes;

    @Column(name = "etat_mem_premier")
    private String etatMemPremier;

    @Column(name = "nombre_entre")
    private String nombreEntre;

    @Column(name = "old_visa_existe")
    private Boolean oldVisaExiste;

    @Column(name = "date_deliv_debut")
    private LocalDate dateDelivDebut;

    @Column(name = "date_deliv_fin")
    private LocalDate dateDelivFin;

    @Column(name = "is_empr_degit")
    private Boolean isEmprDegit;

    @Column(name = "date_empreint")
    private LocalDate dateEmpreint;

    @Column(name = "date_deliv_autor")
    private LocalDate dateDelivAutor;

    @Column(name = "date_valide_ator_debut")
    private LocalDate dateValideAtorDebut;

    @Column(name = "date_valide_autor_fin")
    private LocalDate dateValideAutorFin;

    @Column(name = "date_arriv_prevu")
    private LocalDate dateArrivPrevu;

    @Column(name = "date_depart_prevu")
    private LocalDate dateDepartPrevu;

    @Column(name = "desc_invite")
    private String descInvite;

    @Column(name = "adresse_invit")
    private String adresseInvit;

    @Column(name = "tel_invite")
    private String telInvite;

    @Column(name = "email_invite")
    private String emailInvite;

    @Column(name = "nom_entreprise")
    private String nomEntreprise;

    @Column(name = "adresse_entreprise")
    private String adresseEntreprise;

    @Column(name = "contact_entreprise")
    private String contactEntreprise;

    @Column(name = "desc_contact_entreprise")
    private String descContactEntreprise;

    @Column(name = "finance_frais_voyage")
    private String financeFraisVoyage;

    @Column(name = "nom_cit")
    private String nomCit;

    @Column(name = "prenom_cit")
    private String prenomCit;

    @Column(name = "date_naiss_cit")
    private LocalDate dateNaissCit;

    @Column(name = "nation_cit")
    private Integer nationCit;

    @Column(name = "num_doc_cit")
    private String numDocCit;

    @Column(name = "lieu_form")
    private String lieuForm;

    @Column(name = "date_form")
    private LocalDate dateForm;

    @Column(name = "desc_photo_form")
    private String descPhotoForm;

    @Column(name = "desc_form_1")
    private String descForm1;

    @Column(name = "desc_form_2")
    private String descForm2;

    @Column(name = "email_comp")
    private String emailComp;

    @Column(name = "nom_form")
    private String nomForm;

    @Column(name = "prenom_form")
    private String prenomForm;

    @Column(name = "id_pays_form")
    private String idPaysForm;

    @Column(name = "desc_form_3")
    private String descForm3;

    @Column(name = "desc_form_4")
    private String descForm4;

    @Column(name = "autreetatcivil")
    private String autreetatcivil;

    @Column(name = "autredoc")
    private String autredoc;

    @Column(name = "autreobj")
    private String autreobj;

    @Column(name = "autrefris")
    private String autrefris;

    @Column(name = "descpromot")
    private String descpromot;

    @Column(name = "descinvit")
    private String descinvit;

    @Column(name = "is_visa")
    private Boolean isVisa;

    @Column(name = "dateaut")
    private LocalDate dateaut;

    @Column(name = "datecreatefo")
    private LocalDate datecreatefo;

    @Column(name = "tel_form")
    private String telForm;

    @Column(name = "profession_form")
    private String professionForm;

    @Column(name = "reference_form")
    private String referenceForm;

    @Column(name = "other_nation")
    private String otherNation;

    @Column(name = "tel_autori")
    private String telAutori;

    @Column(name = "email_autori")
    private String emailAutori;

    @Column(name = "num_sejour")
    private String numSejour;

    @Column(name = "nom_emp")
    private String nomEmp;

    @Column(name = "num_emp")
    private String numEmp;

    @Column(name = "ville_destination")
    private String villeDestination;

    @Column(name = "info_obje_voyage")
    private String infoObjeVoyage;

    @Column(name = "auto_delivre_par")
    private String autoDelivrePar;

    @Column(name = "other_moyen")
    private String otherMoyen;

    @Column(name = "other_lien")
    private String otherLien;

    @Column(name = "fax_invite")
    private String faxInvite;

    @Column(name = "fax_entreprise")
    private String faxEntreprise;

    @Column(name = "nat_actuel")
    private String natActuel;

    @Column(name = "nat_differ")
    private String natDiffer;

    @Column(name = "nat_mineur")
    private String natMineur;

    @Column(name = "nat_citoyen")
    private String natCitoyen;

    @Column(name = "moysub_1")
    private Boolean moysub1;

    @Column(name = "moysub_2")
    private Boolean moysub2;

    @Column(name = "moysub_3")
    private Boolean moysub3;

    @Column(name = "moysub_4")
    private Boolean moysub4;

    @Column(name = "moysub_5")
    private Boolean moysub5;

    @Column(name = "moysub_8")
    private Boolean moysub8;

    @Column(name = "moysubs_1")
    private Boolean moysubs1;

    @Column(name = "moysubs_5")
    private Boolean moysubs5;

    @Column(name = "moysubs_6")
    private Boolean moysubs6;

    @Column(name = "moysubs_7")
    private Boolean moysubs7;

    @Column(name = "moysubs_8")
    private Boolean moysubs8;

    @Column(name = "autre_frais")
    private String autreFrais;

    @Column(name = "etat_civil")
    private Integer etatCivil;

    @Column(name = "id_lien_f")
    private Integer idLienF;

    @Column(name = "obj_person")
    private Integer objPerson;

    @Column(name = "id_sexe_avs")
    private Integer idSexeAvs;

    @Column(name = "type_doc_v")
    private Integer typeDocV;

    @Column(name = "type_finance")
    private Integer typeFinance;

    @Column(name = "deleted")
    private Boolean deleted;

    @JsonIgnoreProperties(value = { "appointment", "site", "services", "visa" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Folder folder;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getIdMoyenSub() {
        return this.idMoyenSub;
    }

    public Person idMoyenSub(Integer idMoyenSub) {
        this.idMoyenSub = idMoyenSub;
        return this;
    }

    public void setIdMoyenSub(Integer idMoyenSub) {
        this.idMoyenSub = idMoyenSub;
    }

    public String getNomPrec() {
        return this.nomPrec;
    }

    public Person nomPrec(String nomPrec) {
        this.nomPrec = nomPrec;
        return this;
    }

    public void setNomPrec(String nomPrec) {
        this.nomPrec = nomPrec;
    }

    public String getLieuNaissance() {
        return this.lieuNaissance;
    }

    public Person lieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
        return this;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public Integer getEtatCivilWeb() {
        return this.etatCivilWeb;
    }

    public Person etatCivilWeb(Integer etatCivilWeb) {
        this.etatCivilWeb = etatCivilWeb;
        return this;
    }

    public void setEtatCivilWeb(Integer etatCivilWeb) {
        this.etatCivilWeb = etatCivilWeb;
    }

    public Integer getNationPrec() {
        return this.nationPrec;
    }

    public Person nationPrec(Integer nationPrec) {
        this.nationPrec = nationPrec;
        return this;
    }

    public void setNationPrec(Integer nationPrec) {
        this.nationPrec = nationPrec;
    }

    public Integer getNationActuel() {
        return this.nationActuel;
    }

    public Person nationActuel(Integer nationActuel) {
        this.nationActuel = nationActuel;
        return this;
    }

    public void setNationActuel(Integer nationActuel) {
        this.nationActuel = nationActuel;
    }

    public String getNomAutor() {
        return this.nomAutor;
    }

    public Person nomAutor(String nomAutor) {
        this.nomAutor = nomAutor;
        return this;
    }

    public void setNomAutor(String nomAutor) {
        this.nomAutor = nomAutor;
    }

    public String getPrenomAutor() {
        return this.prenomAutor;
    }

    public Person prenomAutor(String prenomAutor) {
        this.prenomAutor = prenomAutor;
        return this;
    }

    public void setPrenomAutor(String prenomAutor) {
        this.prenomAutor = prenomAutor;
    }

    public String getAdresAutor() {
        return this.adresAutor;
    }

    public Person adresAutor(String adresAutor) {
        this.adresAutor = adresAutor;
        return this;
    }

    public void setAdresAutor(String adresAutor) {
        this.adresAutor = adresAutor;
    }

    public Integer getNatiAutor() {
        return this.natiAutor;
    }

    public Person natiAutor(Integer natiAutor) {
        this.natiAutor = natiAutor;
        return this;
    }

    public void setNatiAutor(Integer natiAutor) {
        this.natiAutor = natiAutor;
    }

    public String getNumCin() {
        return this.numCin;
    }

    public Person numCin(String numCin) {
        this.numCin = numCin;
        return this;
    }

    public void setNumCin(String numCin) {
        this.numCin = numCin;
    }

    public String getNumDoc() {
        return this.numDoc;
    }

    public Person numDoc(String numDoc) {
        this.numDoc = numDoc;
        return this;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public LocalDate getDateDelivDoc() {
        return this.dateDelivDoc;
    }

    public Person dateDelivDoc(LocalDate dateDelivDoc) {
        this.dateDelivDoc = dateDelivDoc;
        return this;
    }

    public void setDateDelivDoc(LocalDate dateDelivDoc) {
        this.dateDelivDoc = dateDelivDoc;
    }

    public LocalDate getDateExpDoc() {
        return this.dateExpDoc;
    }

    public Person dateExpDoc(LocalDate dateExpDoc) {
        this.dateExpDoc = dateExpDoc;
        return this;
    }

    public void setDateExpDoc(LocalDate dateExpDoc) {
        this.dateExpDoc = dateExpDoc;
    }

    public String getDelivParDoc() {
        return this.delivParDoc;
    }

    public Person delivParDoc(String delivParDoc) {
        this.delivParDoc = delivParDoc;
        return this;
    }

    public void setDelivParDoc(String delivParDoc) {
        this.delivParDoc = delivParDoc;
    }

    public String getAdresseDomicile() {
        return this.adresseDomicile;
    }

    public Person adresseDomicile(String adresseDomicile) {
        this.adresseDomicile = adresseDomicile;
        return this;
    }

    public void setAdresseDomicile(String adresseDomicile) {
        this.adresseDomicile = adresseDomicile;
    }

    public String getAdresseEmail() {
        return this.adresseEmail;
    }

    public Person adresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
        return this;
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public Boolean getIsResident() {
        return this.isResident;
    }

    public Person isResident(Boolean isResident) {
        this.isResident = isResident;
        return this;
    }

    public void setIsResident(Boolean isResident) {
        this.isResident = isResident;
    }

    public String getTitreSejour() {
        return this.titreSejour;
    }

    public Person titreSejour(String titreSejour) {
        this.titreSejour = titreSejour;
        return this;
    }

    public void setTitreSejour(String titreSejour) {
        this.titreSejour = titreSejour;
    }

    public LocalDate getDateExpSejour() {
        return this.dateExpSejour;
    }

    public Person dateExpSejour(LocalDate dateExpSejour) {
        this.dateExpSejour = dateExpSejour;
        return this;
    }

    public void setDateExpSejour(LocalDate dateExpSejour) {
        this.dateExpSejour = dateExpSejour;
    }

    public String getNumTel() {
        return this.numTel;
    }

    public Person numTel(String numTel) {
        this.numTel = numTel;
        return this;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getAdressEmp() {
        return this.adressEmp;
    }

    public Person adressEmp(String adressEmp) {
        this.adressEmp = adressEmp;
        return this;
    }

    public void setAdressEmp(String adressEmp) {
        this.adressEmp = adressEmp;
    }

    public String getTelEmp() {
        return this.telEmp;
    }

    public Person telEmp(String telEmp) {
        this.telEmp = telEmp;
        return this;
    }

    public void setTelEmp(String telEmp) {
        this.telEmp = telEmp;
    }

    public String getNomEtab() {
        return this.nomEtab;
    }

    public Person nomEtab(String nomEtab) {
        this.nomEtab = nomEtab;
        return this;
    }

    public void setNomEtab(String nomEtab) {
        this.nomEtab = nomEtab;
    }

    public String getAdressEtablis() {
        return this.adressEtablis;
    }

    public Person adressEtablis(String adressEtablis) {
        this.adressEtablis = adressEtablis;
        return this;
    }

    public void setAdressEtablis(String adressEtablis) {
        this.adressEtablis = adressEtablis;
    }

    public Integer getDureeSejour() {
        return this.dureeSejour;
    }

    public Person dureeSejour(Integer dureeSejour) {
        this.dureeSejour = dureeSejour;
        return this;
    }

    public void setDureeSejour(Integer dureeSejour) {
        this.dureeSejour = dureeSejour;
    }

    public String getEtatMemDes() {
        return this.etatMemDes;
    }

    public Person etatMemDes(String etatMemDes) {
        this.etatMemDes = etatMemDes;
        return this;
    }

    public void setEtatMemDes(String etatMemDes) {
        this.etatMemDes = etatMemDes;
    }

    public String getEtatMemPremier() {
        return this.etatMemPremier;
    }

    public Person etatMemPremier(String etatMemPremier) {
        this.etatMemPremier = etatMemPremier;
        return this;
    }

    public void setEtatMemPremier(String etatMemPremier) {
        this.etatMemPremier = etatMemPremier;
    }

    public String getNombreEntre() {
        return this.nombreEntre;
    }

    public Person nombreEntre(String nombreEntre) {
        this.nombreEntre = nombreEntre;
        return this;
    }

    public void setNombreEntre(String nombreEntre) {
        this.nombreEntre = nombreEntre;
    }

    public Boolean getOldVisaExiste() {
        return this.oldVisaExiste;
    }

    public Person oldVisaExiste(Boolean oldVisaExiste) {
        this.oldVisaExiste = oldVisaExiste;
        return this;
    }

    public void setOldVisaExiste(Boolean oldVisaExiste) {
        this.oldVisaExiste = oldVisaExiste;
    }

    public LocalDate getDateDelivDebut() {
        return this.dateDelivDebut;
    }

    public Person dateDelivDebut(LocalDate dateDelivDebut) {
        this.dateDelivDebut = dateDelivDebut;
        return this;
    }

    public void setDateDelivDebut(LocalDate dateDelivDebut) {
        this.dateDelivDebut = dateDelivDebut;
    }

    public LocalDate getDateDelivFin() {
        return this.dateDelivFin;
    }

    public Person dateDelivFin(LocalDate dateDelivFin) {
        this.dateDelivFin = dateDelivFin;
        return this;
    }

    public void setDateDelivFin(LocalDate dateDelivFin) {
        this.dateDelivFin = dateDelivFin;
    }

    public Boolean getIsEmprDegit() {
        return this.isEmprDegit;
    }

    public Person isEmprDegit(Boolean isEmprDegit) {
        this.isEmprDegit = isEmprDegit;
        return this;
    }

    public void setIsEmprDegit(Boolean isEmprDegit) {
        this.isEmprDegit = isEmprDegit;
    }

    public LocalDate getDateEmpreint() {
        return this.dateEmpreint;
    }

    public Person dateEmpreint(LocalDate dateEmpreint) {
        this.dateEmpreint = dateEmpreint;
        return this;
    }

    public void setDateEmpreint(LocalDate dateEmpreint) {
        this.dateEmpreint = dateEmpreint;
    }

    public LocalDate getDateDelivAutor() {
        return this.dateDelivAutor;
    }

    public Person dateDelivAutor(LocalDate dateDelivAutor) {
        this.dateDelivAutor = dateDelivAutor;
        return this;
    }

    public void setDateDelivAutor(LocalDate dateDelivAutor) {
        this.dateDelivAutor = dateDelivAutor;
    }

    public LocalDate getDateValideAtorDebut() {
        return this.dateValideAtorDebut;
    }

    public Person dateValideAtorDebut(LocalDate dateValideAtorDebut) {
        this.dateValideAtorDebut = dateValideAtorDebut;
        return this;
    }

    public void setDateValideAtorDebut(LocalDate dateValideAtorDebut) {
        this.dateValideAtorDebut = dateValideAtorDebut;
    }

    public LocalDate getDateValideAutorFin() {
        return this.dateValideAutorFin;
    }

    public Person dateValideAutorFin(LocalDate dateValideAutorFin) {
        this.dateValideAutorFin = dateValideAutorFin;
        return this;
    }

    public void setDateValideAutorFin(LocalDate dateValideAutorFin) {
        this.dateValideAutorFin = dateValideAutorFin;
    }

    public LocalDate getDateArrivPrevu() {
        return this.dateArrivPrevu;
    }

    public Person dateArrivPrevu(LocalDate dateArrivPrevu) {
        this.dateArrivPrevu = dateArrivPrevu;
        return this;
    }

    public void setDateArrivPrevu(LocalDate dateArrivPrevu) {
        this.dateArrivPrevu = dateArrivPrevu;
    }

    public LocalDate getDateDepartPrevu() {
        return this.dateDepartPrevu;
    }

    public Person dateDepartPrevu(LocalDate dateDepartPrevu) {
        this.dateDepartPrevu = dateDepartPrevu;
        return this;
    }

    public void setDateDepartPrevu(LocalDate dateDepartPrevu) {
        this.dateDepartPrevu = dateDepartPrevu;
    }

    public String getDescInvite() {
        return this.descInvite;
    }

    public Person descInvite(String descInvite) {
        this.descInvite = descInvite;
        return this;
    }

    public void setDescInvite(String descInvite) {
        this.descInvite = descInvite;
    }

    public String getAdresseInvit() {
        return this.adresseInvit;
    }

    public Person adresseInvit(String adresseInvit) {
        this.adresseInvit = adresseInvit;
        return this;
    }

    public void setAdresseInvit(String adresseInvit) {
        this.adresseInvit = adresseInvit;
    }

    public String getTelInvite() {
        return this.telInvite;
    }

    public Person telInvite(String telInvite) {
        this.telInvite = telInvite;
        return this;
    }

    public void setTelInvite(String telInvite) {
        this.telInvite = telInvite;
    }

    public String getEmailInvite() {
        return this.emailInvite;
    }

    public Person emailInvite(String emailInvite) {
        this.emailInvite = emailInvite;
        return this;
    }

    public void setEmailInvite(String emailInvite) {
        this.emailInvite = emailInvite;
    }

    public String getNomEntreprise() {
        return this.nomEntreprise;
    }

    public Person nomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
        return this;
    }

    public void setNomEntreprise(String nomEntreprise) {
        this.nomEntreprise = nomEntreprise;
    }

    public String getAdresseEntreprise() {
        return this.adresseEntreprise;
    }

    public Person adresseEntreprise(String adresseEntreprise) {
        this.adresseEntreprise = adresseEntreprise;
        return this;
    }

    public void setAdresseEntreprise(String adresseEntreprise) {
        this.adresseEntreprise = adresseEntreprise;
    }

    public String getContactEntreprise() {
        return this.contactEntreprise;
    }

    public Person contactEntreprise(String contactEntreprise) {
        this.contactEntreprise = contactEntreprise;
        return this;
    }

    public void setContactEntreprise(String contactEntreprise) {
        this.contactEntreprise = contactEntreprise;
    }

    public String getDescContactEntreprise() {
        return this.descContactEntreprise;
    }

    public Person descContactEntreprise(String descContactEntreprise) {
        this.descContactEntreprise = descContactEntreprise;
        return this;
    }

    public void setDescContactEntreprise(String descContactEntreprise) {
        this.descContactEntreprise = descContactEntreprise;
    }

    public String getFinanceFraisVoyage() {
        return this.financeFraisVoyage;
    }

    public Person financeFraisVoyage(String financeFraisVoyage) {
        this.financeFraisVoyage = financeFraisVoyage;
        return this;
    }

    public void setFinanceFraisVoyage(String financeFraisVoyage) {
        this.financeFraisVoyage = financeFraisVoyage;
    }

    public String getNomCit() {
        return this.nomCit;
    }

    public Person nomCit(String nomCit) {
        this.nomCit = nomCit;
        return this;
    }

    public void setNomCit(String nomCit) {
        this.nomCit = nomCit;
    }

    public String getPrenomCit() {
        return this.prenomCit;
    }

    public Person prenomCit(String prenomCit) {
        this.prenomCit = prenomCit;
        return this;
    }

    public void setPrenomCit(String prenomCit) {
        this.prenomCit = prenomCit;
    }

    public LocalDate getDateNaissCit() {
        return this.dateNaissCit;
    }

    public Person dateNaissCit(LocalDate dateNaissCit) {
        this.dateNaissCit = dateNaissCit;
        return this;
    }

    public void setDateNaissCit(LocalDate dateNaissCit) {
        this.dateNaissCit = dateNaissCit;
    }

    public Integer getNationCit() {
        return this.nationCit;
    }

    public Person nationCit(Integer nationCit) {
        this.nationCit = nationCit;
        return this;
    }

    public void setNationCit(Integer nationCit) {
        this.nationCit = nationCit;
    }

    public String getNumDocCit() {
        return this.numDocCit;
    }

    public Person numDocCit(String numDocCit) {
        this.numDocCit = numDocCit;
        return this;
    }

    public void setNumDocCit(String numDocCit) {
        this.numDocCit = numDocCit;
    }

    public String getLieuForm() {
        return this.lieuForm;
    }

    public Person lieuForm(String lieuForm) {
        this.lieuForm = lieuForm;
        return this;
    }

    public void setLieuForm(String lieuForm) {
        this.lieuForm = lieuForm;
    }

    public LocalDate getDateForm() {
        return this.dateForm;
    }

    public Person dateForm(LocalDate dateForm) {
        this.dateForm = dateForm;
        return this;
    }

    public void setDateForm(LocalDate dateForm) {
        this.dateForm = dateForm;
    }

    public String getDescPhotoForm() {
        return this.descPhotoForm;
    }

    public Person descPhotoForm(String descPhotoForm) {
        this.descPhotoForm = descPhotoForm;
        return this;
    }

    public void setDescPhotoForm(String descPhotoForm) {
        this.descPhotoForm = descPhotoForm;
    }

    public String getDescForm1() {
        return this.descForm1;
    }

    public Person descForm1(String descForm1) {
        this.descForm1 = descForm1;
        return this;
    }

    public void setDescForm1(String descForm1) {
        this.descForm1 = descForm1;
    }

    public String getDescForm2() {
        return this.descForm2;
    }

    public Person descForm2(String descForm2) {
        this.descForm2 = descForm2;
        return this;
    }

    public void setDescForm2(String descForm2) {
        this.descForm2 = descForm2;
    }

    public String getEmailComp() {
        return this.emailComp;
    }

    public Person emailComp(String emailComp) {
        this.emailComp = emailComp;
        return this;
    }

    public void setEmailComp(String emailComp) {
        this.emailComp = emailComp;
    }

    public String getNomForm() {
        return this.nomForm;
    }

    public Person nomForm(String nomForm) {
        this.nomForm = nomForm;
        return this;
    }

    public void setNomForm(String nomForm) {
        this.nomForm = nomForm;
    }

    public String getPrenomForm() {
        return this.prenomForm;
    }

    public Person prenomForm(String prenomForm) {
        this.prenomForm = prenomForm;
        return this;
    }

    public void setPrenomForm(String prenomForm) {
        this.prenomForm = prenomForm;
    }

    public String getIdPaysForm() {
        return this.idPaysForm;
    }

    public Person idPaysForm(String idPaysForm) {
        this.idPaysForm = idPaysForm;
        return this;
    }

    public void setIdPaysForm(String idPaysForm) {
        this.idPaysForm = idPaysForm;
    }

    public String getDescForm3() {
        return this.descForm3;
    }

    public Person descForm3(String descForm3) {
        this.descForm3 = descForm3;
        return this;
    }

    public void setDescForm3(String descForm3) {
        this.descForm3 = descForm3;
    }

    public String getDescForm4() {
        return this.descForm4;
    }

    public Person descForm4(String descForm4) {
        this.descForm4 = descForm4;
        return this;
    }

    public void setDescForm4(String descForm4) {
        this.descForm4 = descForm4;
    }

    public String getAutreetatcivil() {
        return this.autreetatcivil;
    }

    public Person autreetatcivil(String autreetatcivil) {
        this.autreetatcivil = autreetatcivil;
        return this;
    }

    public void setAutreetatcivil(String autreetatcivil) {
        this.autreetatcivil = autreetatcivil;
    }

    public String getAutredoc() {
        return this.autredoc;
    }

    public Person autredoc(String autredoc) {
        this.autredoc = autredoc;
        return this;
    }

    public void setAutredoc(String autredoc) {
        this.autredoc = autredoc;
    }

    public String getAutreobj() {
        return this.autreobj;
    }

    public Person autreobj(String autreobj) {
        this.autreobj = autreobj;
        return this;
    }

    public void setAutreobj(String autreobj) {
        this.autreobj = autreobj;
    }

    public String getAutrefris() {
        return this.autrefris;
    }

    public Person autrefris(String autrefris) {
        this.autrefris = autrefris;
        return this;
    }

    public void setAutrefris(String autrefris) {
        this.autrefris = autrefris;
    }

    public String getDescpromot() {
        return this.descpromot;
    }

    public Person descpromot(String descpromot) {
        this.descpromot = descpromot;
        return this;
    }

    public void setDescpromot(String descpromot) {
        this.descpromot = descpromot;
    }

    public String getDescinvit() {
        return this.descinvit;
    }

    public Person descinvit(String descinvit) {
        this.descinvit = descinvit;
        return this;
    }

    public void setDescinvit(String descinvit) {
        this.descinvit = descinvit;
    }

    public Boolean getIsVisa() {
        return this.isVisa;
    }

    public Person isVisa(Boolean isVisa) {
        this.isVisa = isVisa;
        return this;
    }

    public void setIsVisa(Boolean isVisa) {
        this.isVisa = isVisa;
    }

    public LocalDate getDateaut() {
        return this.dateaut;
    }

    public Person dateaut(LocalDate dateaut) {
        this.dateaut = dateaut;
        return this;
    }

    public void setDateaut(LocalDate dateaut) {
        this.dateaut = dateaut;
    }

    public LocalDate getDatecreatefo() {
        return this.datecreatefo;
    }

    public Person datecreatefo(LocalDate datecreatefo) {
        this.datecreatefo = datecreatefo;
        return this;
    }

    public void setDatecreatefo(LocalDate datecreatefo) {
        this.datecreatefo = datecreatefo;
    }

    public String getTelForm() {
        return this.telForm;
    }

    public Person telForm(String telForm) {
        this.telForm = telForm;
        return this;
    }

    public void setTelForm(String telForm) {
        this.telForm = telForm;
    }

    public String getProfessionForm() {
        return this.professionForm;
    }

    public Person professionForm(String professionForm) {
        this.professionForm = professionForm;
        return this;
    }

    public void setProfessionForm(String professionForm) {
        this.professionForm = professionForm;
    }

    public String getReferenceForm() {
        return this.referenceForm;
    }

    public Person referenceForm(String referenceForm) {
        this.referenceForm = referenceForm;
        return this;
    }

    public void setReferenceForm(String referenceForm) {
        this.referenceForm = referenceForm;
    }

    public String getOtherNation() {
        return this.otherNation;
    }

    public Person otherNation(String otherNation) {
        this.otherNation = otherNation;
        return this;
    }

    public void setOtherNation(String otherNation) {
        this.otherNation = otherNation;
    }

    public String getTelAutori() {
        return this.telAutori;
    }

    public Person telAutori(String telAutori) {
        this.telAutori = telAutori;
        return this;
    }

    public void setTelAutori(String telAutori) {
        this.telAutori = telAutori;
    }

    public String getEmailAutori() {
        return this.emailAutori;
    }

    public Person emailAutori(String emailAutori) {
        this.emailAutori = emailAutori;
        return this;
    }

    public void setEmailAutori(String emailAutori) {
        this.emailAutori = emailAutori;
    }

    public String getNumSejour() {
        return this.numSejour;
    }

    public Person numSejour(String numSejour) {
        this.numSejour = numSejour;
        return this;
    }

    public void setNumSejour(String numSejour) {
        this.numSejour = numSejour;
    }

    public String getNomEmp() {
        return this.nomEmp;
    }

    public Person nomEmp(String nomEmp) {
        this.nomEmp = nomEmp;
        return this;
    }

    public void setNomEmp(String nomEmp) {
        this.nomEmp = nomEmp;
    }

    public String getNumEmp() {
        return this.numEmp;
    }

    public Person numEmp(String numEmp) {
        this.numEmp = numEmp;
        return this;
    }

    public void setNumEmp(String numEmp) {
        this.numEmp = numEmp;
    }

    public String getVilleDestination() {
        return this.villeDestination;
    }

    public Person villeDestination(String villeDestination) {
        this.villeDestination = villeDestination;
        return this;
    }

    public void setVilleDestination(String villeDestination) {
        this.villeDestination = villeDestination;
    }

    public String getInfoObjeVoyage() {
        return this.infoObjeVoyage;
    }

    public Person infoObjeVoyage(String infoObjeVoyage) {
        this.infoObjeVoyage = infoObjeVoyage;
        return this;
    }

    public void setInfoObjeVoyage(String infoObjeVoyage) {
        this.infoObjeVoyage = infoObjeVoyage;
    }

    public String getAutoDelivrePar() {
        return this.autoDelivrePar;
    }

    public Person autoDelivrePar(String autoDelivrePar) {
        this.autoDelivrePar = autoDelivrePar;
        return this;
    }

    public void setAutoDelivrePar(String autoDelivrePar) {
        this.autoDelivrePar = autoDelivrePar;
    }

    public String getOtherMoyen() {
        return this.otherMoyen;
    }

    public Person otherMoyen(String otherMoyen) {
        this.otherMoyen = otherMoyen;
        return this;
    }

    public void setOtherMoyen(String otherMoyen) {
        this.otherMoyen = otherMoyen;
    }

    public String getOtherLien() {
        return this.otherLien;
    }

    public Person otherLien(String otherLien) {
        this.otherLien = otherLien;
        return this;
    }

    public void setOtherLien(String otherLien) {
        this.otherLien = otherLien;
    }

    public String getFaxInvite() {
        return this.faxInvite;
    }

    public Person faxInvite(String faxInvite) {
        this.faxInvite = faxInvite;
        return this;
    }

    public void setFaxInvite(String faxInvite) {
        this.faxInvite = faxInvite;
    }

    public String getFaxEntreprise() {
        return this.faxEntreprise;
    }

    public Person faxEntreprise(String faxEntreprise) {
        this.faxEntreprise = faxEntreprise;
        return this;
    }

    public void setFaxEntreprise(String faxEntreprise) {
        this.faxEntreprise = faxEntreprise;
    }

    public String getNatActuel() {
        return this.natActuel;
    }

    public Person natActuel(String natActuel) {
        this.natActuel = natActuel;
        return this;
    }

    public void setNatActuel(String natActuel) {
        this.natActuel = natActuel;
    }

    public String getNatDiffer() {
        return this.natDiffer;
    }

    public Person natDiffer(String natDiffer) {
        this.natDiffer = natDiffer;
        return this;
    }

    public void setNatDiffer(String natDiffer) {
        this.natDiffer = natDiffer;
    }

    public String getNatMineur() {
        return this.natMineur;
    }

    public Person natMineur(String natMineur) {
        this.natMineur = natMineur;
        return this;
    }

    public void setNatMineur(String natMineur) {
        this.natMineur = natMineur;
    }

    public String getNatCitoyen() {
        return this.natCitoyen;
    }

    public Person natCitoyen(String natCitoyen) {
        this.natCitoyen = natCitoyen;
        return this;
    }

    public void setNatCitoyen(String natCitoyen) {
        this.natCitoyen = natCitoyen;
    }

    public Boolean getMoysub1() {
        return this.moysub1;
    }

    public Person moysub1(Boolean moysub1) {
        this.moysub1 = moysub1;
        return this;
    }

    public void setMoysub1(Boolean moysub1) {
        this.moysub1 = moysub1;
    }

    public Boolean getMoysub2() {
        return this.moysub2;
    }

    public Person moysub2(Boolean moysub2) {
        this.moysub2 = moysub2;
        return this;
    }

    public void setMoysub2(Boolean moysub2) {
        this.moysub2 = moysub2;
    }

    public Boolean getMoysub3() {
        return this.moysub3;
    }

    public Person moysub3(Boolean moysub3) {
        this.moysub3 = moysub3;
        return this;
    }

    public void setMoysub3(Boolean moysub3) {
        this.moysub3 = moysub3;
    }

    public Boolean getMoysub4() {
        return this.moysub4;
    }

    public Person moysub4(Boolean moysub4) {
        this.moysub4 = moysub4;
        return this;
    }

    public void setMoysub4(Boolean moysub4) {
        this.moysub4 = moysub4;
    }

    public Boolean getMoysub5() {
        return this.moysub5;
    }

    public Person moysub5(Boolean moysub5) {
        this.moysub5 = moysub5;
        return this;
    }

    public void setMoysub5(Boolean moysub5) {
        this.moysub5 = moysub5;
    }

    public Boolean getMoysub8() {
        return this.moysub8;
    }

    public Person moysub8(Boolean moysub8) {
        this.moysub8 = moysub8;
        return this;
    }

    public void setMoysub8(Boolean moysub8) {
        this.moysub8 = moysub8;
    }

    public Boolean getMoysubs1() {
        return this.moysubs1;
    }

    public Person moysubs1(Boolean moysubs1) {
        this.moysubs1 = moysubs1;
        return this;
    }

    public void setMoysubs1(Boolean moysubs1) {
        this.moysubs1 = moysubs1;
    }

    public Boolean getMoysubs5() {
        return this.moysubs5;
    }

    public Person moysubs5(Boolean moysubs5) {
        this.moysubs5 = moysubs5;
        return this;
    }

    public void setMoysubs5(Boolean moysubs5) {
        this.moysubs5 = moysubs5;
    }

    public Boolean getMoysubs6() {
        return this.moysubs6;
    }

    public Person moysubs6(Boolean moysubs6) {
        this.moysubs6 = moysubs6;
        return this;
    }

    public void setMoysubs6(Boolean moysubs6) {
        this.moysubs6 = moysubs6;
    }

    public Boolean getMoysubs7() {
        return this.moysubs7;
    }

    public Person moysubs7(Boolean moysubs7) {
        this.moysubs7 = moysubs7;
        return this;
    }

    public void setMoysubs7(Boolean moysubs7) {
        this.moysubs7 = moysubs7;
    }

    public Boolean getMoysubs8() {
        return this.moysubs8;
    }

    public Person moysubs8(Boolean moysubs8) {
        this.moysubs8 = moysubs8;
        return this;
    }

    public void setMoysubs8(Boolean moysubs8) {
        this.moysubs8 = moysubs8;
    }

    public String getAutreFrais() {
        return this.autreFrais;
    }

    public Person autreFrais(String autreFrais) {
        this.autreFrais = autreFrais;
        return this;
    }

    public void setAutreFrais(String autreFrais) {
        this.autreFrais = autreFrais;
    }

    public Integer getEtatCivil() {
        return this.etatCivil;
    }

    public Person etatCivil(Integer etatCivil) {
        this.etatCivil = etatCivil;
        return this;
    }

    public void setEtatCivil(Integer etatCivil) {
        this.etatCivil = etatCivil;
    }

    public Integer getIdLienF() {
        return this.idLienF;
    }

    public Person idLienF(Integer idLienF) {
        this.idLienF = idLienF;
        return this;
    }

    public void setIdLienF(Integer idLienF) {
        this.idLienF = idLienF;
    }

    public Integer getObjPerson() {
        return this.objPerson;
    }

    public Person objPerson(Integer objPerson) {
        this.objPerson = objPerson;
        return this;
    }

    public void setObjPerson(Integer objPerson) {
        this.objPerson = objPerson;
    }

    public Integer getIdSexeAvs() {
        return this.idSexeAvs;
    }

    public Person idSexeAvs(Integer idSexeAvs) {
        this.idSexeAvs = idSexeAvs;
        return this;
    }

    public void setIdSexeAvs(Integer idSexeAvs) {
        this.idSexeAvs = idSexeAvs;
    }

    public Integer getTypeDocV() {
        return this.typeDocV;
    }

    public Person typeDocV(Integer typeDocV) {
        this.typeDocV = typeDocV;
        return this;
    }

    public void setTypeDocV(Integer typeDocV) {
        this.typeDocV = typeDocV;
    }

    public Integer getTypeFinance() {
        return this.typeFinance;
    }

    public Person typeFinance(Integer typeFinance) {
        this.typeFinance = typeFinance;
        return this;
    }

    public void setTypeFinance(Integer typeFinance) {
        this.typeFinance = typeFinance;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Person deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Folder getFolder() {
        return this.folder;
    }

    public Person folder(Folder folder) {
        this.setFolder(folder);
        return this;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", idMoyenSub=" + getIdMoyenSub() +
            ", nomPrec='" + getNomPrec() + "'" +
            ", lieuNaissance='" + getLieuNaissance() + "'" +
            ", etatCivilWeb=" + getEtatCivilWeb() +
            ", nationPrec=" + getNationPrec() +
            ", nationActuel=" + getNationActuel() +
            ", nomAutor='" + getNomAutor() + "'" +
            ", prenomAutor='" + getPrenomAutor() + "'" +
            ", adresAutor='" + getAdresAutor() + "'" +
            ", natiAutor=" + getNatiAutor() +
            ", numCin='" + getNumCin() + "'" +
            ", numDoc='" + getNumDoc() + "'" +
            ", dateDelivDoc='" + getDateDelivDoc() + "'" +
            ", dateExpDoc='" + getDateExpDoc() + "'" +
            ", delivParDoc='" + getDelivParDoc() + "'" +
            ", adresseDomicile='" + getAdresseDomicile() + "'" +
            ", adresseEmail='" + getAdresseEmail() + "'" +
            ", isResident='" + getIsResident() + "'" +
            ", titreSejour='" + getTitreSejour() + "'" +
            ", dateExpSejour='" + getDateExpSejour() + "'" +
            ", numTel='" + getNumTel() + "'" +
            ", adressEmp='" + getAdressEmp() + "'" +
            ", telEmp='" + getTelEmp() + "'" +
            ", nomEtab='" + getNomEtab() + "'" +
            ", adressEtablis='" + getAdressEtablis() + "'" +
            ", dureeSejour=" + getDureeSejour() +
            ", etatMemDes='" + getEtatMemDes() + "'" +
            ", etatMemPremier='" + getEtatMemPremier() + "'" +
            ", nombreEntre='" + getNombreEntre() + "'" +
            ", oldVisaExiste='" + getOldVisaExiste() + "'" +
            ", dateDelivDebut='" + getDateDelivDebut() + "'" +
            ", dateDelivFin='" + getDateDelivFin() + "'" +
            ", isEmprDegit='" + getIsEmprDegit() + "'" +
            ", dateEmpreint='" + getDateEmpreint() + "'" +
            ", dateDelivAutor='" + getDateDelivAutor() + "'" +
            ", dateValideAtorDebut='" + getDateValideAtorDebut() + "'" +
            ", dateValideAutorFin='" + getDateValideAutorFin() + "'" +
            ", dateArrivPrevu='" + getDateArrivPrevu() + "'" +
            ", dateDepartPrevu='" + getDateDepartPrevu() + "'" +
            ", descInvite='" + getDescInvite() + "'" +
            ", adresseInvit='" + getAdresseInvit() + "'" +
            ", telInvite='" + getTelInvite() + "'" +
            ", emailInvite='" + getEmailInvite() + "'" +
            ", nomEntreprise='" + getNomEntreprise() + "'" +
            ", adresseEntreprise='" + getAdresseEntreprise() + "'" +
            ", contactEntreprise='" + getContactEntreprise() + "'" +
            ", descContactEntreprise='" + getDescContactEntreprise() + "'" +
            ", financeFraisVoyage='" + getFinanceFraisVoyage() + "'" +
            ", nomCit='" + getNomCit() + "'" +
            ", prenomCit='" + getPrenomCit() + "'" +
            ", dateNaissCit='" + getDateNaissCit() + "'" +
            ", nationCit=" + getNationCit() +
            ", numDocCit='" + getNumDocCit() + "'" +
            ", lieuForm='" + getLieuForm() + "'" +
            ", dateForm='" + getDateForm() + "'" +
            ", descPhotoForm='" + getDescPhotoForm() + "'" +
            ", descForm1='" + getDescForm1() + "'" +
            ", descForm2='" + getDescForm2() + "'" +
            ", emailComp='" + getEmailComp() + "'" +
            ", nomForm='" + getNomForm() + "'" +
            ", prenomForm='" + getPrenomForm() + "'" +
            ", idPaysForm='" + getIdPaysForm() + "'" +
            ", descForm3='" + getDescForm3() + "'" +
            ", descForm4='" + getDescForm4() + "'" +
            ", autreetatcivil='" + getAutreetatcivil() + "'" +
            ", autredoc='" + getAutredoc() + "'" +
            ", autreobj='" + getAutreobj() + "'" +
            ", autrefris='" + getAutrefris() + "'" +
            ", descpromot='" + getDescpromot() + "'" +
            ", descinvit='" + getDescinvit() + "'" +
            ", isVisa='" + getIsVisa() + "'" +
            ", dateaut='" + getDateaut() + "'" +
            ", datecreatefo='" + getDatecreatefo() + "'" +
            ", telForm='" + getTelForm() + "'" +
            ", professionForm='" + getProfessionForm() + "'" +
            ", referenceForm='" + getReferenceForm() + "'" +
            ", otherNation='" + getOtherNation() + "'" +
            ", telAutori='" + getTelAutori() + "'" +
            ", emailAutori='" + getEmailAutori() + "'" +
            ", numSejour='" + getNumSejour() + "'" +
            ", nomEmp='" + getNomEmp() + "'" +
            ", numEmp='" + getNumEmp() + "'" +
            ", villeDestination='" + getVilleDestination() + "'" +
            ", infoObjeVoyage='" + getInfoObjeVoyage() + "'" +
            ", autoDelivrePar='" + getAutoDelivrePar() + "'" +
            ", otherMoyen='" + getOtherMoyen() + "'" +
            ", otherLien='" + getOtherLien() + "'" +
            ", faxInvite='" + getFaxInvite() + "'" +
            ", faxEntreprise='" + getFaxEntreprise() + "'" +
            ", natActuel='" + getNatActuel() + "'" +
            ", natDiffer='" + getNatDiffer() + "'" +
            ", natMineur='" + getNatMineur() + "'" +
            ", natCitoyen='" + getNatCitoyen() + "'" +
            ", moysub1='" + getMoysub1() + "'" +
            ", moysub2='" + getMoysub2() + "'" +
            ", moysub3='" + getMoysub3() + "'" +
            ", moysub4='" + getMoysub4() + "'" +
            ", moysub5='" + getMoysub5() + "'" +
            ", moysub8='" + getMoysub8() + "'" +
            ", moysubs1='" + getMoysubs1() + "'" +
            ", moysubs5='" + getMoysubs5() + "'" +
            ", moysubs6='" + getMoysubs6() + "'" +
            ", moysubs7='" + getMoysubs7() + "'" +
            ", moysubs8='" + getMoysubs8() + "'" +
            ", autreFrais='" + getAutreFrais() + "'" +
            ", etatCivil=" + getEtatCivil() +
            ", idLienF=" + getIdLienF() +
            ", objPerson=" + getObjPerson() +
            ", idSexeAvs=" + getIdSexeAvs() +
            ", typeDocV=" + getTypeDocV() +
            ", typeFinance=" + getTypeFinance() +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
