package com.almaviva.vs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.almaviva.vs.IntegrationTest;
import com.almaviva.vs.domain.Person;
import com.almaviva.vs.repository.PersonRepository;
import com.almaviva.vs.repository.search.PersonSearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PersonResourceIT {

    private static final Integer DEFAULT_ID_MOYEN_SUB = 1;
    private static final Integer UPDATED_ID_MOYEN_SUB = 2;

    private static final String DEFAULT_NOM_PREC = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PREC = "BBBBBBBBBB";

    private static final String DEFAULT_LIEU_NAISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISSANCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ETAT_CIVIL_WEB = 1;
    private static final Integer UPDATED_ETAT_CIVIL_WEB = 2;

    private static final Integer DEFAULT_NATION_PREC = 1;
    private static final Integer UPDATED_NATION_PREC = 2;

    private static final Integer DEFAULT_NATION_ACTUEL = 1;
    private static final Integer UPDATED_NATION_ACTUEL = 2;

    private static final String DEFAULT_NOM_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_AUTOR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_AUTOR = "BBBBBBBBBB";

    private static final String DEFAULT_ADRES_AUTOR = "AAAAAAAAAA";
    private static final String UPDATED_ADRES_AUTOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_NATI_AUTOR = 1;
    private static final Integer UPDATED_NATI_AUTOR = 2;

    private static final String DEFAULT_NUM_CIN = "AAAAAAAAAA";
    private static final String UPDATED_NUM_CIN = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_DOC = "AAAAAAAAAA";
    private static final String UPDATED_NUM_DOC = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DELIV_DOC = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DELIV_DOC = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_EXP_DOC = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EXP_DOC = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DELIV_PAR_DOC = "AAAAAAAAAA";
    private static final String UPDATED_DELIV_PAR_DOC = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_DOMICILE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_DOMICILE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_RESIDENT = false;
    private static final Boolean UPDATED_IS_RESIDENT = true;

    private static final String DEFAULT_TITRE_SEJOUR = "AAAAAAAAAA";
    private static final String UPDATED_TITRE_SEJOUR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_EXP_SEJOUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EXP_SEJOUR = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NUM_TEL = "AAAAAAAAAA";
    private static final String UPDATED_NUM_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS_EMP = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS_EMP = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_EMP = "AAAAAAAAAA";
    private static final String UPDATED_TEL_EMP = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_ETAB = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ETAB = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS_ETABLIS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS_ETABLIS = "BBBBBBBBBB";

    private static final Integer DEFAULT_DUREE_SEJOUR = 1;
    private static final Integer UPDATED_DUREE_SEJOUR = 2;

    private static final String DEFAULT_ETAT_MEM_DES = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_MEM_DES = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT_MEM_PREMIER = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_MEM_PREMIER = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_ENTRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_ENTRE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OLD_VISA_EXISTE = false;
    private static final Boolean UPDATED_OLD_VISA_EXISTE = true;

    private static final LocalDate DEFAULT_DATE_DELIV_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DELIV_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_DELIV_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DELIV_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_EMPR_DEGIT = false;
    private static final Boolean UPDATED_IS_EMPR_DEGIT = true;

    private static final LocalDate DEFAULT_DATE_EMPREINT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EMPREINT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_DELIV_AUTOR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DELIV_AUTOR = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_VALIDE_ATOR_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALIDE_ATOR_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_VALIDE_AUTOR_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALIDE_AUTOR_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_ARRIV_PREVU = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ARRIV_PREVU = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_DEPART_PREVU = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEPART_PREVU = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESC_INVITE = "AAAAAAAAAA";
    private static final String UPDATED_DESC_INVITE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_INVIT = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_INVIT = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_INVITE = "AAAAAAAAAA";
    private static final String UPDATED_TEL_INVITE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_INVITE = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_INVITE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_ENTREPRISE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ENTREPRISE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_ENTREPRISE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_ENTREPRISE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_ENTREPRISE = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_ENTREPRISE = "BBBBBBBBBB";

    private static final String DEFAULT_DESC_CONTACT_ENTREPRISE = "AAAAAAAAAA";
    private static final String UPDATED_DESC_CONTACT_ENTREPRISE = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCE_FRAIS_VOYAGE = "AAAAAAAAAA";
    private static final String UPDATED_FINANCE_FRAIS_VOYAGE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_CIT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CIT = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_CIT = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_CIT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISS_CIT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISS_CIT = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_NATION_CIT = 1;
    private static final Integer UPDATED_NATION_CIT = 2;

    private static final String DEFAULT_NUM_DOC_CIT = "AAAAAAAAAA";
    private static final String UPDATED_NUM_DOC_CIT = "BBBBBBBBBB";

    private static final String DEFAULT_LIEU_FORM = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_FORM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_FORM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FORM = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESC_PHOTO_FORM = "AAAAAAAAAA";
    private static final String UPDATED_DESC_PHOTO_FORM = "BBBBBBBBBB";

    private static final String DEFAULT_DESC_FORM_1 = "AAAAAAAAAA";
    private static final String UPDATED_DESC_FORM_1 = "BBBBBBBBBB";

    private static final String DEFAULT_DESC_FORM_2 = "AAAAAAAAAA";
    private static final String UPDATED_DESC_FORM_2 = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_COMP = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_COMP = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_FORM = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FORM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_FORM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_FORM = "BBBBBBBBBB";

    private static final String DEFAULT_ID_PAYS_FORM = "AAAAAAAAAA";
    private static final String UPDATED_ID_PAYS_FORM = "BBBBBBBBBB";

    private static final String DEFAULT_DESC_FORM_3 = "AAAAAAAAAA";
    private static final String UPDATED_DESC_FORM_3 = "BBBBBBBBBB";

    private static final String DEFAULT_DESC_FORM_4 = "AAAAAAAAAA";
    private static final String UPDATED_DESC_FORM_4 = "BBBBBBBBBB";

    private static final String DEFAULT_AUTREETATCIVIL = "AAAAAAAAAA";
    private static final String UPDATED_AUTREETATCIVIL = "BBBBBBBBBB";

    private static final String DEFAULT_AUTREDOC = "AAAAAAAAAA";
    private static final String UPDATED_AUTREDOC = "BBBBBBBBBB";

    private static final String DEFAULT_AUTREOBJ = "AAAAAAAAAA";
    private static final String UPDATED_AUTREOBJ = "BBBBBBBBBB";

    private static final String DEFAULT_AUTREFRIS = "AAAAAAAAAA";
    private static final String UPDATED_AUTREFRIS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCPROMOT = "AAAAAAAAAA";
    private static final String UPDATED_DESCPROMOT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCINVIT = "AAAAAAAAAA";
    private static final String UPDATED_DESCINVIT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_VISA = false;
    private static final Boolean UPDATED_IS_VISA = true;

    private static final LocalDate DEFAULT_DATEAUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEAUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATECREATEFO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATECREATEFO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TEL_FORM = "AAAAAAAAAA";
    private static final String UPDATED_TEL_FORM = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSION_FORM = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION_FORM = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE_FORM = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_FORM = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_NATION = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_NATION = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_AUTORI = "AAAAAAAAAA";
    private static final String UPDATED_TEL_AUTORI = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_AUTORI = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_AUTORI = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_SEJOUR = "AAAAAAAAAA";
    private static final String UPDATED_NUM_SEJOUR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_EMP = "AAAAAAAAAA";
    private static final String UPDATED_NOM_EMP = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_EMP = "AAAAAAAAAA";
    private static final String UPDATED_NUM_EMP = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_VILLE_DESTINATION = "BBBBBBBBBB";

    private static final String DEFAULT_INFO_OBJE_VOYAGE = "AAAAAAAAAA";
    private static final String UPDATED_INFO_OBJE_VOYAGE = "BBBBBBBBBB";

    private static final String DEFAULT_AUTO_DELIVRE_PAR = "AAAAAAAAAA";
    private static final String UPDATED_AUTO_DELIVRE_PAR = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_MOYEN = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_MOYEN = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_LIEN = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_LIEN = "BBBBBBBBBB";

    private static final String DEFAULT_FAX_INVITE = "AAAAAAAAAA";
    private static final String UPDATED_FAX_INVITE = "BBBBBBBBBB";

    private static final String DEFAULT_FAX_ENTREPRISE = "AAAAAAAAAA";
    private static final String UPDATED_FAX_ENTREPRISE = "BBBBBBBBBB";

    private static final String DEFAULT_NAT_ACTUEL = "AAAAAAAAAA";
    private static final String UPDATED_NAT_ACTUEL = "BBBBBBBBBB";

    private static final String DEFAULT_NAT_DIFFER = "AAAAAAAAAA";
    private static final String UPDATED_NAT_DIFFER = "BBBBBBBBBB";

    private static final String DEFAULT_NAT_MINEUR = "AAAAAAAAAA";
    private static final String UPDATED_NAT_MINEUR = "BBBBBBBBBB";

    private static final String DEFAULT_NAT_CITOYEN = "AAAAAAAAAA";
    private static final String UPDATED_NAT_CITOYEN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MOYSUB_1 = false;
    private static final Boolean UPDATED_MOYSUB_1 = true;

    private static final Boolean DEFAULT_MOYSUB_2 = false;
    private static final Boolean UPDATED_MOYSUB_2 = true;

    private static final Boolean DEFAULT_MOYSUB_3 = false;
    private static final Boolean UPDATED_MOYSUB_3 = true;

    private static final Boolean DEFAULT_MOYSUB_4 = false;
    private static final Boolean UPDATED_MOYSUB_4 = true;

    private static final Boolean DEFAULT_MOYSUB_5 = false;
    private static final Boolean UPDATED_MOYSUB_5 = true;

    private static final Boolean DEFAULT_MOYSUB_8 = false;
    private static final Boolean UPDATED_MOYSUB_8 = true;

    private static final Boolean DEFAULT_MOYSUBS_1 = false;
    private static final Boolean UPDATED_MOYSUBS_1 = true;

    private static final Boolean DEFAULT_MOYSUBS_5 = false;
    private static final Boolean UPDATED_MOYSUBS_5 = true;

    private static final Boolean DEFAULT_MOYSUBS_6 = false;
    private static final Boolean UPDATED_MOYSUBS_6 = true;

    private static final Boolean DEFAULT_MOYSUBS_7 = false;
    private static final Boolean UPDATED_MOYSUBS_7 = true;

    private static final Boolean DEFAULT_MOYSUBS_8 = false;
    private static final Boolean UPDATED_MOYSUBS_8 = true;

    private static final String DEFAULT_AUTRE_FRAIS = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_FRAIS = "BBBBBBBBBB";

    private static final Integer DEFAULT_ETAT_CIVIL = 1;
    private static final Integer UPDATED_ETAT_CIVIL = 2;

    private static final Integer DEFAULT_ID_LIEN_F = 1;
    private static final Integer UPDATED_ID_LIEN_F = 2;

    private static final Integer DEFAULT_OBJ_PERSON = 1;
    private static final Integer UPDATED_OBJ_PERSON = 2;

    private static final Integer DEFAULT_ID_SEXE_AVS = 1;
    private static final Integer UPDATED_ID_SEXE_AVS = 2;

    private static final Integer DEFAULT_TYPE_DOC_V = 1;
    private static final Integer UPDATED_TYPE_DOC_V = 2;

    private static final Integer DEFAULT_TYPE_FINANCE = 1;
    private static final Integer UPDATED_TYPE_FINANCE = 2;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/people";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonRepository personRepository;

    /**
     * This repository is mocked in the com.almaviva.vs.repository.search test package.
     *
     * @see com.almaviva.vs.repository.search.PersonSearchRepositoryMockConfiguration
     */
    @Autowired
    private PersonSearchRepository mockPersonSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonMockMvc;

    private Person person;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createEntity(EntityManager em) {
        Person person = new Person()
            .idMoyenSub(DEFAULT_ID_MOYEN_SUB)
            .nomPrec(DEFAULT_NOM_PREC)
            .lieuNaissance(DEFAULT_LIEU_NAISSANCE)
            .etatCivilWeb(DEFAULT_ETAT_CIVIL_WEB)
            .nationPrec(DEFAULT_NATION_PREC)
            .nationActuel(DEFAULT_NATION_ACTUEL)
            .nomAutor(DEFAULT_NOM_AUTOR)
            .prenomAutor(DEFAULT_PRENOM_AUTOR)
            .adresAutor(DEFAULT_ADRES_AUTOR)
            .natiAutor(DEFAULT_NATI_AUTOR)
            .numCin(DEFAULT_NUM_CIN)
            .numDoc(DEFAULT_NUM_DOC)
            .dateDelivDoc(DEFAULT_DATE_DELIV_DOC)
            .dateExpDoc(DEFAULT_DATE_EXP_DOC)
            .delivParDoc(DEFAULT_DELIV_PAR_DOC)
            .adresseDomicile(DEFAULT_ADRESSE_DOMICILE)
            .adresseEmail(DEFAULT_ADRESSE_EMAIL)
            .isResident(DEFAULT_IS_RESIDENT)
            .titreSejour(DEFAULT_TITRE_SEJOUR)
            .dateExpSejour(DEFAULT_DATE_EXP_SEJOUR)
            .numTel(DEFAULT_NUM_TEL)
            .adressEmp(DEFAULT_ADRESS_EMP)
            .telEmp(DEFAULT_TEL_EMP)
            .nomEtab(DEFAULT_NOM_ETAB)
            .adressEtablis(DEFAULT_ADRESS_ETABLIS)
            .dureeSejour(DEFAULT_DUREE_SEJOUR)
            .etatMemDes(DEFAULT_ETAT_MEM_DES)
            .etatMemPremier(DEFAULT_ETAT_MEM_PREMIER)
            .nombreEntre(DEFAULT_NOMBRE_ENTRE)
            .oldVisaExiste(DEFAULT_OLD_VISA_EXISTE)
            .dateDelivDebut(DEFAULT_DATE_DELIV_DEBUT)
            .dateDelivFin(DEFAULT_DATE_DELIV_FIN)
            .isEmprDegit(DEFAULT_IS_EMPR_DEGIT)
            .dateEmpreint(DEFAULT_DATE_EMPREINT)
            .dateDelivAutor(DEFAULT_DATE_DELIV_AUTOR)
            .dateValideAtorDebut(DEFAULT_DATE_VALIDE_ATOR_DEBUT)
            .dateValideAutorFin(DEFAULT_DATE_VALIDE_AUTOR_FIN)
            .dateArrivPrevu(DEFAULT_DATE_ARRIV_PREVU)
            .dateDepartPrevu(DEFAULT_DATE_DEPART_PREVU)
            .descInvite(DEFAULT_DESC_INVITE)
            .adresseInvit(DEFAULT_ADRESSE_INVIT)
            .telInvite(DEFAULT_TEL_INVITE)
            .emailInvite(DEFAULT_EMAIL_INVITE)
            .nomEntreprise(DEFAULT_NOM_ENTREPRISE)
            .adresseEntreprise(DEFAULT_ADRESSE_ENTREPRISE)
            .contactEntreprise(DEFAULT_CONTACT_ENTREPRISE)
            .descContactEntreprise(DEFAULT_DESC_CONTACT_ENTREPRISE)
            .financeFraisVoyage(DEFAULT_FINANCE_FRAIS_VOYAGE)
            .nomCit(DEFAULT_NOM_CIT)
            .prenomCit(DEFAULT_PRENOM_CIT)
            .dateNaissCit(DEFAULT_DATE_NAISS_CIT)
            .nationCit(DEFAULT_NATION_CIT)
            .numDocCit(DEFAULT_NUM_DOC_CIT)
            .lieuForm(DEFAULT_LIEU_FORM)
            .dateForm(DEFAULT_DATE_FORM)
            .descPhotoForm(DEFAULT_DESC_PHOTO_FORM)
            .descForm1(DEFAULT_DESC_FORM_1)
            .descForm2(DEFAULT_DESC_FORM_2)
            .emailComp(DEFAULT_EMAIL_COMP)
            .nomForm(DEFAULT_NOM_FORM)
            .prenomForm(DEFAULT_PRENOM_FORM)
            .idPaysForm(DEFAULT_ID_PAYS_FORM)
            .descForm3(DEFAULT_DESC_FORM_3)
            .descForm4(DEFAULT_DESC_FORM_4)
            .autreetatcivil(DEFAULT_AUTREETATCIVIL)
            .autredoc(DEFAULT_AUTREDOC)
            .autreobj(DEFAULT_AUTREOBJ)
            .autrefris(DEFAULT_AUTREFRIS)
            .descpromot(DEFAULT_DESCPROMOT)
            .descinvit(DEFAULT_DESCINVIT)
            .isVisa(DEFAULT_IS_VISA)
            .dateaut(DEFAULT_DATEAUT)
            .datecreatefo(DEFAULT_DATECREATEFO)
            .telForm(DEFAULT_TEL_FORM)
            .professionForm(DEFAULT_PROFESSION_FORM)
            .referenceForm(DEFAULT_REFERENCE_FORM)
            .otherNation(DEFAULT_OTHER_NATION)
            .telAutori(DEFAULT_TEL_AUTORI)
            .emailAutori(DEFAULT_EMAIL_AUTORI)
            .numSejour(DEFAULT_NUM_SEJOUR)
            .nomEmp(DEFAULT_NOM_EMP)
            .numEmp(DEFAULT_NUM_EMP)
            .villeDestination(DEFAULT_VILLE_DESTINATION)
            .infoObjeVoyage(DEFAULT_INFO_OBJE_VOYAGE)
            .autoDelivrePar(DEFAULT_AUTO_DELIVRE_PAR)
            .otherMoyen(DEFAULT_OTHER_MOYEN)
            .otherLien(DEFAULT_OTHER_LIEN)
            .faxInvite(DEFAULT_FAX_INVITE)
            .faxEntreprise(DEFAULT_FAX_ENTREPRISE)
            .natActuel(DEFAULT_NAT_ACTUEL)
            .natDiffer(DEFAULT_NAT_DIFFER)
            .natMineur(DEFAULT_NAT_MINEUR)
            .natCitoyen(DEFAULT_NAT_CITOYEN)
            .moysub1(DEFAULT_MOYSUB_1)
            .moysub2(DEFAULT_MOYSUB_2)
            .moysub3(DEFAULT_MOYSUB_3)
            .moysub4(DEFAULT_MOYSUB_4)
            .moysub5(DEFAULT_MOYSUB_5)
            .moysub8(DEFAULT_MOYSUB_8)
            .moysubs1(DEFAULT_MOYSUBS_1)
            .moysubs5(DEFAULT_MOYSUBS_5)
            .moysubs6(DEFAULT_MOYSUBS_6)
            .moysubs7(DEFAULT_MOYSUBS_7)
            .moysubs8(DEFAULT_MOYSUBS_8)
            .autreFrais(DEFAULT_AUTRE_FRAIS)
            .etatCivil(DEFAULT_ETAT_CIVIL)
            .idLienF(DEFAULT_ID_LIEN_F)
            .objPerson(DEFAULT_OBJ_PERSON)
            .idSexeAvs(DEFAULT_ID_SEXE_AVS)
            .typeDocV(DEFAULT_TYPE_DOC_V)
            .typeFinance(DEFAULT_TYPE_FINANCE)
            .deleted(DEFAULT_DELETED);
        return person;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createUpdatedEntity(EntityManager em) {
        Person person = new Person()
            .idMoyenSub(UPDATED_ID_MOYEN_SUB)
            .nomPrec(UPDATED_NOM_PREC)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .etatCivilWeb(UPDATED_ETAT_CIVIL_WEB)
            .nationPrec(UPDATED_NATION_PREC)
            .nationActuel(UPDATED_NATION_ACTUEL)
            .nomAutor(UPDATED_NOM_AUTOR)
            .prenomAutor(UPDATED_PRENOM_AUTOR)
            .adresAutor(UPDATED_ADRES_AUTOR)
            .natiAutor(UPDATED_NATI_AUTOR)
            .numCin(UPDATED_NUM_CIN)
            .numDoc(UPDATED_NUM_DOC)
            .dateDelivDoc(UPDATED_DATE_DELIV_DOC)
            .dateExpDoc(UPDATED_DATE_EXP_DOC)
            .delivParDoc(UPDATED_DELIV_PAR_DOC)
            .adresseDomicile(UPDATED_ADRESSE_DOMICILE)
            .adresseEmail(UPDATED_ADRESSE_EMAIL)
            .isResident(UPDATED_IS_RESIDENT)
            .titreSejour(UPDATED_TITRE_SEJOUR)
            .dateExpSejour(UPDATED_DATE_EXP_SEJOUR)
            .numTel(UPDATED_NUM_TEL)
            .adressEmp(UPDATED_ADRESS_EMP)
            .telEmp(UPDATED_TEL_EMP)
            .nomEtab(UPDATED_NOM_ETAB)
            .adressEtablis(UPDATED_ADRESS_ETABLIS)
            .dureeSejour(UPDATED_DUREE_SEJOUR)
            .etatMemDes(UPDATED_ETAT_MEM_DES)
            .etatMemPremier(UPDATED_ETAT_MEM_PREMIER)
            .nombreEntre(UPDATED_NOMBRE_ENTRE)
            .oldVisaExiste(UPDATED_OLD_VISA_EXISTE)
            .dateDelivDebut(UPDATED_DATE_DELIV_DEBUT)
            .dateDelivFin(UPDATED_DATE_DELIV_FIN)
            .isEmprDegit(UPDATED_IS_EMPR_DEGIT)
            .dateEmpreint(UPDATED_DATE_EMPREINT)
            .dateDelivAutor(UPDATED_DATE_DELIV_AUTOR)
            .dateValideAtorDebut(UPDATED_DATE_VALIDE_ATOR_DEBUT)
            .dateValideAutorFin(UPDATED_DATE_VALIDE_AUTOR_FIN)
            .dateArrivPrevu(UPDATED_DATE_ARRIV_PREVU)
            .dateDepartPrevu(UPDATED_DATE_DEPART_PREVU)
            .descInvite(UPDATED_DESC_INVITE)
            .adresseInvit(UPDATED_ADRESSE_INVIT)
            .telInvite(UPDATED_TEL_INVITE)
            .emailInvite(UPDATED_EMAIL_INVITE)
            .nomEntreprise(UPDATED_NOM_ENTREPRISE)
            .adresseEntreprise(UPDATED_ADRESSE_ENTREPRISE)
            .contactEntreprise(UPDATED_CONTACT_ENTREPRISE)
            .descContactEntreprise(UPDATED_DESC_CONTACT_ENTREPRISE)
            .financeFraisVoyage(UPDATED_FINANCE_FRAIS_VOYAGE)
            .nomCit(UPDATED_NOM_CIT)
            .prenomCit(UPDATED_PRENOM_CIT)
            .dateNaissCit(UPDATED_DATE_NAISS_CIT)
            .nationCit(UPDATED_NATION_CIT)
            .numDocCit(UPDATED_NUM_DOC_CIT)
            .lieuForm(UPDATED_LIEU_FORM)
            .dateForm(UPDATED_DATE_FORM)
            .descPhotoForm(UPDATED_DESC_PHOTO_FORM)
            .descForm1(UPDATED_DESC_FORM_1)
            .descForm2(UPDATED_DESC_FORM_2)
            .emailComp(UPDATED_EMAIL_COMP)
            .nomForm(UPDATED_NOM_FORM)
            .prenomForm(UPDATED_PRENOM_FORM)
            .idPaysForm(UPDATED_ID_PAYS_FORM)
            .descForm3(UPDATED_DESC_FORM_3)
            .descForm4(UPDATED_DESC_FORM_4)
            .autreetatcivil(UPDATED_AUTREETATCIVIL)
            .autredoc(UPDATED_AUTREDOC)
            .autreobj(UPDATED_AUTREOBJ)
            .autrefris(UPDATED_AUTREFRIS)
            .descpromot(UPDATED_DESCPROMOT)
            .descinvit(UPDATED_DESCINVIT)
            .isVisa(UPDATED_IS_VISA)
            .dateaut(UPDATED_DATEAUT)
            .datecreatefo(UPDATED_DATECREATEFO)
            .telForm(UPDATED_TEL_FORM)
            .professionForm(UPDATED_PROFESSION_FORM)
            .referenceForm(UPDATED_REFERENCE_FORM)
            .otherNation(UPDATED_OTHER_NATION)
            .telAutori(UPDATED_TEL_AUTORI)
            .emailAutori(UPDATED_EMAIL_AUTORI)
            .numSejour(UPDATED_NUM_SEJOUR)
            .nomEmp(UPDATED_NOM_EMP)
            .numEmp(UPDATED_NUM_EMP)
            .villeDestination(UPDATED_VILLE_DESTINATION)
            .infoObjeVoyage(UPDATED_INFO_OBJE_VOYAGE)
            .autoDelivrePar(UPDATED_AUTO_DELIVRE_PAR)
            .otherMoyen(UPDATED_OTHER_MOYEN)
            .otherLien(UPDATED_OTHER_LIEN)
            .faxInvite(UPDATED_FAX_INVITE)
            .faxEntreprise(UPDATED_FAX_ENTREPRISE)
            .natActuel(UPDATED_NAT_ACTUEL)
            .natDiffer(UPDATED_NAT_DIFFER)
            .natMineur(UPDATED_NAT_MINEUR)
            .natCitoyen(UPDATED_NAT_CITOYEN)
            .moysub1(UPDATED_MOYSUB_1)
            .moysub2(UPDATED_MOYSUB_2)
            .moysub3(UPDATED_MOYSUB_3)
            .moysub4(UPDATED_MOYSUB_4)
            .moysub5(UPDATED_MOYSUB_5)
            .moysub8(UPDATED_MOYSUB_8)
            .moysubs1(UPDATED_MOYSUBS_1)
            .moysubs5(UPDATED_MOYSUBS_5)
            .moysubs6(UPDATED_MOYSUBS_6)
            .moysubs7(UPDATED_MOYSUBS_7)
            .moysubs8(UPDATED_MOYSUBS_8)
            .autreFrais(UPDATED_AUTRE_FRAIS)
            .etatCivil(UPDATED_ETAT_CIVIL)
            .idLienF(UPDATED_ID_LIEN_F)
            .objPerson(UPDATED_OBJ_PERSON)
            .idSexeAvs(UPDATED_ID_SEXE_AVS)
            .typeDocV(UPDATED_TYPE_DOC_V)
            .typeFinance(UPDATED_TYPE_FINANCE)
            .deleted(UPDATED_DELETED);
        return person;
    }

    @BeforeEach
    public void initTest() {
        person = createEntity(em);
    }

    @Test
    @Transactional
    void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();
        // Create the Person
        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getIdMoyenSub()).isEqualTo(DEFAULT_ID_MOYEN_SUB);
        assertThat(testPerson.getNomPrec()).isEqualTo(DEFAULT_NOM_PREC);
        assertThat(testPerson.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
        assertThat(testPerson.getEtatCivilWeb()).isEqualTo(DEFAULT_ETAT_CIVIL_WEB);
        assertThat(testPerson.getNationPrec()).isEqualTo(DEFAULT_NATION_PREC);
        assertThat(testPerson.getNationActuel()).isEqualTo(DEFAULT_NATION_ACTUEL);
        assertThat(testPerson.getNomAutor()).isEqualTo(DEFAULT_NOM_AUTOR);
        assertThat(testPerson.getPrenomAutor()).isEqualTo(DEFAULT_PRENOM_AUTOR);
        assertThat(testPerson.getAdresAutor()).isEqualTo(DEFAULT_ADRES_AUTOR);
        assertThat(testPerson.getNatiAutor()).isEqualTo(DEFAULT_NATI_AUTOR);
        assertThat(testPerson.getNumCin()).isEqualTo(DEFAULT_NUM_CIN);
        assertThat(testPerson.getNumDoc()).isEqualTo(DEFAULT_NUM_DOC);
        assertThat(testPerson.getDateDelivDoc()).isEqualTo(DEFAULT_DATE_DELIV_DOC);
        assertThat(testPerson.getDateExpDoc()).isEqualTo(DEFAULT_DATE_EXP_DOC);
        assertThat(testPerson.getDelivParDoc()).isEqualTo(DEFAULT_DELIV_PAR_DOC);
        assertThat(testPerson.getAdresseDomicile()).isEqualTo(DEFAULT_ADRESSE_DOMICILE);
        assertThat(testPerson.getAdresseEmail()).isEqualTo(DEFAULT_ADRESSE_EMAIL);
        assertThat(testPerson.getIsResident()).isEqualTo(DEFAULT_IS_RESIDENT);
        assertThat(testPerson.getTitreSejour()).isEqualTo(DEFAULT_TITRE_SEJOUR);
        assertThat(testPerson.getDateExpSejour()).isEqualTo(DEFAULT_DATE_EXP_SEJOUR);
        assertThat(testPerson.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
        assertThat(testPerson.getAdressEmp()).isEqualTo(DEFAULT_ADRESS_EMP);
        assertThat(testPerson.getTelEmp()).isEqualTo(DEFAULT_TEL_EMP);
        assertThat(testPerson.getNomEtab()).isEqualTo(DEFAULT_NOM_ETAB);
        assertThat(testPerson.getAdressEtablis()).isEqualTo(DEFAULT_ADRESS_ETABLIS);
        assertThat(testPerson.getDureeSejour()).isEqualTo(DEFAULT_DUREE_SEJOUR);
        assertThat(testPerson.getEtatMemDes()).isEqualTo(DEFAULT_ETAT_MEM_DES);
        assertThat(testPerson.getEtatMemPremier()).isEqualTo(DEFAULT_ETAT_MEM_PREMIER);
        assertThat(testPerson.getNombreEntre()).isEqualTo(DEFAULT_NOMBRE_ENTRE);
        assertThat(testPerson.getOldVisaExiste()).isEqualTo(DEFAULT_OLD_VISA_EXISTE);
        assertThat(testPerson.getDateDelivDebut()).isEqualTo(DEFAULT_DATE_DELIV_DEBUT);
        assertThat(testPerson.getDateDelivFin()).isEqualTo(DEFAULT_DATE_DELIV_FIN);
        assertThat(testPerson.getIsEmprDegit()).isEqualTo(DEFAULT_IS_EMPR_DEGIT);
        assertThat(testPerson.getDateEmpreint()).isEqualTo(DEFAULT_DATE_EMPREINT);
        assertThat(testPerson.getDateDelivAutor()).isEqualTo(DEFAULT_DATE_DELIV_AUTOR);
        assertThat(testPerson.getDateValideAtorDebut()).isEqualTo(DEFAULT_DATE_VALIDE_ATOR_DEBUT);
        assertThat(testPerson.getDateValideAutorFin()).isEqualTo(DEFAULT_DATE_VALIDE_AUTOR_FIN);
        assertThat(testPerson.getDateArrivPrevu()).isEqualTo(DEFAULT_DATE_ARRIV_PREVU);
        assertThat(testPerson.getDateDepartPrevu()).isEqualTo(DEFAULT_DATE_DEPART_PREVU);
        assertThat(testPerson.getDescInvite()).isEqualTo(DEFAULT_DESC_INVITE);
        assertThat(testPerson.getAdresseInvit()).isEqualTo(DEFAULT_ADRESSE_INVIT);
        assertThat(testPerson.getTelInvite()).isEqualTo(DEFAULT_TEL_INVITE);
        assertThat(testPerson.getEmailInvite()).isEqualTo(DEFAULT_EMAIL_INVITE);
        assertThat(testPerson.getNomEntreprise()).isEqualTo(DEFAULT_NOM_ENTREPRISE);
        assertThat(testPerson.getAdresseEntreprise()).isEqualTo(DEFAULT_ADRESSE_ENTREPRISE);
        assertThat(testPerson.getContactEntreprise()).isEqualTo(DEFAULT_CONTACT_ENTREPRISE);
        assertThat(testPerson.getDescContactEntreprise()).isEqualTo(DEFAULT_DESC_CONTACT_ENTREPRISE);
        assertThat(testPerson.getFinanceFraisVoyage()).isEqualTo(DEFAULT_FINANCE_FRAIS_VOYAGE);
        assertThat(testPerson.getNomCit()).isEqualTo(DEFAULT_NOM_CIT);
        assertThat(testPerson.getPrenomCit()).isEqualTo(DEFAULT_PRENOM_CIT);
        assertThat(testPerson.getDateNaissCit()).isEqualTo(DEFAULT_DATE_NAISS_CIT);
        assertThat(testPerson.getNationCit()).isEqualTo(DEFAULT_NATION_CIT);
        assertThat(testPerson.getNumDocCit()).isEqualTo(DEFAULT_NUM_DOC_CIT);
        assertThat(testPerson.getLieuForm()).isEqualTo(DEFAULT_LIEU_FORM);
        assertThat(testPerson.getDateForm()).isEqualTo(DEFAULT_DATE_FORM);
        assertThat(testPerson.getDescPhotoForm()).isEqualTo(DEFAULT_DESC_PHOTO_FORM);
        assertThat(testPerson.getDescForm1()).isEqualTo(DEFAULT_DESC_FORM_1);
        assertThat(testPerson.getDescForm2()).isEqualTo(DEFAULT_DESC_FORM_2);
        assertThat(testPerson.getEmailComp()).isEqualTo(DEFAULT_EMAIL_COMP);
        assertThat(testPerson.getNomForm()).isEqualTo(DEFAULT_NOM_FORM);
        assertThat(testPerson.getPrenomForm()).isEqualTo(DEFAULT_PRENOM_FORM);
        assertThat(testPerson.getIdPaysForm()).isEqualTo(DEFAULT_ID_PAYS_FORM);
        assertThat(testPerson.getDescForm3()).isEqualTo(DEFAULT_DESC_FORM_3);
        assertThat(testPerson.getDescForm4()).isEqualTo(DEFAULT_DESC_FORM_4);
        assertThat(testPerson.getAutreetatcivil()).isEqualTo(DEFAULT_AUTREETATCIVIL);
        assertThat(testPerson.getAutredoc()).isEqualTo(DEFAULT_AUTREDOC);
        assertThat(testPerson.getAutreobj()).isEqualTo(DEFAULT_AUTREOBJ);
        assertThat(testPerson.getAutrefris()).isEqualTo(DEFAULT_AUTREFRIS);
        assertThat(testPerson.getDescpromot()).isEqualTo(DEFAULT_DESCPROMOT);
        assertThat(testPerson.getDescinvit()).isEqualTo(DEFAULT_DESCINVIT);
        assertThat(testPerson.getIsVisa()).isEqualTo(DEFAULT_IS_VISA);
        assertThat(testPerson.getDateaut()).isEqualTo(DEFAULT_DATEAUT);
        assertThat(testPerson.getDatecreatefo()).isEqualTo(DEFAULT_DATECREATEFO);
        assertThat(testPerson.getTelForm()).isEqualTo(DEFAULT_TEL_FORM);
        assertThat(testPerson.getProfessionForm()).isEqualTo(DEFAULT_PROFESSION_FORM);
        assertThat(testPerson.getReferenceForm()).isEqualTo(DEFAULT_REFERENCE_FORM);
        assertThat(testPerson.getOtherNation()).isEqualTo(DEFAULT_OTHER_NATION);
        assertThat(testPerson.getTelAutori()).isEqualTo(DEFAULT_TEL_AUTORI);
        assertThat(testPerson.getEmailAutori()).isEqualTo(DEFAULT_EMAIL_AUTORI);
        assertThat(testPerson.getNumSejour()).isEqualTo(DEFAULT_NUM_SEJOUR);
        assertThat(testPerson.getNomEmp()).isEqualTo(DEFAULT_NOM_EMP);
        assertThat(testPerson.getNumEmp()).isEqualTo(DEFAULT_NUM_EMP);
        assertThat(testPerson.getVilleDestination()).isEqualTo(DEFAULT_VILLE_DESTINATION);
        assertThat(testPerson.getInfoObjeVoyage()).isEqualTo(DEFAULT_INFO_OBJE_VOYAGE);
        assertThat(testPerson.getAutoDelivrePar()).isEqualTo(DEFAULT_AUTO_DELIVRE_PAR);
        assertThat(testPerson.getOtherMoyen()).isEqualTo(DEFAULT_OTHER_MOYEN);
        assertThat(testPerson.getOtherLien()).isEqualTo(DEFAULT_OTHER_LIEN);
        assertThat(testPerson.getFaxInvite()).isEqualTo(DEFAULT_FAX_INVITE);
        assertThat(testPerson.getFaxEntreprise()).isEqualTo(DEFAULT_FAX_ENTREPRISE);
        assertThat(testPerson.getNatActuel()).isEqualTo(DEFAULT_NAT_ACTUEL);
        assertThat(testPerson.getNatDiffer()).isEqualTo(DEFAULT_NAT_DIFFER);
        assertThat(testPerson.getNatMineur()).isEqualTo(DEFAULT_NAT_MINEUR);
        assertThat(testPerson.getNatCitoyen()).isEqualTo(DEFAULT_NAT_CITOYEN);
        assertThat(testPerson.getMoysub1()).isEqualTo(DEFAULT_MOYSUB_1);
        assertThat(testPerson.getMoysub2()).isEqualTo(DEFAULT_MOYSUB_2);
        assertThat(testPerson.getMoysub3()).isEqualTo(DEFAULT_MOYSUB_3);
        assertThat(testPerson.getMoysub4()).isEqualTo(DEFAULT_MOYSUB_4);
        assertThat(testPerson.getMoysub5()).isEqualTo(DEFAULT_MOYSUB_5);
        assertThat(testPerson.getMoysub8()).isEqualTo(DEFAULT_MOYSUB_8);
        assertThat(testPerson.getMoysubs1()).isEqualTo(DEFAULT_MOYSUBS_1);
        assertThat(testPerson.getMoysubs5()).isEqualTo(DEFAULT_MOYSUBS_5);
        assertThat(testPerson.getMoysubs6()).isEqualTo(DEFAULT_MOYSUBS_6);
        assertThat(testPerson.getMoysubs7()).isEqualTo(DEFAULT_MOYSUBS_7);
        assertThat(testPerson.getMoysubs8()).isEqualTo(DEFAULT_MOYSUBS_8);
        assertThat(testPerson.getAutreFrais()).isEqualTo(DEFAULT_AUTRE_FRAIS);
        assertThat(testPerson.getEtatCivil()).isEqualTo(DEFAULT_ETAT_CIVIL);
        assertThat(testPerson.getIdLienF()).isEqualTo(DEFAULT_ID_LIEN_F);
        assertThat(testPerson.getObjPerson()).isEqualTo(DEFAULT_OBJ_PERSON);
        assertThat(testPerson.getIdSexeAvs()).isEqualTo(DEFAULT_ID_SEXE_AVS);
        assertThat(testPerson.getTypeDocV()).isEqualTo(DEFAULT_TYPE_DOC_V);
        assertThat(testPerson.getTypeFinance()).isEqualTo(DEFAULT_TYPE_FINANCE);
        assertThat(testPerson.getDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the Person in Elasticsearch
        verify(mockPersonSearchRepository, times(1)).save(testPerson);
    }

    @Test
    @Transactional
    void createPersonWithExistingId() throws Exception {
        // Create the Person with an existing ID
        person.setId(1L);

        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);

        // Validate the Person in Elasticsearch
        verify(mockPersonSearchRepository, times(0)).save(person);
    }

    @Test
    @Transactional
    void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].idMoyenSub").value(hasItem(DEFAULT_ID_MOYEN_SUB)))
            .andExpect(jsonPath("$.[*].nomPrec").value(hasItem(DEFAULT_NOM_PREC)))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE)))
            .andExpect(jsonPath("$.[*].etatCivilWeb").value(hasItem(DEFAULT_ETAT_CIVIL_WEB)))
            .andExpect(jsonPath("$.[*].nationPrec").value(hasItem(DEFAULT_NATION_PREC)))
            .andExpect(jsonPath("$.[*].nationActuel").value(hasItem(DEFAULT_NATION_ACTUEL)))
            .andExpect(jsonPath("$.[*].nomAutor").value(hasItem(DEFAULT_NOM_AUTOR)))
            .andExpect(jsonPath("$.[*].prenomAutor").value(hasItem(DEFAULT_PRENOM_AUTOR)))
            .andExpect(jsonPath("$.[*].adresAutor").value(hasItem(DEFAULT_ADRES_AUTOR)))
            .andExpect(jsonPath("$.[*].natiAutor").value(hasItem(DEFAULT_NATI_AUTOR)))
            .andExpect(jsonPath("$.[*].numCin").value(hasItem(DEFAULT_NUM_CIN)))
            .andExpect(jsonPath("$.[*].numDoc").value(hasItem(DEFAULT_NUM_DOC)))
            .andExpect(jsonPath("$.[*].dateDelivDoc").value(hasItem(DEFAULT_DATE_DELIV_DOC.toString())))
            .andExpect(jsonPath("$.[*].dateExpDoc").value(hasItem(DEFAULT_DATE_EXP_DOC.toString())))
            .andExpect(jsonPath("$.[*].delivParDoc").value(hasItem(DEFAULT_DELIV_PAR_DOC)))
            .andExpect(jsonPath("$.[*].adresseDomicile").value(hasItem(DEFAULT_ADRESSE_DOMICILE)))
            .andExpect(jsonPath("$.[*].adresseEmail").value(hasItem(DEFAULT_ADRESSE_EMAIL)))
            .andExpect(jsonPath("$.[*].isResident").value(hasItem(DEFAULT_IS_RESIDENT.booleanValue())))
            .andExpect(jsonPath("$.[*].titreSejour").value(hasItem(DEFAULT_TITRE_SEJOUR)))
            .andExpect(jsonPath("$.[*].dateExpSejour").value(hasItem(DEFAULT_DATE_EXP_SEJOUR.toString())))
            .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL)))
            .andExpect(jsonPath("$.[*].adressEmp").value(hasItem(DEFAULT_ADRESS_EMP)))
            .andExpect(jsonPath("$.[*].telEmp").value(hasItem(DEFAULT_TEL_EMP)))
            .andExpect(jsonPath("$.[*].nomEtab").value(hasItem(DEFAULT_NOM_ETAB)))
            .andExpect(jsonPath("$.[*].adressEtablis").value(hasItem(DEFAULT_ADRESS_ETABLIS)))
            .andExpect(jsonPath("$.[*].dureeSejour").value(hasItem(DEFAULT_DUREE_SEJOUR)))
            .andExpect(jsonPath("$.[*].etatMemDes").value(hasItem(DEFAULT_ETAT_MEM_DES)))
            .andExpect(jsonPath("$.[*].etatMemPremier").value(hasItem(DEFAULT_ETAT_MEM_PREMIER)))
            .andExpect(jsonPath("$.[*].nombreEntre").value(hasItem(DEFAULT_NOMBRE_ENTRE)))
            .andExpect(jsonPath("$.[*].oldVisaExiste").value(hasItem(DEFAULT_OLD_VISA_EXISTE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDelivDebut").value(hasItem(DEFAULT_DATE_DELIV_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateDelivFin").value(hasItem(DEFAULT_DATE_DELIV_FIN.toString())))
            .andExpect(jsonPath("$.[*].isEmprDegit").value(hasItem(DEFAULT_IS_EMPR_DEGIT.booleanValue())))
            .andExpect(jsonPath("$.[*].dateEmpreint").value(hasItem(DEFAULT_DATE_EMPREINT.toString())))
            .andExpect(jsonPath("$.[*].dateDelivAutor").value(hasItem(DEFAULT_DATE_DELIV_AUTOR.toString())))
            .andExpect(jsonPath("$.[*].dateValideAtorDebut").value(hasItem(DEFAULT_DATE_VALIDE_ATOR_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateValideAutorFin").value(hasItem(DEFAULT_DATE_VALIDE_AUTOR_FIN.toString())))
            .andExpect(jsonPath("$.[*].dateArrivPrevu").value(hasItem(DEFAULT_DATE_ARRIV_PREVU.toString())))
            .andExpect(jsonPath("$.[*].dateDepartPrevu").value(hasItem(DEFAULT_DATE_DEPART_PREVU.toString())))
            .andExpect(jsonPath("$.[*].descInvite").value(hasItem(DEFAULT_DESC_INVITE)))
            .andExpect(jsonPath("$.[*].adresseInvit").value(hasItem(DEFAULT_ADRESSE_INVIT)))
            .andExpect(jsonPath("$.[*].telInvite").value(hasItem(DEFAULT_TEL_INVITE)))
            .andExpect(jsonPath("$.[*].emailInvite").value(hasItem(DEFAULT_EMAIL_INVITE)))
            .andExpect(jsonPath("$.[*].nomEntreprise").value(hasItem(DEFAULT_NOM_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].adresseEntreprise").value(hasItem(DEFAULT_ADRESSE_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].contactEntreprise").value(hasItem(DEFAULT_CONTACT_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].descContactEntreprise").value(hasItem(DEFAULT_DESC_CONTACT_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].financeFraisVoyage").value(hasItem(DEFAULT_FINANCE_FRAIS_VOYAGE)))
            .andExpect(jsonPath("$.[*].nomCit").value(hasItem(DEFAULT_NOM_CIT)))
            .andExpect(jsonPath("$.[*].prenomCit").value(hasItem(DEFAULT_PRENOM_CIT)))
            .andExpect(jsonPath("$.[*].dateNaissCit").value(hasItem(DEFAULT_DATE_NAISS_CIT.toString())))
            .andExpect(jsonPath("$.[*].nationCit").value(hasItem(DEFAULT_NATION_CIT)))
            .andExpect(jsonPath("$.[*].numDocCit").value(hasItem(DEFAULT_NUM_DOC_CIT)))
            .andExpect(jsonPath("$.[*].lieuForm").value(hasItem(DEFAULT_LIEU_FORM)))
            .andExpect(jsonPath("$.[*].dateForm").value(hasItem(DEFAULT_DATE_FORM.toString())))
            .andExpect(jsonPath("$.[*].descPhotoForm").value(hasItem(DEFAULT_DESC_PHOTO_FORM)))
            .andExpect(jsonPath("$.[*].descForm1").value(hasItem(DEFAULT_DESC_FORM_1)))
            .andExpect(jsonPath("$.[*].descForm2").value(hasItem(DEFAULT_DESC_FORM_2)))
            .andExpect(jsonPath("$.[*].emailComp").value(hasItem(DEFAULT_EMAIL_COMP)))
            .andExpect(jsonPath("$.[*].nomForm").value(hasItem(DEFAULT_NOM_FORM)))
            .andExpect(jsonPath("$.[*].prenomForm").value(hasItem(DEFAULT_PRENOM_FORM)))
            .andExpect(jsonPath("$.[*].idPaysForm").value(hasItem(DEFAULT_ID_PAYS_FORM)))
            .andExpect(jsonPath("$.[*].descForm3").value(hasItem(DEFAULT_DESC_FORM_3)))
            .andExpect(jsonPath("$.[*].descForm4").value(hasItem(DEFAULT_DESC_FORM_4)))
            .andExpect(jsonPath("$.[*].autreetatcivil").value(hasItem(DEFAULT_AUTREETATCIVIL)))
            .andExpect(jsonPath("$.[*].autredoc").value(hasItem(DEFAULT_AUTREDOC)))
            .andExpect(jsonPath("$.[*].autreobj").value(hasItem(DEFAULT_AUTREOBJ)))
            .andExpect(jsonPath("$.[*].autrefris").value(hasItem(DEFAULT_AUTREFRIS)))
            .andExpect(jsonPath("$.[*].descpromot").value(hasItem(DEFAULT_DESCPROMOT)))
            .andExpect(jsonPath("$.[*].descinvit").value(hasItem(DEFAULT_DESCINVIT)))
            .andExpect(jsonPath("$.[*].isVisa").value(hasItem(DEFAULT_IS_VISA.booleanValue())))
            .andExpect(jsonPath("$.[*].dateaut").value(hasItem(DEFAULT_DATEAUT.toString())))
            .andExpect(jsonPath("$.[*].datecreatefo").value(hasItem(DEFAULT_DATECREATEFO.toString())))
            .andExpect(jsonPath("$.[*].telForm").value(hasItem(DEFAULT_TEL_FORM)))
            .andExpect(jsonPath("$.[*].professionForm").value(hasItem(DEFAULT_PROFESSION_FORM)))
            .andExpect(jsonPath("$.[*].referenceForm").value(hasItem(DEFAULT_REFERENCE_FORM)))
            .andExpect(jsonPath("$.[*].otherNation").value(hasItem(DEFAULT_OTHER_NATION)))
            .andExpect(jsonPath("$.[*].telAutori").value(hasItem(DEFAULT_TEL_AUTORI)))
            .andExpect(jsonPath("$.[*].emailAutori").value(hasItem(DEFAULT_EMAIL_AUTORI)))
            .andExpect(jsonPath("$.[*].numSejour").value(hasItem(DEFAULT_NUM_SEJOUR)))
            .andExpect(jsonPath("$.[*].nomEmp").value(hasItem(DEFAULT_NOM_EMP)))
            .andExpect(jsonPath("$.[*].numEmp").value(hasItem(DEFAULT_NUM_EMP)))
            .andExpect(jsonPath("$.[*].villeDestination").value(hasItem(DEFAULT_VILLE_DESTINATION)))
            .andExpect(jsonPath("$.[*].infoObjeVoyage").value(hasItem(DEFAULT_INFO_OBJE_VOYAGE)))
            .andExpect(jsonPath("$.[*].autoDelivrePar").value(hasItem(DEFAULT_AUTO_DELIVRE_PAR)))
            .andExpect(jsonPath("$.[*].otherMoyen").value(hasItem(DEFAULT_OTHER_MOYEN)))
            .andExpect(jsonPath("$.[*].otherLien").value(hasItem(DEFAULT_OTHER_LIEN)))
            .andExpect(jsonPath("$.[*].faxInvite").value(hasItem(DEFAULT_FAX_INVITE)))
            .andExpect(jsonPath("$.[*].faxEntreprise").value(hasItem(DEFAULT_FAX_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].natActuel").value(hasItem(DEFAULT_NAT_ACTUEL)))
            .andExpect(jsonPath("$.[*].natDiffer").value(hasItem(DEFAULT_NAT_DIFFER)))
            .andExpect(jsonPath("$.[*].natMineur").value(hasItem(DEFAULT_NAT_MINEUR)))
            .andExpect(jsonPath("$.[*].natCitoyen").value(hasItem(DEFAULT_NAT_CITOYEN)))
            .andExpect(jsonPath("$.[*].moysub1").value(hasItem(DEFAULT_MOYSUB_1.booleanValue())))
            .andExpect(jsonPath("$.[*].moysub2").value(hasItem(DEFAULT_MOYSUB_2.booleanValue())))
            .andExpect(jsonPath("$.[*].moysub3").value(hasItem(DEFAULT_MOYSUB_3.booleanValue())))
            .andExpect(jsonPath("$.[*].moysub4").value(hasItem(DEFAULT_MOYSUB_4.booleanValue())))
            .andExpect(jsonPath("$.[*].moysub5").value(hasItem(DEFAULT_MOYSUB_5.booleanValue())))
            .andExpect(jsonPath("$.[*].moysub8").value(hasItem(DEFAULT_MOYSUB_8.booleanValue())))
            .andExpect(jsonPath("$.[*].moysubs1").value(hasItem(DEFAULT_MOYSUBS_1.booleanValue())))
            .andExpect(jsonPath("$.[*].moysubs5").value(hasItem(DEFAULT_MOYSUBS_5.booleanValue())))
            .andExpect(jsonPath("$.[*].moysubs6").value(hasItem(DEFAULT_MOYSUBS_6.booleanValue())))
            .andExpect(jsonPath("$.[*].moysubs7").value(hasItem(DEFAULT_MOYSUBS_7.booleanValue())))
            .andExpect(jsonPath("$.[*].moysubs8").value(hasItem(DEFAULT_MOYSUBS_8.booleanValue())))
            .andExpect(jsonPath("$.[*].autreFrais").value(hasItem(DEFAULT_AUTRE_FRAIS)))
            .andExpect(jsonPath("$.[*].etatCivil").value(hasItem(DEFAULT_ETAT_CIVIL)))
            .andExpect(jsonPath("$.[*].idLienF").value(hasItem(DEFAULT_ID_LIEN_F)))
            .andExpect(jsonPath("$.[*].objPerson").value(hasItem(DEFAULT_OBJ_PERSON)))
            .andExpect(jsonPath("$.[*].idSexeAvs").value(hasItem(DEFAULT_ID_SEXE_AVS)))
            .andExpect(jsonPath("$.[*].typeDocV").value(hasItem(DEFAULT_TYPE_DOC_V)))
            .andExpect(jsonPath("$.[*].typeFinance").value(hasItem(DEFAULT_TYPE_FINANCE)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get the person
        restPersonMockMvc
            .perform(get(ENTITY_API_URL_ID, person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(person.getId().intValue()))
            .andExpect(jsonPath("$.idMoyenSub").value(DEFAULT_ID_MOYEN_SUB))
            .andExpect(jsonPath("$.nomPrec").value(DEFAULT_NOM_PREC))
            .andExpect(jsonPath("$.lieuNaissance").value(DEFAULT_LIEU_NAISSANCE))
            .andExpect(jsonPath("$.etatCivilWeb").value(DEFAULT_ETAT_CIVIL_WEB))
            .andExpect(jsonPath("$.nationPrec").value(DEFAULT_NATION_PREC))
            .andExpect(jsonPath("$.nationActuel").value(DEFAULT_NATION_ACTUEL))
            .andExpect(jsonPath("$.nomAutor").value(DEFAULT_NOM_AUTOR))
            .andExpect(jsonPath("$.prenomAutor").value(DEFAULT_PRENOM_AUTOR))
            .andExpect(jsonPath("$.adresAutor").value(DEFAULT_ADRES_AUTOR))
            .andExpect(jsonPath("$.natiAutor").value(DEFAULT_NATI_AUTOR))
            .andExpect(jsonPath("$.numCin").value(DEFAULT_NUM_CIN))
            .andExpect(jsonPath("$.numDoc").value(DEFAULT_NUM_DOC))
            .andExpect(jsonPath("$.dateDelivDoc").value(DEFAULT_DATE_DELIV_DOC.toString()))
            .andExpect(jsonPath("$.dateExpDoc").value(DEFAULT_DATE_EXP_DOC.toString()))
            .andExpect(jsonPath("$.delivParDoc").value(DEFAULT_DELIV_PAR_DOC))
            .andExpect(jsonPath("$.adresseDomicile").value(DEFAULT_ADRESSE_DOMICILE))
            .andExpect(jsonPath("$.adresseEmail").value(DEFAULT_ADRESSE_EMAIL))
            .andExpect(jsonPath("$.isResident").value(DEFAULT_IS_RESIDENT.booleanValue()))
            .andExpect(jsonPath("$.titreSejour").value(DEFAULT_TITRE_SEJOUR))
            .andExpect(jsonPath("$.dateExpSejour").value(DEFAULT_DATE_EXP_SEJOUR.toString()))
            .andExpect(jsonPath("$.numTel").value(DEFAULT_NUM_TEL))
            .andExpect(jsonPath("$.adressEmp").value(DEFAULT_ADRESS_EMP))
            .andExpect(jsonPath("$.telEmp").value(DEFAULT_TEL_EMP))
            .andExpect(jsonPath("$.nomEtab").value(DEFAULT_NOM_ETAB))
            .andExpect(jsonPath("$.adressEtablis").value(DEFAULT_ADRESS_ETABLIS))
            .andExpect(jsonPath("$.dureeSejour").value(DEFAULT_DUREE_SEJOUR))
            .andExpect(jsonPath("$.etatMemDes").value(DEFAULT_ETAT_MEM_DES))
            .andExpect(jsonPath("$.etatMemPremier").value(DEFAULT_ETAT_MEM_PREMIER))
            .andExpect(jsonPath("$.nombreEntre").value(DEFAULT_NOMBRE_ENTRE))
            .andExpect(jsonPath("$.oldVisaExiste").value(DEFAULT_OLD_VISA_EXISTE.booleanValue()))
            .andExpect(jsonPath("$.dateDelivDebut").value(DEFAULT_DATE_DELIV_DEBUT.toString()))
            .andExpect(jsonPath("$.dateDelivFin").value(DEFAULT_DATE_DELIV_FIN.toString()))
            .andExpect(jsonPath("$.isEmprDegit").value(DEFAULT_IS_EMPR_DEGIT.booleanValue()))
            .andExpect(jsonPath("$.dateEmpreint").value(DEFAULT_DATE_EMPREINT.toString()))
            .andExpect(jsonPath("$.dateDelivAutor").value(DEFAULT_DATE_DELIV_AUTOR.toString()))
            .andExpect(jsonPath("$.dateValideAtorDebut").value(DEFAULT_DATE_VALIDE_ATOR_DEBUT.toString()))
            .andExpect(jsonPath("$.dateValideAutorFin").value(DEFAULT_DATE_VALIDE_AUTOR_FIN.toString()))
            .andExpect(jsonPath("$.dateArrivPrevu").value(DEFAULT_DATE_ARRIV_PREVU.toString()))
            .andExpect(jsonPath("$.dateDepartPrevu").value(DEFAULT_DATE_DEPART_PREVU.toString()))
            .andExpect(jsonPath("$.descInvite").value(DEFAULT_DESC_INVITE))
            .andExpect(jsonPath("$.adresseInvit").value(DEFAULT_ADRESSE_INVIT))
            .andExpect(jsonPath("$.telInvite").value(DEFAULT_TEL_INVITE))
            .andExpect(jsonPath("$.emailInvite").value(DEFAULT_EMAIL_INVITE))
            .andExpect(jsonPath("$.nomEntreprise").value(DEFAULT_NOM_ENTREPRISE))
            .andExpect(jsonPath("$.adresseEntreprise").value(DEFAULT_ADRESSE_ENTREPRISE))
            .andExpect(jsonPath("$.contactEntreprise").value(DEFAULT_CONTACT_ENTREPRISE))
            .andExpect(jsonPath("$.descContactEntreprise").value(DEFAULT_DESC_CONTACT_ENTREPRISE))
            .andExpect(jsonPath("$.financeFraisVoyage").value(DEFAULT_FINANCE_FRAIS_VOYAGE))
            .andExpect(jsonPath("$.nomCit").value(DEFAULT_NOM_CIT))
            .andExpect(jsonPath("$.prenomCit").value(DEFAULT_PRENOM_CIT))
            .andExpect(jsonPath("$.dateNaissCit").value(DEFAULT_DATE_NAISS_CIT.toString()))
            .andExpect(jsonPath("$.nationCit").value(DEFAULT_NATION_CIT))
            .andExpect(jsonPath("$.numDocCit").value(DEFAULT_NUM_DOC_CIT))
            .andExpect(jsonPath("$.lieuForm").value(DEFAULT_LIEU_FORM))
            .andExpect(jsonPath("$.dateForm").value(DEFAULT_DATE_FORM.toString()))
            .andExpect(jsonPath("$.descPhotoForm").value(DEFAULT_DESC_PHOTO_FORM))
            .andExpect(jsonPath("$.descForm1").value(DEFAULT_DESC_FORM_1))
            .andExpect(jsonPath("$.descForm2").value(DEFAULT_DESC_FORM_2))
            .andExpect(jsonPath("$.emailComp").value(DEFAULT_EMAIL_COMP))
            .andExpect(jsonPath("$.nomForm").value(DEFAULT_NOM_FORM))
            .andExpect(jsonPath("$.prenomForm").value(DEFAULT_PRENOM_FORM))
            .andExpect(jsonPath("$.idPaysForm").value(DEFAULT_ID_PAYS_FORM))
            .andExpect(jsonPath("$.descForm3").value(DEFAULT_DESC_FORM_3))
            .andExpect(jsonPath("$.descForm4").value(DEFAULT_DESC_FORM_4))
            .andExpect(jsonPath("$.autreetatcivil").value(DEFAULT_AUTREETATCIVIL))
            .andExpect(jsonPath("$.autredoc").value(DEFAULT_AUTREDOC))
            .andExpect(jsonPath("$.autreobj").value(DEFAULT_AUTREOBJ))
            .andExpect(jsonPath("$.autrefris").value(DEFAULT_AUTREFRIS))
            .andExpect(jsonPath("$.descpromot").value(DEFAULT_DESCPROMOT))
            .andExpect(jsonPath("$.descinvit").value(DEFAULT_DESCINVIT))
            .andExpect(jsonPath("$.isVisa").value(DEFAULT_IS_VISA.booleanValue()))
            .andExpect(jsonPath("$.dateaut").value(DEFAULT_DATEAUT.toString()))
            .andExpect(jsonPath("$.datecreatefo").value(DEFAULT_DATECREATEFO.toString()))
            .andExpect(jsonPath("$.telForm").value(DEFAULT_TEL_FORM))
            .andExpect(jsonPath("$.professionForm").value(DEFAULT_PROFESSION_FORM))
            .andExpect(jsonPath("$.referenceForm").value(DEFAULT_REFERENCE_FORM))
            .andExpect(jsonPath("$.otherNation").value(DEFAULT_OTHER_NATION))
            .andExpect(jsonPath("$.telAutori").value(DEFAULT_TEL_AUTORI))
            .andExpect(jsonPath("$.emailAutori").value(DEFAULT_EMAIL_AUTORI))
            .andExpect(jsonPath("$.numSejour").value(DEFAULT_NUM_SEJOUR))
            .andExpect(jsonPath("$.nomEmp").value(DEFAULT_NOM_EMP))
            .andExpect(jsonPath("$.numEmp").value(DEFAULT_NUM_EMP))
            .andExpect(jsonPath("$.villeDestination").value(DEFAULT_VILLE_DESTINATION))
            .andExpect(jsonPath("$.infoObjeVoyage").value(DEFAULT_INFO_OBJE_VOYAGE))
            .andExpect(jsonPath("$.autoDelivrePar").value(DEFAULT_AUTO_DELIVRE_PAR))
            .andExpect(jsonPath("$.otherMoyen").value(DEFAULT_OTHER_MOYEN))
            .andExpect(jsonPath("$.otherLien").value(DEFAULT_OTHER_LIEN))
            .andExpect(jsonPath("$.faxInvite").value(DEFAULT_FAX_INVITE))
            .andExpect(jsonPath("$.faxEntreprise").value(DEFAULT_FAX_ENTREPRISE))
            .andExpect(jsonPath("$.natActuel").value(DEFAULT_NAT_ACTUEL))
            .andExpect(jsonPath("$.natDiffer").value(DEFAULT_NAT_DIFFER))
            .andExpect(jsonPath("$.natMineur").value(DEFAULT_NAT_MINEUR))
            .andExpect(jsonPath("$.natCitoyen").value(DEFAULT_NAT_CITOYEN))
            .andExpect(jsonPath("$.moysub1").value(DEFAULT_MOYSUB_1.booleanValue()))
            .andExpect(jsonPath("$.moysub2").value(DEFAULT_MOYSUB_2.booleanValue()))
            .andExpect(jsonPath("$.moysub3").value(DEFAULT_MOYSUB_3.booleanValue()))
            .andExpect(jsonPath("$.moysub4").value(DEFAULT_MOYSUB_4.booleanValue()))
            .andExpect(jsonPath("$.moysub5").value(DEFAULT_MOYSUB_5.booleanValue()))
            .andExpect(jsonPath("$.moysub8").value(DEFAULT_MOYSUB_8.booleanValue()))
            .andExpect(jsonPath("$.moysubs1").value(DEFAULT_MOYSUBS_1.booleanValue()))
            .andExpect(jsonPath("$.moysubs5").value(DEFAULT_MOYSUBS_5.booleanValue()))
            .andExpect(jsonPath("$.moysubs6").value(DEFAULT_MOYSUBS_6.booleanValue()))
            .andExpect(jsonPath("$.moysubs7").value(DEFAULT_MOYSUBS_7.booleanValue()))
            .andExpect(jsonPath("$.moysubs8").value(DEFAULT_MOYSUBS_8.booleanValue()))
            .andExpect(jsonPath("$.autreFrais").value(DEFAULT_AUTRE_FRAIS))
            .andExpect(jsonPath("$.etatCivil").value(DEFAULT_ETAT_CIVIL))
            .andExpect(jsonPath("$.idLienF").value(DEFAULT_ID_LIEN_F))
            .andExpect(jsonPath("$.objPerson").value(DEFAULT_OBJ_PERSON))
            .andExpect(jsonPath("$.idSexeAvs").value(DEFAULT_ID_SEXE_AVS))
            .andExpect(jsonPath("$.typeDocV").value(DEFAULT_TYPE_DOC_V))
            .andExpect(jsonPath("$.typeFinance").value(DEFAULT_TYPE_FINANCE))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        Person updatedPerson = personRepository.findById(person.getId()).get();
        // Disconnect from session so that the updates on updatedPerson are not directly saved in db
        em.detach(updatedPerson);
        updatedPerson
            .idMoyenSub(UPDATED_ID_MOYEN_SUB)
            .nomPrec(UPDATED_NOM_PREC)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .etatCivilWeb(UPDATED_ETAT_CIVIL_WEB)
            .nationPrec(UPDATED_NATION_PREC)
            .nationActuel(UPDATED_NATION_ACTUEL)
            .nomAutor(UPDATED_NOM_AUTOR)
            .prenomAutor(UPDATED_PRENOM_AUTOR)
            .adresAutor(UPDATED_ADRES_AUTOR)
            .natiAutor(UPDATED_NATI_AUTOR)
            .numCin(UPDATED_NUM_CIN)
            .numDoc(UPDATED_NUM_DOC)
            .dateDelivDoc(UPDATED_DATE_DELIV_DOC)
            .dateExpDoc(UPDATED_DATE_EXP_DOC)
            .delivParDoc(UPDATED_DELIV_PAR_DOC)
            .adresseDomicile(UPDATED_ADRESSE_DOMICILE)
            .adresseEmail(UPDATED_ADRESSE_EMAIL)
            .isResident(UPDATED_IS_RESIDENT)
            .titreSejour(UPDATED_TITRE_SEJOUR)
            .dateExpSejour(UPDATED_DATE_EXP_SEJOUR)
            .numTel(UPDATED_NUM_TEL)
            .adressEmp(UPDATED_ADRESS_EMP)
            .telEmp(UPDATED_TEL_EMP)
            .nomEtab(UPDATED_NOM_ETAB)
            .adressEtablis(UPDATED_ADRESS_ETABLIS)
            .dureeSejour(UPDATED_DUREE_SEJOUR)
            .etatMemDes(UPDATED_ETAT_MEM_DES)
            .etatMemPremier(UPDATED_ETAT_MEM_PREMIER)
            .nombreEntre(UPDATED_NOMBRE_ENTRE)
            .oldVisaExiste(UPDATED_OLD_VISA_EXISTE)
            .dateDelivDebut(UPDATED_DATE_DELIV_DEBUT)
            .dateDelivFin(UPDATED_DATE_DELIV_FIN)
            .isEmprDegit(UPDATED_IS_EMPR_DEGIT)
            .dateEmpreint(UPDATED_DATE_EMPREINT)
            .dateDelivAutor(UPDATED_DATE_DELIV_AUTOR)
            .dateValideAtorDebut(UPDATED_DATE_VALIDE_ATOR_DEBUT)
            .dateValideAutorFin(UPDATED_DATE_VALIDE_AUTOR_FIN)
            .dateArrivPrevu(UPDATED_DATE_ARRIV_PREVU)
            .dateDepartPrevu(UPDATED_DATE_DEPART_PREVU)
            .descInvite(UPDATED_DESC_INVITE)
            .adresseInvit(UPDATED_ADRESSE_INVIT)
            .telInvite(UPDATED_TEL_INVITE)
            .emailInvite(UPDATED_EMAIL_INVITE)
            .nomEntreprise(UPDATED_NOM_ENTREPRISE)
            .adresseEntreprise(UPDATED_ADRESSE_ENTREPRISE)
            .contactEntreprise(UPDATED_CONTACT_ENTREPRISE)
            .descContactEntreprise(UPDATED_DESC_CONTACT_ENTREPRISE)
            .financeFraisVoyage(UPDATED_FINANCE_FRAIS_VOYAGE)
            .nomCit(UPDATED_NOM_CIT)
            .prenomCit(UPDATED_PRENOM_CIT)
            .dateNaissCit(UPDATED_DATE_NAISS_CIT)
            .nationCit(UPDATED_NATION_CIT)
            .numDocCit(UPDATED_NUM_DOC_CIT)
            .lieuForm(UPDATED_LIEU_FORM)
            .dateForm(UPDATED_DATE_FORM)
            .descPhotoForm(UPDATED_DESC_PHOTO_FORM)
            .descForm1(UPDATED_DESC_FORM_1)
            .descForm2(UPDATED_DESC_FORM_2)
            .emailComp(UPDATED_EMAIL_COMP)
            .nomForm(UPDATED_NOM_FORM)
            .prenomForm(UPDATED_PRENOM_FORM)
            .idPaysForm(UPDATED_ID_PAYS_FORM)
            .descForm3(UPDATED_DESC_FORM_3)
            .descForm4(UPDATED_DESC_FORM_4)
            .autreetatcivil(UPDATED_AUTREETATCIVIL)
            .autredoc(UPDATED_AUTREDOC)
            .autreobj(UPDATED_AUTREOBJ)
            .autrefris(UPDATED_AUTREFRIS)
            .descpromot(UPDATED_DESCPROMOT)
            .descinvit(UPDATED_DESCINVIT)
            .isVisa(UPDATED_IS_VISA)
            .dateaut(UPDATED_DATEAUT)
            .datecreatefo(UPDATED_DATECREATEFO)
            .telForm(UPDATED_TEL_FORM)
            .professionForm(UPDATED_PROFESSION_FORM)
            .referenceForm(UPDATED_REFERENCE_FORM)
            .otherNation(UPDATED_OTHER_NATION)
            .telAutori(UPDATED_TEL_AUTORI)
            .emailAutori(UPDATED_EMAIL_AUTORI)
            .numSejour(UPDATED_NUM_SEJOUR)
            .nomEmp(UPDATED_NOM_EMP)
            .numEmp(UPDATED_NUM_EMP)
            .villeDestination(UPDATED_VILLE_DESTINATION)
            .infoObjeVoyage(UPDATED_INFO_OBJE_VOYAGE)
            .autoDelivrePar(UPDATED_AUTO_DELIVRE_PAR)
            .otherMoyen(UPDATED_OTHER_MOYEN)
            .otherLien(UPDATED_OTHER_LIEN)
            .faxInvite(UPDATED_FAX_INVITE)
            .faxEntreprise(UPDATED_FAX_ENTREPRISE)
            .natActuel(UPDATED_NAT_ACTUEL)
            .natDiffer(UPDATED_NAT_DIFFER)
            .natMineur(UPDATED_NAT_MINEUR)
            .natCitoyen(UPDATED_NAT_CITOYEN)
            .moysub1(UPDATED_MOYSUB_1)
            .moysub2(UPDATED_MOYSUB_2)
            .moysub3(UPDATED_MOYSUB_3)
            .moysub4(UPDATED_MOYSUB_4)
            .moysub5(UPDATED_MOYSUB_5)
            .moysub8(UPDATED_MOYSUB_8)
            .moysubs1(UPDATED_MOYSUBS_1)
            .moysubs5(UPDATED_MOYSUBS_5)
            .moysubs6(UPDATED_MOYSUBS_6)
            .moysubs7(UPDATED_MOYSUBS_7)
            .moysubs8(UPDATED_MOYSUBS_8)
            .autreFrais(UPDATED_AUTRE_FRAIS)
            .etatCivil(UPDATED_ETAT_CIVIL)
            .idLienF(UPDATED_ID_LIEN_F)
            .objPerson(UPDATED_OBJ_PERSON)
            .idSexeAvs(UPDATED_ID_SEXE_AVS)
            .typeDocV(UPDATED_TYPE_DOC_V)
            .typeFinance(UPDATED_TYPE_FINANCE)
            .deleted(UPDATED_DELETED);

        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPerson.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPerson))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getIdMoyenSub()).isEqualTo(UPDATED_ID_MOYEN_SUB);
        assertThat(testPerson.getNomPrec()).isEqualTo(UPDATED_NOM_PREC);
        assertThat(testPerson.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testPerson.getEtatCivilWeb()).isEqualTo(UPDATED_ETAT_CIVIL_WEB);
        assertThat(testPerson.getNationPrec()).isEqualTo(UPDATED_NATION_PREC);
        assertThat(testPerson.getNationActuel()).isEqualTo(UPDATED_NATION_ACTUEL);
        assertThat(testPerson.getNomAutor()).isEqualTo(UPDATED_NOM_AUTOR);
        assertThat(testPerson.getPrenomAutor()).isEqualTo(UPDATED_PRENOM_AUTOR);
        assertThat(testPerson.getAdresAutor()).isEqualTo(UPDATED_ADRES_AUTOR);
        assertThat(testPerson.getNatiAutor()).isEqualTo(UPDATED_NATI_AUTOR);
        assertThat(testPerson.getNumCin()).isEqualTo(UPDATED_NUM_CIN);
        assertThat(testPerson.getNumDoc()).isEqualTo(UPDATED_NUM_DOC);
        assertThat(testPerson.getDateDelivDoc()).isEqualTo(UPDATED_DATE_DELIV_DOC);
        assertThat(testPerson.getDateExpDoc()).isEqualTo(UPDATED_DATE_EXP_DOC);
        assertThat(testPerson.getDelivParDoc()).isEqualTo(UPDATED_DELIV_PAR_DOC);
        assertThat(testPerson.getAdresseDomicile()).isEqualTo(UPDATED_ADRESSE_DOMICILE);
        assertThat(testPerson.getAdresseEmail()).isEqualTo(UPDATED_ADRESSE_EMAIL);
        assertThat(testPerson.getIsResident()).isEqualTo(UPDATED_IS_RESIDENT);
        assertThat(testPerson.getTitreSejour()).isEqualTo(UPDATED_TITRE_SEJOUR);
        assertThat(testPerson.getDateExpSejour()).isEqualTo(UPDATED_DATE_EXP_SEJOUR);
        assertThat(testPerson.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testPerson.getAdressEmp()).isEqualTo(UPDATED_ADRESS_EMP);
        assertThat(testPerson.getTelEmp()).isEqualTo(UPDATED_TEL_EMP);
        assertThat(testPerson.getNomEtab()).isEqualTo(UPDATED_NOM_ETAB);
        assertThat(testPerson.getAdressEtablis()).isEqualTo(UPDATED_ADRESS_ETABLIS);
        assertThat(testPerson.getDureeSejour()).isEqualTo(UPDATED_DUREE_SEJOUR);
        assertThat(testPerson.getEtatMemDes()).isEqualTo(UPDATED_ETAT_MEM_DES);
        assertThat(testPerson.getEtatMemPremier()).isEqualTo(UPDATED_ETAT_MEM_PREMIER);
        assertThat(testPerson.getNombreEntre()).isEqualTo(UPDATED_NOMBRE_ENTRE);
        assertThat(testPerson.getOldVisaExiste()).isEqualTo(UPDATED_OLD_VISA_EXISTE);
        assertThat(testPerson.getDateDelivDebut()).isEqualTo(UPDATED_DATE_DELIV_DEBUT);
        assertThat(testPerson.getDateDelivFin()).isEqualTo(UPDATED_DATE_DELIV_FIN);
        assertThat(testPerson.getIsEmprDegit()).isEqualTo(UPDATED_IS_EMPR_DEGIT);
        assertThat(testPerson.getDateEmpreint()).isEqualTo(UPDATED_DATE_EMPREINT);
        assertThat(testPerson.getDateDelivAutor()).isEqualTo(UPDATED_DATE_DELIV_AUTOR);
        assertThat(testPerson.getDateValideAtorDebut()).isEqualTo(UPDATED_DATE_VALIDE_ATOR_DEBUT);
        assertThat(testPerson.getDateValideAutorFin()).isEqualTo(UPDATED_DATE_VALIDE_AUTOR_FIN);
        assertThat(testPerson.getDateArrivPrevu()).isEqualTo(UPDATED_DATE_ARRIV_PREVU);
        assertThat(testPerson.getDateDepartPrevu()).isEqualTo(UPDATED_DATE_DEPART_PREVU);
        assertThat(testPerson.getDescInvite()).isEqualTo(UPDATED_DESC_INVITE);
        assertThat(testPerson.getAdresseInvit()).isEqualTo(UPDATED_ADRESSE_INVIT);
        assertThat(testPerson.getTelInvite()).isEqualTo(UPDATED_TEL_INVITE);
        assertThat(testPerson.getEmailInvite()).isEqualTo(UPDATED_EMAIL_INVITE);
        assertThat(testPerson.getNomEntreprise()).isEqualTo(UPDATED_NOM_ENTREPRISE);
        assertThat(testPerson.getAdresseEntreprise()).isEqualTo(UPDATED_ADRESSE_ENTREPRISE);
        assertThat(testPerson.getContactEntreprise()).isEqualTo(UPDATED_CONTACT_ENTREPRISE);
        assertThat(testPerson.getDescContactEntreprise()).isEqualTo(UPDATED_DESC_CONTACT_ENTREPRISE);
        assertThat(testPerson.getFinanceFraisVoyage()).isEqualTo(UPDATED_FINANCE_FRAIS_VOYAGE);
        assertThat(testPerson.getNomCit()).isEqualTo(UPDATED_NOM_CIT);
        assertThat(testPerson.getPrenomCit()).isEqualTo(UPDATED_PRENOM_CIT);
        assertThat(testPerson.getDateNaissCit()).isEqualTo(UPDATED_DATE_NAISS_CIT);
        assertThat(testPerson.getNationCit()).isEqualTo(UPDATED_NATION_CIT);
        assertThat(testPerson.getNumDocCit()).isEqualTo(UPDATED_NUM_DOC_CIT);
        assertThat(testPerson.getLieuForm()).isEqualTo(UPDATED_LIEU_FORM);
        assertThat(testPerson.getDateForm()).isEqualTo(UPDATED_DATE_FORM);
        assertThat(testPerson.getDescPhotoForm()).isEqualTo(UPDATED_DESC_PHOTO_FORM);
        assertThat(testPerson.getDescForm1()).isEqualTo(UPDATED_DESC_FORM_1);
        assertThat(testPerson.getDescForm2()).isEqualTo(UPDATED_DESC_FORM_2);
        assertThat(testPerson.getEmailComp()).isEqualTo(UPDATED_EMAIL_COMP);
        assertThat(testPerson.getNomForm()).isEqualTo(UPDATED_NOM_FORM);
        assertThat(testPerson.getPrenomForm()).isEqualTo(UPDATED_PRENOM_FORM);
        assertThat(testPerson.getIdPaysForm()).isEqualTo(UPDATED_ID_PAYS_FORM);
        assertThat(testPerson.getDescForm3()).isEqualTo(UPDATED_DESC_FORM_3);
        assertThat(testPerson.getDescForm4()).isEqualTo(UPDATED_DESC_FORM_4);
        assertThat(testPerson.getAutreetatcivil()).isEqualTo(UPDATED_AUTREETATCIVIL);
        assertThat(testPerson.getAutredoc()).isEqualTo(UPDATED_AUTREDOC);
        assertThat(testPerson.getAutreobj()).isEqualTo(UPDATED_AUTREOBJ);
        assertThat(testPerson.getAutrefris()).isEqualTo(UPDATED_AUTREFRIS);
        assertThat(testPerson.getDescpromot()).isEqualTo(UPDATED_DESCPROMOT);
        assertThat(testPerson.getDescinvit()).isEqualTo(UPDATED_DESCINVIT);
        assertThat(testPerson.getIsVisa()).isEqualTo(UPDATED_IS_VISA);
        assertThat(testPerson.getDateaut()).isEqualTo(UPDATED_DATEAUT);
        assertThat(testPerson.getDatecreatefo()).isEqualTo(UPDATED_DATECREATEFO);
        assertThat(testPerson.getTelForm()).isEqualTo(UPDATED_TEL_FORM);
        assertThat(testPerson.getProfessionForm()).isEqualTo(UPDATED_PROFESSION_FORM);
        assertThat(testPerson.getReferenceForm()).isEqualTo(UPDATED_REFERENCE_FORM);
        assertThat(testPerson.getOtherNation()).isEqualTo(UPDATED_OTHER_NATION);
        assertThat(testPerson.getTelAutori()).isEqualTo(UPDATED_TEL_AUTORI);
        assertThat(testPerson.getEmailAutori()).isEqualTo(UPDATED_EMAIL_AUTORI);
        assertThat(testPerson.getNumSejour()).isEqualTo(UPDATED_NUM_SEJOUR);
        assertThat(testPerson.getNomEmp()).isEqualTo(UPDATED_NOM_EMP);
        assertThat(testPerson.getNumEmp()).isEqualTo(UPDATED_NUM_EMP);
        assertThat(testPerson.getVilleDestination()).isEqualTo(UPDATED_VILLE_DESTINATION);
        assertThat(testPerson.getInfoObjeVoyage()).isEqualTo(UPDATED_INFO_OBJE_VOYAGE);
        assertThat(testPerson.getAutoDelivrePar()).isEqualTo(UPDATED_AUTO_DELIVRE_PAR);
        assertThat(testPerson.getOtherMoyen()).isEqualTo(UPDATED_OTHER_MOYEN);
        assertThat(testPerson.getOtherLien()).isEqualTo(UPDATED_OTHER_LIEN);
        assertThat(testPerson.getFaxInvite()).isEqualTo(UPDATED_FAX_INVITE);
        assertThat(testPerson.getFaxEntreprise()).isEqualTo(UPDATED_FAX_ENTREPRISE);
        assertThat(testPerson.getNatActuel()).isEqualTo(UPDATED_NAT_ACTUEL);
        assertThat(testPerson.getNatDiffer()).isEqualTo(UPDATED_NAT_DIFFER);
        assertThat(testPerson.getNatMineur()).isEqualTo(UPDATED_NAT_MINEUR);
        assertThat(testPerson.getNatCitoyen()).isEqualTo(UPDATED_NAT_CITOYEN);
        assertThat(testPerson.getMoysub1()).isEqualTo(UPDATED_MOYSUB_1);
        assertThat(testPerson.getMoysub2()).isEqualTo(UPDATED_MOYSUB_2);
        assertThat(testPerson.getMoysub3()).isEqualTo(UPDATED_MOYSUB_3);
        assertThat(testPerson.getMoysub4()).isEqualTo(UPDATED_MOYSUB_4);
        assertThat(testPerson.getMoysub5()).isEqualTo(UPDATED_MOYSUB_5);
        assertThat(testPerson.getMoysub8()).isEqualTo(UPDATED_MOYSUB_8);
        assertThat(testPerson.getMoysubs1()).isEqualTo(UPDATED_MOYSUBS_1);
        assertThat(testPerson.getMoysubs5()).isEqualTo(UPDATED_MOYSUBS_5);
        assertThat(testPerson.getMoysubs6()).isEqualTo(UPDATED_MOYSUBS_6);
        assertThat(testPerson.getMoysubs7()).isEqualTo(UPDATED_MOYSUBS_7);
        assertThat(testPerson.getMoysubs8()).isEqualTo(UPDATED_MOYSUBS_8);
        assertThat(testPerson.getAutreFrais()).isEqualTo(UPDATED_AUTRE_FRAIS);
        assertThat(testPerson.getEtatCivil()).isEqualTo(UPDATED_ETAT_CIVIL);
        assertThat(testPerson.getIdLienF()).isEqualTo(UPDATED_ID_LIEN_F);
        assertThat(testPerson.getObjPerson()).isEqualTo(UPDATED_OBJ_PERSON);
        assertThat(testPerson.getIdSexeAvs()).isEqualTo(UPDATED_ID_SEXE_AVS);
        assertThat(testPerson.getTypeDocV()).isEqualTo(UPDATED_TYPE_DOC_V);
        assertThat(testPerson.getTypeFinance()).isEqualTo(UPDATED_TYPE_FINANCE);
        assertThat(testPerson.getDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the Person in Elasticsearch
        verify(mockPersonSearchRepository).save(testPerson);
    }

    @Test
    @Transactional
    void putNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, person.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(person))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Person in Elasticsearch
        verify(mockPersonSearchRepository, times(0)).save(person);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(person))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Person in Elasticsearch
        verify(mockPersonSearchRepository, times(0)).save(person);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Person in Elasticsearch
        verify(mockPersonSearchRepository, times(0)).save(person);
    }

    @Test
    @Transactional
    void partialUpdatePersonWithPatch() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person using partial update
        Person partialUpdatedPerson = new Person();
        partialUpdatedPerson.setId(person.getId());

        partialUpdatedPerson
            .nomPrec(UPDATED_NOM_PREC)
            .etatCivilWeb(UPDATED_ETAT_CIVIL_WEB)
            .adresAutor(UPDATED_ADRES_AUTOR)
            .natiAutor(UPDATED_NATI_AUTOR)
            .numCin(UPDATED_NUM_CIN)
            .dateExpDoc(UPDATED_DATE_EXP_DOC)
            .adresseDomicile(UPDATED_ADRESSE_DOMICILE)
            .numTel(UPDATED_NUM_TEL)
            .etatMemDes(UPDATED_ETAT_MEM_DES)
            .isEmprDegit(UPDATED_IS_EMPR_DEGIT)
            .dateEmpreint(UPDATED_DATE_EMPREINT)
            .dateValideAutorFin(UPDATED_DATE_VALIDE_AUTOR_FIN)
            .dateArrivPrevu(UPDATED_DATE_ARRIV_PREVU)
            .dateDepartPrevu(UPDATED_DATE_DEPART_PREVU)
            .adresseInvit(UPDATED_ADRESSE_INVIT)
            .nomEntreprise(UPDATED_NOM_ENTREPRISE)
            .contactEntreprise(UPDATED_CONTACT_ENTREPRISE)
            .descContactEntreprise(UPDATED_DESC_CONTACT_ENTREPRISE)
            .financeFraisVoyage(UPDATED_FINANCE_FRAIS_VOYAGE)
            .nomCit(UPDATED_NOM_CIT)
            .prenomCit(UPDATED_PRENOM_CIT)
            .dateNaissCit(UPDATED_DATE_NAISS_CIT)
            .nationCit(UPDATED_NATION_CIT)
            .lieuForm(UPDATED_LIEU_FORM)
            .descPhotoForm(UPDATED_DESC_PHOTO_FORM)
            .descForm2(UPDATED_DESC_FORM_2)
            .nomForm(UPDATED_NOM_FORM)
            .descForm3(UPDATED_DESC_FORM_3)
            .descForm4(UPDATED_DESC_FORM_4)
            .autreetatcivil(UPDATED_AUTREETATCIVIL)
            .autredoc(UPDATED_AUTREDOC)
            .descpromot(UPDATED_DESCPROMOT)
            .descinvit(UPDATED_DESCINVIT)
            .dateaut(UPDATED_DATEAUT)
            .professionForm(UPDATED_PROFESSION_FORM)
            .numSejour(UPDATED_NUM_SEJOUR)
            .nomEmp(UPDATED_NOM_EMP)
            .numEmp(UPDATED_NUM_EMP)
            .infoObjeVoyage(UPDATED_INFO_OBJE_VOYAGE)
            .autoDelivrePar(UPDATED_AUTO_DELIVRE_PAR)
            .otherMoyen(UPDATED_OTHER_MOYEN)
            .otherLien(UPDATED_OTHER_LIEN)
            .faxEntreprise(UPDATED_FAX_ENTREPRISE)
            .natActuel(UPDATED_NAT_ACTUEL)
            .natMineur(UPDATED_NAT_MINEUR)
            .moysub2(UPDATED_MOYSUB_2)
            .moysub3(UPDATED_MOYSUB_3)
            .moysub5(UPDATED_MOYSUB_5)
            .moysubs6(UPDATED_MOYSUBS_6)
            .autreFrais(UPDATED_AUTRE_FRAIS)
            .etatCivil(UPDATED_ETAT_CIVIL)
            .idSexeAvs(UPDATED_ID_SEXE_AVS)
            .typeDocV(UPDATED_TYPE_DOC_V)
            .typeFinance(UPDATED_TYPE_FINANCE)
            .deleted(UPDATED_DELETED);

        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerson))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getIdMoyenSub()).isEqualTo(DEFAULT_ID_MOYEN_SUB);
        assertThat(testPerson.getNomPrec()).isEqualTo(UPDATED_NOM_PREC);
        assertThat(testPerson.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
        assertThat(testPerson.getEtatCivilWeb()).isEqualTo(UPDATED_ETAT_CIVIL_WEB);
        assertThat(testPerson.getNationPrec()).isEqualTo(DEFAULT_NATION_PREC);
        assertThat(testPerson.getNationActuel()).isEqualTo(DEFAULT_NATION_ACTUEL);
        assertThat(testPerson.getNomAutor()).isEqualTo(DEFAULT_NOM_AUTOR);
        assertThat(testPerson.getPrenomAutor()).isEqualTo(DEFAULT_PRENOM_AUTOR);
        assertThat(testPerson.getAdresAutor()).isEqualTo(UPDATED_ADRES_AUTOR);
        assertThat(testPerson.getNatiAutor()).isEqualTo(UPDATED_NATI_AUTOR);
        assertThat(testPerson.getNumCin()).isEqualTo(UPDATED_NUM_CIN);
        assertThat(testPerson.getNumDoc()).isEqualTo(DEFAULT_NUM_DOC);
        assertThat(testPerson.getDateDelivDoc()).isEqualTo(DEFAULT_DATE_DELIV_DOC);
        assertThat(testPerson.getDateExpDoc()).isEqualTo(UPDATED_DATE_EXP_DOC);
        assertThat(testPerson.getDelivParDoc()).isEqualTo(DEFAULT_DELIV_PAR_DOC);
        assertThat(testPerson.getAdresseDomicile()).isEqualTo(UPDATED_ADRESSE_DOMICILE);
        assertThat(testPerson.getAdresseEmail()).isEqualTo(DEFAULT_ADRESSE_EMAIL);
        assertThat(testPerson.getIsResident()).isEqualTo(DEFAULT_IS_RESIDENT);
        assertThat(testPerson.getTitreSejour()).isEqualTo(DEFAULT_TITRE_SEJOUR);
        assertThat(testPerson.getDateExpSejour()).isEqualTo(DEFAULT_DATE_EXP_SEJOUR);
        assertThat(testPerson.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testPerson.getAdressEmp()).isEqualTo(DEFAULT_ADRESS_EMP);
        assertThat(testPerson.getTelEmp()).isEqualTo(DEFAULT_TEL_EMP);
        assertThat(testPerson.getNomEtab()).isEqualTo(DEFAULT_NOM_ETAB);
        assertThat(testPerson.getAdressEtablis()).isEqualTo(DEFAULT_ADRESS_ETABLIS);
        assertThat(testPerson.getDureeSejour()).isEqualTo(DEFAULT_DUREE_SEJOUR);
        assertThat(testPerson.getEtatMemDes()).isEqualTo(UPDATED_ETAT_MEM_DES);
        assertThat(testPerson.getEtatMemPremier()).isEqualTo(DEFAULT_ETAT_MEM_PREMIER);
        assertThat(testPerson.getNombreEntre()).isEqualTo(DEFAULT_NOMBRE_ENTRE);
        assertThat(testPerson.getOldVisaExiste()).isEqualTo(DEFAULT_OLD_VISA_EXISTE);
        assertThat(testPerson.getDateDelivDebut()).isEqualTo(DEFAULT_DATE_DELIV_DEBUT);
        assertThat(testPerson.getDateDelivFin()).isEqualTo(DEFAULT_DATE_DELIV_FIN);
        assertThat(testPerson.getIsEmprDegit()).isEqualTo(UPDATED_IS_EMPR_DEGIT);
        assertThat(testPerson.getDateEmpreint()).isEqualTo(UPDATED_DATE_EMPREINT);
        assertThat(testPerson.getDateDelivAutor()).isEqualTo(DEFAULT_DATE_DELIV_AUTOR);
        assertThat(testPerson.getDateValideAtorDebut()).isEqualTo(DEFAULT_DATE_VALIDE_ATOR_DEBUT);
        assertThat(testPerson.getDateValideAutorFin()).isEqualTo(UPDATED_DATE_VALIDE_AUTOR_FIN);
        assertThat(testPerson.getDateArrivPrevu()).isEqualTo(UPDATED_DATE_ARRIV_PREVU);
        assertThat(testPerson.getDateDepartPrevu()).isEqualTo(UPDATED_DATE_DEPART_PREVU);
        assertThat(testPerson.getDescInvite()).isEqualTo(DEFAULT_DESC_INVITE);
        assertThat(testPerson.getAdresseInvit()).isEqualTo(UPDATED_ADRESSE_INVIT);
        assertThat(testPerson.getTelInvite()).isEqualTo(DEFAULT_TEL_INVITE);
        assertThat(testPerson.getEmailInvite()).isEqualTo(DEFAULT_EMAIL_INVITE);
        assertThat(testPerson.getNomEntreprise()).isEqualTo(UPDATED_NOM_ENTREPRISE);
        assertThat(testPerson.getAdresseEntreprise()).isEqualTo(DEFAULT_ADRESSE_ENTREPRISE);
        assertThat(testPerson.getContactEntreprise()).isEqualTo(UPDATED_CONTACT_ENTREPRISE);
        assertThat(testPerson.getDescContactEntreprise()).isEqualTo(UPDATED_DESC_CONTACT_ENTREPRISE);
        assertThat(testPerson.getFinanceFraisVoyage()).isEqualTo(UPDATED_FINANCE_FRAIS_VOYAGE);
        assertThat(testPerson.getNomCit()).isEqualTo(UPDATED_NOM_CIT);
        assertThat(testPerson.getPrenomCit()).isEqualTo(UPDATED_PRENOM_CIT);
        assertThat(testPerson.getDateNaissCit()).isEqualTo(UPDATED_DATE_NAISS_CIT);
        assertThat(testPerson.getNationCit()).isEqualTo(UPDATED_NATION_CIT);
        assertThat(testPerson.getNumDocCit()).isEqualTo(DEFAULT_NUM_DOC_CIT);
        assertThat(testPerson.getLieuForm()).isEqualTo(UPDATED_LIEU_FORM);
        assertThat(testPerson.getDateForm()).isEqualTo(DEFAULT_DATE_FORM);
        assertThat(testPerson.getDescPhotoForm()).isEqualTo(UPDATED_DESC_PHOTO_FORM);
        assertThat(testPerson.getDescForm1()).isEqualTo(DEFAULT_DESC_FORM_1);
        assertThat(testPerson.getDescForm2()).isEqualTo(UPDATED_DESC_FORM_2);
        assertThat(testPerson.getEmailComp()).isEqualTo(DEFAULT_EMAIL_COMP);
        assertThat(testPerson.getNomForm()).isEqualTo(UPDATED_NOM_FORM);
        assertThat(testPerson.getPrenomForm()).isEqualTo(DEFAULT_PRENOM_FORM);
        assertThat(testPerson.getIdPaysForm()).isEqualTo(DEFAULT_ID_PAYS_FORM);
        assertThat(testPerson.getDescForm3()).isEqualTo(UPDATED_DESC_FORM_3);
        assertThat(testPerson.getDescForm4()).isEqualTo(UPDATED_DESC_FORM_4);
        assertThat(testPerson.getAutreetatcivil()).isEqualTo(UPDATED_AUTREETATCIVIL);
        assertThat(testPerson.getAutredoc()).isEqualTo(UPDATED_AUTREDOC);
        assertThat(testPerson.getAutreobj()).isEqualTo(DEFAULT_AUTREOBJ);
        assertThat(testPerson.getAutrefris()).isEqualTo(DEFAULT_AUTREFRIS);
        assertThat(testPerson.getDescpromot()).isEqualTo(UPDATED_DESCPROMOT);
        assertThat(testPerson.getDescinvit()).isEqualTo(UPDATED_DESCINVIT);
        assertThat(testPerson.getIsVisa()).isEqualTo(DEFAULT_IS_VISA);
        assertThat(testPerson.getDateaut()).isEqualTo(UPDATED_DATEAUT);
        assertThat(testPerson.getDatecreatefo()).isEqualTo(DEFAULT_DATECREATEFO);
        assertThat(testPerson.getTelForm()).isEqualTo(DEFAULT_TEL_FORM);
        assertThat(testPerson.getProfessionForm()).isEqualTo(UPDATED_PROFESSION_FORM);
        assertThat(testPerson.getReferenceForm()).isEqualTo(DEFAULT_REFERENCE_FORM);
        assertThat(testPerson.getOtherNation()).isEqualTo(DEFAULT_OTHER_NATION);
        assertThat(testPerson.getTelAutori()).isEqualTo(DEFAULT_TEL_AUTORI);
        assertThat(testPerson.getEmailAutori()).isEqualTo(DEFAULT_EMAIL_AUTORI);
        assertThat(testPerson.getNumSejour()).isEqualTo(UPDATED_NUM_SEJOUR);
        assertThat(testPerson.getNomEmp()).isEqualTo(UPDATED_NOM_EMP);
        assertThat(testPerson.getNumEmp()).isEqualTo(UPDATED_NUM_EMP);
        assertThat(testPerson.getVilleDestination()).isEqualTo(DEFAULT_VILLE_DESTINATION);
        assertThat(testPerson.getInfoObjeVoyage()).isEqualTo(UPDATED_INFO_OBJE_VOYAGE);
        assertThat(testPerson.getAutoDelivrePar()).isEqualTo(UPDATED_AUTO_DELIVRE_PAR);
        assertThat(testPerson.getOtherMoyen()).isEqualTo(UPDATED_OTHER_MOYEN);
        assertThat(testPerson.getOtherLien()).isEqualTo(UPDATED_OTHER_LIEN);
        assertThat(testPerson.getFaxInvite()).isEqualTo(DEFAULT_FAX_INVITE);
        assertThat(testPerson.getFaxEntreprise()).isEqualTo(UPDATED_FAX_ENTREPRISE);
        assertThat(testPerson.getNatActuel()).isEqualTo(UPDATED_NAT_ACTUEL);
        assertThat(testPerson.getNatDiffer()).isEqualTo(DEFAULT_NAT_DIFFER);
        assertThat(testPerson.getNatMineur()).isEqualTo(UPDATED_NAT_MINEUR);
        assertThat(testPerson.getNatCitoyen()).isEqualTo(DEFAULT_NAT_CITOYEN);
        assertThat(testPerson.getMoysub1()).isEqualTo(DEFAULT_MOYSUB_1);
        assertThat(testPerson.getMoysub2()).isEqualTo(UPDATED_MOYSUB_2);
        assertThat(testPerson.getMoysub3()).isEqualTo(UPDATED_MOYSUB_3);
        assertThat(testPerson.getMoysub4()).isEqualTo(DEFAULT_MOYSUB_4);
        assertThat(testPerson.getMoysub5()).isEqualTo(UPDATED_MOYSUB_5);
        assertThat(testPerson.getMoysub8()).isEqualTo(DEFAULT_MOYSUB_8);
        assertThat(testPerson.getMoysubs1()).isEqualTo(DEFAULT_MOYSUBS_1);
        assertThat(testPerson.getMoysubs5()).isEqualTo(DEFAULT_MOYSUBS_5);
        assertThat(testPerson.getMoysubs6()).isEqualTo(UPDATED_MOYSUBS_6);
        assertThat(testPerson.getMoysubs7()).isEqualTo(DEFAULT_MOYSUBS_7);
        assertThat(testPerson.getMoysubs8()).isEqualTo(DEFAULT_MOYSUBS_8);
        assertThat(testPerson.getAutreFrais()).isEqualTo(UPDATED_AUTRE_FRAIS);
        assertThat(testPerson.getEtatCivil()).isEqualTo(UPDATED_ETAT_CIVIL);
        assertThat(testPerson.getIdLienF()).isEqualTo(DEFAULT_ID_LIEN_F);
        assertThat(testPerson.getObjPerson()).isEqualTo(DEFAULT_OBJ_PERSON);
        assertThat(testPerson.getIdSexeAvs()).isEqualTo(UPDATED_ID_SEXE_AVS);
        assertThat(testPerson.getTypeDocV()).isEqualTo(UPDATED_TYPE_DOC_V);
        assertThat(testPerson.getTypeFinance()).isEqualTo(UPDATED_TYPE_FINANCE);
        assertThat(testPerson.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void fullUpdatePersonWithPatch() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person using partial update
        Person partialUpdatedPerson = new Person();
        partialUpdatedPerson.setId(person.getId());

        partialUpdatedPerson
            .idMoyenSub(UPDATED_ID_MOYEN_SUB)
            .nomPrec(UPDATED_NOM_PREC)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .etatCivilWeb(UPDATED_ETAT_CIVIL_WEB)
            .nationPrec(UPDATED_NATION_PREC)
            .nationActuel(UPDATED_NATION_ACTUEL)
            .nomAutor(UPDATED_NOM_AUTOR)
            .prenomAutor(UPDATED_PRENOM_AUTOR)
            .adresAutor(UPDATED_ADRES_AUTOR)
            .natiAutor(UPDATED_NATI_AUTOR)
            .numCin(UPDATED_NUM_CIN)
            .numDoc(UPDATED_NUM_DOC)
            .dateDelivDoc(UPDATED_DATE_DELIV_DOC)
            .dateExpDoc(UPDATED_DATE_EXP_DOC)
            .delivParDoc(UPDATED_DELIV_PAR_DOC)
            .adresseDomicile(UPDATED_ADRESSE_DOMICILE)
            .adresseEmail(UPDATED_ADRESSE_EMAIL)
            .isResident(UPDATED_IS_RESIDENT)
            .titreSejour(UPDATED_TITRE_SEJOUR)
            .dateExpSejour(UPDATED_DATE_EXP_SEJOUR)
            .numTel(UPDATED_NUM_TEL)
            .adressEmp(UPDATED_ADRESS_EMP)
            .telEmp(UPDATED_TEL_EMP)
            .nomEtab(UPDATED_NOM_ETAB)
            .adressEtablis(UPDATED_ADRESS_ETABLIS)
            .dureeSejour(UPDATED_DUREE_SEJOUR)
            .etatMemDes(UPDATED_ETAT_MEM_DES)
            .etatMemPremier(UPDATED_ETAT_MEM_PREMIER)
            .nombreEntre(UPDATED_NOMBRE_ENTRE)
            .oldVisaExiste(UPDATED_OLD_VISA_EXISTE)
            .dateDelivDebut(UPDATED_DATE_DELIV_DEBUT)
            .dateDelivFin(UPDATED_DATE_DELIV_FIN)
            .isEmprDegit(UPDATED_IS_EMPR_DEGIT)
            .dateEmpreint(UPDATED_DATE_EMPREINT)
            .dateDelivAutor(UPDATED_DATE_DELIV_AUTOR)
            .dateValideAtorDebut(UPDATED_DATE_VALIDE_ATOR_DEBUT)
            .dateValideAutorFin(UPDATED_DATE_VALIDE_AUTOR_FIN)
            .dateArrivPrevu(UPDATED_DATE_ARRIV_PREVU)
            .dateDepartPrevu(UPDATED_DATE_DEPART_PREVU)
            .descInvite(UPDATED_DESC_INVITE)
            .adresseInvit(UPDATED_ADRESSE_INVIT)
            .telInvite(UPDATED_TEL_INVITE)
            .emailInvite(UPDATED_EMAIL_INVITE)
            .nomEntreprise(UPDATED_NOM_ENTREPRISE)
            .adresseEntreprise(UPDATED_ADRESSE_ENTREPRISE)
            .contactEntreprise(UPDATED_CONTACT_ENTREPRISE)
            .descContactEntreprise(UPDATED_DESC_CONTACT_ENTREPRISE)
            .financeFraisVoyage(UPDATED_FINANCE_FRAIS_VOYAGE)
            .nomCit(UPDATED_NOM_CIT)
            .prenomCit(UPDATED_PRENOM_CIT)
            .dateNaissCit(UPDATED_DATE_NAISS_CIT)
            .nationCit(UPDATED_NATION_CIT)
            .numDocCit(UPDATED_NUM_DOC_CIT)
            .lieuForm(UPDATED_LIEU_FORM)
            .dateForm(UPDATED_DATE_FORM)
            .descPhotoForm(UPDATED_DESC_PHOTO_FORM)
            .descForm1(UPDATED_DESC_FORM_1)
            .descForm2(UPDATED_DESC_FORM_2)
            .emailComp(UPDATED_EMAIL_COMP)
            .nomForm(UPDATED_NOM_FORM)
            .prenomForm(UPDATED_PRENOM_FORM)
            .idPaysForm(UPDATED_ID_PAYS_FORM)
            .descForm3(UPDATED_DESC_FORM_3)
            .descForm4(UPDATED_DESC_FORM_4)
            .autreetatcivil(UPDATED_AUTREETATCIVIL)
            .autredoc(UPDATED_AUTREDOC)
            .autreobj(UPDATED_AUTREOBJ)
            .autrefris(UPDATED_AUTREFRIS)
            .descpromot(UPDATED_DESCPROMOT)
            .descinvit(UPDATED_DESCINVIT)
            .isVisa(UPDATED_IS_VISA)
            .dateaut(UPDATED_DATEAUT)
            .datecreatefo(UPDATED_DATECREATEFO)
            .telForm(UPDATED_TEL_FORM)
            .professionForm(UPDATED_PROFESSION_FORM)
            .referenceForm(UPDATED_REFERENCE_FORM)
            .otherNation(UPDATED_OTHER_NATION)
            .telAutori(UPDATED_TEL_AUTORI)
            .emailAutori(UPDATED_EMAIL_AUTORI)
            .numSejour(UPDATED_NUM_SEJOUR)
            .nomEmp(UPDATED_NOM_EMP)
            .numEmp(UPDATED_NUM_EMP)
            .villeDestination(UPDATED_VILLE_DESTINATION)
            .infoObjeVoyage(UPDATED_INFO_OBJE_VOYAGE)
            .autoDelivrePar(UPDATED_AUTO_DELIVRE_PAR)
            .otherMoyen(UPDATED_OTHER_MOYEN)
            .otherLien(UPDATED_OTHER_LIEN)
            .faxInvite(UPDATED_FAX_INVITE)
            .faxEntreprise(UPDATED_FAX_ENTREPRISE)
            .natActuel(UPDATED_NAT_ACTUEL)
            .natDiffer(UPDATED_NAT_DIFFER)
            .natMineur(UPDATED_NAT_MINEUR)
            .natCitoyen(UPDATED_NAT_CITOYEN)
            .moysub1(UPDATED_MOYSUB_1)
            .moysub2(UPDATED_MOYSUB_2)
            .moysub3(UPDATED_MOYSUB_3)
            .moysub4(UPDATED_MOYSUB_4)
            .moysub5(UPDATED_MOYSUB_5)
            .moysub8(UPDATED_MOYSUB_8)
            .moysubs1(UPDATED_MOYSUBS_1)
            .moysubs5(UPDATED_MOYSUBS_5)
            .moysubs6(UPDATED_MOYSUBS_6)
            .moysubs7(UPDATED_MOYSUBS_7)
            .moysubs8(UPDATED_MOYSUBS_8)
            .autreFrais(UPDATED_AUTRE_FRAIS)
            .etatCivil(UPDATED_ETAT_CIVIL)
            .idLienF(UPDATED_ID_LIEN_F)
            .objPerson(UPDATED_OBJ_PERSON)
            .idSexeAvs(UPDATED_ID_SEXE_AVS)
            .typeDocV(UPDATED_TYPE_DOC_V)
            .typeFinance(UPDATED_TYPE_FINANCE)
            .deleted(UPDATED_DELETED);

        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerson))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getIdMoyenSub()).isEqualTo(UPDATED_ID_MOYEN_SUB);
        assertThat(testPerson.getNomPrec()).isEqualTo(UPDATED_NOM_PREC);
        assertThat(testPerson.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testPerson.getEtatCivilWeb()).isEqualTo(UPDATED_ETAT_CIVIL_WEB);
        assertThat(testPerson.getNationPrec()).isEqualTo(UPDATED_NATION_PREC);
        assertThat(testPerson.getNationActuel()).isEqualTo(UPDATED_NATION_ACTUEL);
        assertThat(testPerson.getNomAutor()).isEqualTo(UPDATED_NOM_AUTOR);
        assertThat(testPerson.getPrenomAutor()).isEqualTo(UPDATED_PRENOM_AUTOR);
        assertThat(testPerson.getAdresAutor()).isEqualTo(UPDATED_ADRES_AUTOR);
        assertThat(testPerson.getNatiAutor()).isEqualTo(UPDATED_NATI_AUTOR);
        assertThat(testPerson.getNumCin()).isEqualTo(UPDATED_NUM_CIN);
        assertThat(testPerson.getNumDoc()).isEqualTo(UPDATED_NUM_DOC);
        assertThat(testPerson.getDateDelivDoc()).isEqualTo(UPDATED_DATE_DELIV_DOC);
        assertThat(testPerson.getDateExpDoc()).isEqualTo(UPDATED_DATE_EXP_DOC);
        assertThat(testPerson.getDelivParDoc()).isEqualTo(UPDATED_DELIV_PAR_DOC);
        assertThat(testPerson.getAdresseDomicile()).isEqualTo(UPDATED_ADRESSE_DOMICILE);
        assertThat(testPerson.getAdresseEmail()).isEqualTo(UPDATED_ADRESSE_EMAIL);
        assertThat(testPerson.getIsResident()).isEqualTo(UPDATED_IS_RESIDENT);
        assertThat(testPerson.getTitreSejour()).isEqualTo(UPDATED_TITRE_SEJOUR);
        assertThat(testPerson.getDateExpSejour()).isEqualTo(UPDATED_DATE_EXP_SEJOUR);
        assertThat(testPerson.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testPerson.getAdressEmp()).isEqualTo(UPDATED_ADRESS_EMP);
        assertThat(testPerson.getTelEmp()).isEqualTo(UPDATED_TEL_EMP);
        assertThat(testPerson.getNomEtab()).isEqualTo(UPDATED_NOM_ETAB);
        assertThat(testPerson.getAdressEtablis()).isEqualTo(UPDATED_ADRESS_ETABLIS);
        assertThat(testPerson.getDureeSejour()).isEqualTo(UPDATED_DUREE_SEJOUR);
        assertThat(testPerson.getEtatMemDes()).isEqualTo(UPDATED_ETAT_MEM_DES);
        assertThat(testPerson.getEtatMemPremier()).isEqualTo(UPDATED_ETAT_MEM_PREMIER);
        assertThat(testPerson.getNombreEntre()).isEqualTo(UPDATED_NOMBRE_ENTRE);
        assertThat(testPerson.getOldVisaExiste()).isEqualTo(UPDATED_OLD_VISA_EXISTE);
        assertThat(testPerson.getDateDelivDebut()).isEqualTo(UPDATED_DATE_DELIV_DEBUT);
        assertThat(testPerson.getDateDelivFin()).isEqualTo(UPDATED_DATE_DELIV_FIN);
        assertThat(testPerson.getIsEmprDegit()).isEqualTo(UPDATED_IS_EMPR_DEGIT);
        assertThat(testPerson.getDateEmpreint()).isEqualTo(UPDATED_DATE_EMPREINT);
        assertThat(testPerson.getDateDelivAutor()).isEqualTo(UPDATED_DATE_DELIV_AUTOR);
        assertThat(testPerson.getDateValideAtorDebut()).isEqualTo(UPDATED_DATE_VALIDE_ATOR_DEBUT);
        assertThat(testPerson.getDateValideAutorFin()).isEqualTo(UPDATED_DATE_VALIDE_AUTOR_FIN);
        assertThat(testPerson.getDateArrivPrevu()).isEqualTo(UPDATED_DATE_ARRIV_PREVU);
        assertThat(testPerson.getDateDepartPrevu()).isEqualTo(UPDATED_DATE_DEPART_PREVU);
        assertThat(testPerson.getDescInvite()).isEqualTo(UPDATED_DESC_INVITE);
        assertThat(testPerson.getAdresseInvit()).isEqualTo(UPDATED_ADRESSE_INVIT);
        assertThat(testPerson.getTelInvite()).isEqualTo(UPDATED_TEL_INVITE);
        assertThat(testPerson.getEmailInvite()).isEqualTo(UPDATED_EMAIL_INVITE);
        assertThat(testPerson.getNomEntreprise()).isEqualTo(UPDATED_NOM_ENTREPRISE);
        assertThat(testPerson.getAdresseEntreprise()).isEqualTo(UPDATED_ADRESSE_ENTREPRISE);
        assertThat(testPerson.getContactEntreprise()).isEqualTo(UPDATED_CONTACT_ENTREPRISE);
        assertThat(testPerson.getDescContactEntreprise()).isEqualTo(UPDATED_DESC_CONTACT_ENTREPRISE);
        assertThat(testPerson.getFinanceFraisVoyage()).isEqualTo(UPDATED_FINANCE_FRAIS_VOYAGE);
        assertThat(testPerson.getNomCit()).isEqualTo(UPDATED_NOM_CIT);
        assertThat(testPerson.getPrenomCit()).isEqualTo(UPDATED_PRENOM_CIT);
        assertThat(testPerson.getDateNaissCit()).isEqualTo(UPDATED_DATE_NAISS_CIT);
        assertThat(testPerson.getNationCit()).isEqualTo(UPDATED_NATION_CIT);
        assertThat(testPerson.getNumDocCit()).isEqualTo(UPDATED_NUM_DOC_CIT);
        assertThat(testPerson.getLieuForm()).isEqualTo(UPDATED_LIEU_FORM);
        assertThat(testPerson.getDateForm()).isEqualTo(UPDATED_DATE_FORM);
        assertThat(testPerson.getDescPhotoForm()).isEqualTo(UPDATED_DESC_PHOTO_FORM);
        assertThat(testPerson.getDescForm1()).isEqualTo(UPDATED_DESC_FORM_1);
        assertThat(testPerson.getDescForm2()).isEqualTo(UPDATED_DESC_FORM_2);
        assertThat(testPerson.getEmailComp()).isEqualTo(UPDATED_EMAIL_COMP);
        assertThat(testPerson.getNomForm()).isEqualTo(UPDATED_NOM_FORM);
        assertThat(testPerson.getPrenomForm()).isEqualTo(UPDATED_PRENOM_FORM);
        assertThat(testPerson.getIdPaysForm()).isEqualTo(UPDATED_ID_PAYS_FORM);
        assertThat(testPerson.getDescForm3()).isEqualTo(UPDATED_DESC_FORM_3);
        assertThat(testPerson.getDescForm4()).isEqualTo(UPDATED_DESC_FORM_4);
        assertThat(testPerson.getAutreetatcivil()).isEqualTo(UPDATED_AUTREETATCIVIL);
        assertThat(testPerson.getAutredoc()).isEqualTo(UPDATED_AUTREDOC);
        assertThat(testPerson.getAutreobj()).isEqualTo(UPDATED_AUTREOBJ);
        assertThat(testPerson.getAutrefris()).isEqualTo(UPDATED_AUTREFRIS);
        assertThat(testPerson.getDescpromot()).isEqualTo(UPDATED_DESCPROMOT);
        assertThat(testPerson.getDescinvit()).isEqualTo(UPDATED_DESCINVIT);
        assertThat(testPerson.getIsVisa()).isEqualTo(UPDATED_IS_VISA);
        assertThat(testPerson.getDateaut()).isEqualTo(UPDATED_DATEAUT);
        assertThat(testPerson.getDatecreatefo()).isEqualTo(UPDATED_DATECREATEFO);
        assertThat(testPerson.getTelForm()).isEqualTo(UPDATED_TEL_FORM);
        assertThat(testPerson.getProfessionForm()).isEqualTo(UPDATED_PROFESSION_FORM);
        assertThat(testPerson.getReferenceForm()).isEqualTo(UPDATED_REFERENCE_FORM);
        assertThat(testPerson.getOtherNation()).isEqualTo(UPDATED_OTHER_NATION);
        assertThat(testPerson.getTelAutori()).isEqualTo(UPDATED_TEL_AUTORI);
        assertThat(testPerson.getEmailAutori()).isEqualTo(UPDATED_EMAIL_AUTORI);
        assertThat(testPerson.getNumSejour()).isEqualTo(UPDATED_NUM_SEJOUR);
        assertThat(testPerson.getNomEmp()).isEqualTo(UPDATED_NOM_EMP);
        assertThat(testPerson.getNumEmp()).isEqualTo(UPDATED_NUM_EMP);
        assertThat(testPerson.getVilleDestination()).isEqualTo(UPDATED_VILLE_DESTINATION);
        assertThat(testPerson.getInfoObjeVoyage()).isEqualTo(UPDATED_INFO_OBJE_VOYAGE);
        assertThat(testPerson.getAutoDelivrePar()).isEqualTo(UPDATED_AUTO_DELIVRE_PAR);
        assertThat(testPerson.getOtherMoyen()).isEqualTo(UPDATED_OTHER_MOYEN);
        assertThat(testPerson.getOtherLien()).isEqualTo(UPDATED_OTHER_LIEN);
        assertThat(testPerson.getFaxInvite()).isEqualTo(UPDATED_FAX_INVITE);
        assertThat(testPerson.getFaxEntreprise()).isEqualTo(UPDATED_FAX_ENTREPRISE);
        assertThat(testPerson.getNatActuel()).isEqualTo(UPDATED_NAT_ACTUEL);
        assertThat(testPerson.getNatDiffer()).isEqualTo(UPDATED_NAT_DIFFER);
        assertThat(testPerson.getNatMineur()).isEqualTo(UPDATED_NAT_MINEUR);
        assertThat(testPerson.getNatCitoyen()).isEqualTo(UPDATED_NAT_CITOYEN);
        assertThat(testPerson.getMoysub1()).isEqualTo(UPDATED_MOYSUB_1);
        assertThat(testPerson.getMoysub2()).isEqualTo(UPDATED_MOYSUB_2);
        assertThat(testPerson.getMoysub3()).isEqualTo(UPDATED_MOYSUB_3);
        assertThat(testPerson.getMoysub4()).isEqualTo(UPDATED_MOYSUB_4);
        assertThat(testPerson.getMoysub5()).isEqualTo(UPDATED_MOYSUB_5);
        assertThat(testPerson.getMoysub8()).isEqualTo(UPDATED_MOYSUB_8);
        assertThat(testPerson.getMoysubs1()).isEqualTo(UPDATED_MOYSUBS_1);
        assertThat(testPerson.getMoysubs5()).isEqualTo(UPDATED_MOYSUBS_5);
        assertThat(testPerson.getMoysubs6()).isEqualTo(UPDATED_MOYSUBS_6);
        assertThat(testPerson.getMoysubs7()).isEqualTo(UPDATED_MOYSUBS_7);
        assertThat(testPerson.getMoysubs8()).isEqualTo(UPDATED_MOYSUBS_8);
        assertThat(testPerson.getAutreFrais()).isEqualTo(UPDATED_AUTRE_FRAIS);
        assertThat(testPerson.getEtatCivil()).isEqualTo(UPDATED_ETAT_CIVIL);
        assertThat(testPerson.getIdLienF()).isEqualTo(UPDATED_ID_LIEN_F);
        assertThat(testPerson.getObjPerson()).isEqualTo(UPDATED_OBJ_PERSON);
        assertThat(testPerson.getIdSexeAvs()).isEqualTo(UPDATED_ID_SEXE_AVS);
        assertThat(testPerson.getTypeDocV()).isEqualTo(UPDATED_TYPE_DOC_V);
        assertThat(testPerson.getTypeFinance()).isEqualTo(UPDATED_TYPE_FINANCE);
        assertThat(testPerson.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, person.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(person))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Person in Elasticsearch
        verify(mockPersonSearchRepository, times(0)).save(person);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(person))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Person in Elasticsearch
        verify(mockPersonSearchRepository, times(0)).save(person);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Person in Elasticsearch
        verify(mockPersonSearchRepository, times(0)).save(person);
    }

    @Test
    @Transactional
    void deletePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Delete the person
        restPersonMockMvc
            .perform(delete(ENTITY_API_URL_ID, person.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Person in Elasticsearch
        verify(mockPersonSearchRepository, times(1)).deleteById(person.getId());
    }

    @Test
    @Transactional
    void searchPerson() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        personRepository.saveAndFlush(person);
        when(mockPersonSearchRepository.search(queryStringQuery("id:" + person.getId()))).thenReturn(Collections.singletonList(person));

        // Search the person
        restPersonMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].idMoyenSub").value(hasItem(DEFAULT_ID_MOYEN_SUB)))
            .andExpect(jsonPath("$.[*].nomPrec").value(hasItem(DEFAULT_NOM_PREC)))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE)))
            .andExpect(jsonPath("$.[*].etatCivilWeb").value(hasItem(DEFAULT_ETAT_CIVIL_WEB)))
            .andExpect(jsonPath("$.[*].nationPrec").value(hasItem(DEFAULT_NATION_PREC)))
            .andExpect(jsonPath("$.[*].nationActuel").value(hasItem(DEFAULT_NATION_ACTUEL)))
            .andExpect(jsonPath("$.[*].nomAutor").value(hasItem(DEFAULT_NOM_AUTOR)))
            .andExpect(jsonPath("$.[*].prenomAutor").value(hasItem(DEFAULT_PRENOM_AUTOR)))
            .andExpect(jsonPath("$.[*].adresAutor").value(hasItem(DEFAULT_ADRES_AUTOR)))
            .andExpect(jsonPath("$.[*].natiAutor").value(hasItem(DEFAULT_NATI_AUTOR)))
            .andExpect(jsonPath("$.[*].numCin").value(hasItem(DEFAULT_NUM_CIN)))
            .andExpect(jsonPath("$.[*].numDoc").value(hasItem(DEFAULT_NUM_DOC)))
            .andExpect(jsonPath("$.[*].dateDelivDoc").value(hasItem(DEFAULT_DATE_DELIV_DOC.toString())))
            .andExpect(jsonPath("$.[*].dateExpDoc").value(hasItem(DEFAULT_DATE_EXP_DOC.toString())))
            .andExpect(jsonPath("$.[*].delivParDoc").value(hasItem(DEFAULT_DELIV_PAR_DOC)))
            .andExpect(jsonPath("$.[*].adresseDomicile").value(hasItem(DEFAULT_ADRESSE_DOMICILE)))
            .andExpect(jsonPath("$.[*].adresseEmail").value(hasItem(DEFAULT_ADRESSE_EMAIL)))
            .andExpect(jsonPath("$.[*].isResident").value(hasItem(DEFAULT_IS_RESIDENT.booleanValue())))
            .andExpect(jsonPath("$.[*].titreSejour").value(hasItem(DEFAULT_TITRE_SEJOUR)))
            .andExpect(jsonPath("$.[*].dateExpSejour").value(hasItem(DEFAULT_DATE_EXP_SEJOUR.toString())))
            .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL)))
            .andExpect(jsonPath("$.[*].adressEmp").value(hasItem(DEFAULT_ADRESS_EMP)))
            .andExpect(jsonPath("$.[*].telEmp").value(hasItem(DEFAULT_TEL_EMP)))
            .andExpect(jsonPath("$.[*].nomEtab").value(hasItem(DEFAULT_NOM_ETAB)))
            .andExpect(jsonPath("$.[*].adressEtablis").value(hasItem(DEFAULT_ADRESS_ETABLIS)))
            .andExpect(jsonPath("$.[*].dureeSejour").value(hasItem(DEFAULT_DUREE_SEJOUR)))
            .andExpect(jsonPath("$.[*].etatMemDes").value(hasItem(DEFAULT_ETAT_MEM_DES)))
            .andExpect(jsonPath("$.[*].etatMemPremier").value(hasItem(DEFAULT_ETAT_MEM_PREMIER)))
            .andExpect(jsonPath("$.[*].nombreEntre").value(hasItem(DEFAULT_NOMBRE_ENTRE)))
            .andExpect(jsonPath("$.[*].oldVisaExiste").value(hasItem(DEFAULT_OLD_VISA_EXISTE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDelivDebut").value(hasItem(DEFAULT_DATE_DELIV_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateDelivFin").value(hasItem(DEFAULT_DATE_DELIV_FIN.toString())))
            .andExpect(jsonPath("$.[*].isEmprDegit").value(hasItem(DEFAULT_IS_EMPR_DEGIT.booleanValue())))
            .andExpect(jsonPath("$.[*].dateEmpreint").value(hasItem(DEFAULT_DATE_EMPREINT.toString())))
            .andExpect(jsonPath("$.[*].dateDelivAutor").value(hasItem(DEFAULT_DATE_DELIV_AUTOR.toString())))
            .andExpect(jsonPath("$.[*].dateValideAtorDebut").value(hasItem(DEFAULT_DATE_VALIDE_ATOR_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateValideAutorFin").value(hasItem(DEFAULT_DATE_VALIDE_AUTOR_FIN.toString())))
            .andExpect(jsonPath("$.[*].dateArrivPrevu").value(hasItem(DEFAULT_DATE_ARRIV_PREVU.toString())))
            .andExpect(jsonPath("$.[*].dateDepartPrevu").value(hasItem(DEFAULT_DATE_DEPART_PREVU.toString())))
            .andExpect(jsonPath("$.[*].descInvite").value(hasItem(DEFAULT_DESC_INVITE)))
            .andExpect(jsonPath("$.[*].adresseInvit").value(hasItem(DEFAULT_ADRESSE_INVIT)))
            .andExpect(jsonPath("$.[*].telInvite").value(hasItem(DEFAULT_TEL_INVITE)))
            .andExpect(jsonPath("$.[*].emailInvite").value(hasItem(DEFAULT_EMAIL_INVITE)))
            .andExpect(jsonPath("$.[*].nomEntreprise").value(hasItem(DEFAULT_NOM_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].adresseEntreprise").value(hasItem(DEFAULT_ADRESSE_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].contactEntreprise").value(hasItem(DEFAULT_CONTACT_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].descContactEntreprise").value(hasItem(DEFAULT_DESC_CONTACT_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].financeFraisVoyage").value(hasItem(DEFAULT_FINANCE_FRAIS_VOYAGE)))
            .andExpect(jsonPath("$.[*].nomCit").value(hasItem(DEFAULT_NOM_CIT)))
            .andExpect(jsonPath("$.[*].prenomCit").value(hasItem(DEFAULT_PRENOM_CIT)))
            .andExpect(jsonPath("$.[*].dateNaissCit").value(hasItem(DEFAULT_DATE_NAISS_CIT.toString())))
            .andExpect(jsonPath("$.[*].nationCit").value(hasItem(DEFAULT_NATION_CIT)))
            .andExpect(jsonPath("$.[*].numDocCit").value(hasItem(DEFAULT_NUM_DOC_CIT)))
            .andExpect(jsonPath("$.[*].lieuForm").value(hasItem(DEFAULT_LIEU_FORM)))
            .andExpect(jsonPath("$.[*].dateForm").value(hasItem(DEFAULT_DATE_FORM.toString())))
            .andExpect(jsonPath("$.[*].descPhotoForm").value(hasItem(DEFAULT_DESC_PHOTO_FORM)))
            .andExpect(jsonPath("$.[*].descForm1").value(hasItem(DEFAULT_DESC_FORM_1)))
            .andExpect(jsonPath("$.[*].descForm2").value(hasItem(DEFAULT_DESC_FORM_2)))
            .andExpect(jsonPath("$.[*].emailComp").value(hasItem(DEFAULT_EMAIL_COMP)))
            .andExpect(jsonPath("$.[*].nomForm").value(hasItem(DEFAULT_NOM_FORM)))
            .andExpect(jsonPath("$.[*].prenomForm").value(hasItem(DEFAULT_PRENOM_FORM)))
            .andExpect(jsonPath("$.[*].idPaysForm").value(hasItem(DEFAULT_ID_PAYS_FORM)))
            .andExpect(jsonPath("$.[*].descForm3").value(hasItem(DEFAULT_DESC_FORM_3)))
            .andExpect(jsonPath("$.[*].descForm4").value(hasItem(DEFAULT_DESC_FORM_4)))
            .andExpect(jsonPath("$.[*].autreetatcivil").value(hasItem(DEFAULT_AUTREETATCIVIL)))
            .andExpect(jsonPath("$.[*].autredoc").value(hasItem(DEFAULT_AUTREDOC)))
            .andExpect(jsonPath("$.[*].autreobj").value(hasItem(DEFAULT_AUTREOBJ)))
            .andExpect(jsonPath("$.[*].autrefris").value(hasItem(DEFAULT_AUTREFRIS)))
            .andExpect(jsonPath("$.[*].descpromot").value(hasItem(DEFAULT_DESCPROMOT)))
            .andExpect(jsonPath("$.[*].descinvit").value(hasItem(DEFAULT_DESCINVIT)))
            .andExpect(jsonPath("$.[*].isVisa").value(hasItem(DEFAULT_IS_VISA.booleanValue())))
            .andExpect(jsonPath("$.[*].dateaut").value(hasItem(DEFAULT_DATEAUT.toString())))
            .andExpect(jsonPath("$.[*].datecreatefo").value(hasItem(DEFAULT_DATECREATEFO.toString())))
            .andExpect(jsonPath("$.[*].telForm").value(hasItem(DEFAULT_TEL_FORM)))
            .andExpect(jsonPath("$.[*].professionForm").value(hasItem(DEFAULT_PROFESSION_FORM)))
            .andExpect(jsonPath("$.[*].referenceForm").value(hasItem(DEFAULT_REFERENCE_FORM)))
            .andExpect(jsonPath("$.[*].otherNation").value(hasItem(DEFAULT_OTHER_NATION)))
            .andExpect(jsonPath("$.[*].telAutori").value(hasItem(DEFAULT_TEL_AUTORI)))
            .andExpect(jsonPath("$.[*].emailAutori").value(hasItem(DEFAULT_EMAIL_AUTORI)))
            .andExpect(jsonPath("$.[*].numSejour").value(hasItem(DEFAULT_NUM_SEJOUR)))
            .andExpect(jsonPath("$.[*].nomEmp").value(hasItem(DEFAULT_NOM_EMP)))
            .andExpect(jsonPath("$.[*].numEmp").value(hasItem(DEFAULT_NUM_EMP)))
            .andExpect(jsonPath("$.[*].villeDestination").value(hasItem(DEFAULT_VILLE_DESTINATION)))
            .andExpect(jsonPath("$.[*].infoObjeVoyage").value(hasItem(DEFAULT_INFO_OBJE_VOYAGE)))
            .andExpect(jsonPath("$.[*].autoDelivrePar").value(hasItem(DEFAULT_AUTO_DELIVRE_PAR)))
            .andExpect(jsonPath("$.[*].otherMoyen").value(hasItem(DEFAULT_OTHER_MOYEN)))
            .andExpect(jsonPath("$.[*].otherLien").value(hasItem(DEFAULT_OTHER_LIEN)))
            .andExpect(jsonPath("$.[*].faxInvite").value(hasItem(DEFAULT_FAX_INVITE)))
            .andExpect(jsonPath("$.[*].faxEntreprise").value(hasItem(DEFAULT_FAX_ENTREPRISE)))
            .andExpect(jsonPath("$.[*].natActuel").value(hasItem(DEFAULT_NAT_ACTUEL)))
            .andExpect(jsonPath("$.[*].natDiffer").value(hasItem(DEFAULT_NAT_DIFFER)))
            .andExpect(jsonPath("$.[*].natMineur").value(hasItem(DEFAULT_NAT_MINEUR)))
            .andExpect(jsonPath("$.[*].natCitoyen").value(hasItem(DEFAULT_NAT_CITOYEN)))
            .andExpect(jsonPath("$.[*].moysub1").value(hasItem(DEFAULT_MOYSUB_1.booleanValue())))
            .andExpect(jsonPath("$.[*].moysub2").value(hasItem(DEFAULT_MOYSUB_2.booleanValue())))
            .andExpect(jsonPath("$.[*].moysub3").value(hasItem(DEFAULT_MOYSUB_3.booleanValue())))
            .andExpect(jsonPath("$.[*].moysub4").value(hasItem(DEFAULT_MOYSUB_4.booleanValue())))
            .andExpect(jsonPath("$.[*].moysub5").value(hasItem(DEFAULT_MOYSUB_5.booleanValue())))
            .andExpect(jsonPath("$.[*].moysub8").value(hasItem(DEFAULT_MOYSUB_8.booleanValue())))
            .andExpect(jsonPath("$.[*].moysubs1").value(hasItem(DEFAULT_MOYSUBS_1.booleanValue())))
            .andExpect(jsonPath("$.[*].moysubs5").value(hasItem(DEFAULT_MOYSUBS_5.booleanValue())))
            .andExpect(jsonPath("$.[*].moysubs6").value(hasItem(DEFAULT_MOYSUBS_6.booleanValue())))
            .andExpect(jsonPath("$.[*].moysubs7").value(hasItem(DEFAULT_MOYSUBS_7.booleanValue())))
            .andExpect(jsonPath("$.[*].moysubs8").value(hasItem(DEFAULT_MOYSUBS_8.booleanValue())))
            .andExpect(jsonPath("$.[*].autreFrais").value(hasItem(DEFAULT_AUTRE_FRAIS)))
            .andExpect(jsonPath("$.[*].etatCivil").value(hasItem(DEFAULT_ETAT_CIVIL)))
            .andExpect(jsonPath("$.[*].idLienF").value(hasItem(DEFAULT_ID_LIEN_F)))
            .andExpect(jsonPath("$.[*].objPerson").value(hasItem(DEFAULT_OBJ_PERSON)))
            .andExpect(jsonPath("$.[*].idSexeAvs").value(hasItem(DEFAULT_ID_SEXE_AVS)))
            .andExpect(jsonPath("$.[*].typeDocV").value(hasItem(DEFAULT_TYPE_DOC_V)))
            .andExpect(jsonPath("$.[*].typeFinance").value(hasItem(DEFAULT_TYPE_FINANCE)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
}
