package com.almaviva.vs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.almaviva.vs.domain.Person;
import com.almaviva.vs.repository.PersonRepository;
import com.almaviva.vs.repository.search.PersonSearchRepository;
import com.almaviva.vs.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.almaviva.vs.domain.Person}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PersonResource {

    private final Logger log = LoggerFactory.getLogger(PersonResource.class);

    private static final String ENTITY_NAME = "person";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonRepository personRepository;

    private final PersonSearchRepository personSearchRepository;

    public PersonResource(PersonRepository personRepository, PersonSearchRepository personSearchRepository) {
        this.personRepository = personRepository;
        this.personSearchRepository = personSearchRepository;
    }

    /**
     * {@code POST  /people} : Create a new person.
     *
     * @param person the person to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new person, or with status {@code 400 (Bad Request)} if the person has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/people")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) throws URISyntaxException {
        log.debug("REST request to save Person : {}", person);
        if (person.getId() != null) {
            throw new BadRequestAlertException("A new person cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Person result = personRepository.save(person);
        personSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /people/:id} : Updates an existing person.
     *
     * @param id the id of the person to save.
     * @param person the person to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated person,
     * or with status {@code 400 (Bad Request)} if the person is not valid,
     * or with status {@code 500 (Internal Server Error)} if the person couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/people/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable(value = "id", required = false) final Long id, @RequestBody Person person)
        throws URISyntaxException {
        log.debug("REST request to update Person : {}, {}", id, person);
        if (person.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, person.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Person result = personRepository.save(person);
        personSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, person.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /people/:id} : Partial updates given fields of an existing person, field will ignore if it is null
     *
     * @param id the id of the person to save.
     * @param person the person to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated person,
     * or with status {@code 400 (Bad Request)} if the person is not valid,
     * or with status {@code 404 (Not Found)} if the person is not found,
     * or with status {@code 500 (Internal Server Error)} if the person couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/people/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Person> partialUpdatePerson(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Person person
    ) throws URISyntaxException {
        log.debug("REST request to partial update Person partially : {}, {}", id, person);
        if (person.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, person.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Person> result = personRepository
            .findById(person.getId())
            .map(
                existingPerson -> {
                    if (person.getIdMoyenSub() != null) {
                        existingPerson.setIdMoyenSub(person.getIdMoyenSub());
                    }
                    if (person.getNomPrec() != null) {
                        existingPerson.setNomPrec(person.getNomPrec());
                    }
                    if (person.getLieuNaissance() != null) {
                        existingPerson.setLieuNaissance(person.getLieuNaissance());
                    }
                    if (person.getEtatCivilWeb() != null) {
                        existingPerson.setEtatCivilWeb(person.getEtatCivilWeb());
                    }
                    if (person.getNationPrec() != null) {
                        existingPerson.setNationPrec(person.getNationPrec());
                    }
                    if (person.getNationActuel() != null) {
                        existingPerson.setNationActuel(person.getNationActuel());
                    }
                    if (person.getNomAutor() != null) {
                        existingPerson.setNomAutor(person.getNomAutor());
                    }
                    if (person.getPrenomAutor() != null) {
                        existingPerson.setPrenomAutor(person.getPrenomAutor());
                    }
                    if (person.getAdresAutor() != null) {
                        existingPerson.setAdresAutor(person.getAdresAutor());
                    }
                    if (person.getNatiAutor() != null) {
                        existingPerson.setNatiAutor(person.getNatiAutor());
                    }
                    if (person.getNumCin() != null) {
                        existingPerson.setNumCin(person.getNumCin());
                    }
                    if (person.getNumDoc() != null) {
                        existingPerson.setNumDoc(person.getNumDoc());
                    }
                    if (person.getDateDelivDoc() != null) {
                        existingPerson.setDateDelivDoc(person.getDateDelivDoc());
                    }
                    if (person.getDateExpDoc() != null) {
                        existingPerson.setDateExpDoc(person.getDateExpDoc());
                    }
                    if (person.getDelivParDoc() != null) {
                        existingPerson.setDelivParDoc(person.getDelivParDoc());
                    }
                    if (person.getAdresseDomicile() != null) {
                        existingPerson.setAdresseDomicile(person.getAdresseDomicile());
                    }
                    if (person.getAdresseEmail() != null) {
                        existingPerson.setAdresseEmail(person.getAdresseEmail());
                    }
                    if (person.getIsResident() != null) {
                        existingPerson.setIsResident(person.getIsResident());
                    }
                    if (person.getTitreSejour() != null) {
                        existingPerson.setTitreSejour(person.getTitreSejour());
                    }
                    if (person.getDateExpSejour() != null) {
                        existingPerson.setDateExpSejour(person.getDateExpSejour());
                    }
                    if (person.getNumTel() != null) {
                        existingPerson.setNumTel(person.getNumTel());
                    }
                    if (person.getAdressEmp() != null) {
                        existingPerson.setAdressEmp(person.getAdressEmp());
                    }
                    if (person.getTelEmp() != null) {
                        existingPerson.setTelEmp(person.getTelEmp());
                    }
                    if (person.getNomEtab() != null) {
                        existingPerson.setNomEtab(person.getNomEtab());
                    }
                    if (person.getAdressEtablis() != null) {
                        existingPerson.setAdressEtablis(person.getAdressEtablis());
                    }
                    if (person.getDureeSejour() != null) {
                        existingPerson.setDureeSejour(person.getDureeSejour());
                    }
                    if (person.getEtatMemDes() != null) {
                        existingPerson.setEtatMemDes(person.getEtatMemDes());
                    }
                    if (person.getEtatMemPremier() != null) {
                        existingPerson.setEtatMemPremier(person.getEtatMemPremier());
                    }
                    if (person.getNombreEntre() != null) {
                        existingPerson.setNombreEntre(person.getNombreEntre());
                    }
                    if (person.getOldVisaExiste() != null) {
                        existingPerson.setOldVisaExiste(person.getOldVisaExiste());
                    }
                    if (person.getDateDelivDebut() != null) {
                        existingPerson.setDateDelivDebut(person.getDateDelivDebut());
                    }
                    if (person.getDateDelivFin() != null) {
                        existingPerson.setDateDelivFin(person.getDateDelivFin());
                    }
                    if (person.getIsEmprDegit() != null) {
                        existingPerson.setIsEmprDegit(person.getIsEmprDegit());
                    }
                    if (person.getDateEmpreint() != null) {
                        existingPerson.setDateEmpreint(person.getDateEmpreint());
                    }
                    if (person.getDateDelivAutor() != null) {
                        existingPerson.setDateDelivAutor(person.getDateDelivAutor());
                    }
                    if (person.getDateValideAtorDebut() != null) {
                        existingPerson.setDateValideAtorDebut(person.getDateValideAtorDebut());
                    }
                    if (person.getDateValideAutorFin() != null) {
                        existingPerson.setDateValideAutorFin(person.getDateValideAutorFin());
                    }
                    if (person.getDateArrivPrevu() != null) {
                        existingPerson.setDateArrivPrevu(person.getDateArrivPrevu());
                    }
                    if (person.getDateDepartPrevu() != null) {
                        existingPerson.setDateDepartPrevu(person.getDateDepartPrevu());
                    }
                    if (person.getDescInvite() != null) {
                        existingPerson.setDescInvite(person.getDescInvite());
                    }
                    if (person.getAdresseInvit() != null) {
                        existingPerson.setAdresseInvit(person.getAdresseInvit());
                    }
                    if (person.getTelInvite() != null) {
                        existingPerson.setTelInvite(person.getTelInvite());
                    }
                    if (person.getEmailInvite() != null) {
                        existingPerson.setEmailInvite(person.getEmailInvite());
                    }
                    if (person.getNomEntreprise() != null) {
                        existingPerson.setNomEntreprise(person.getNomEntreprise());
                    }
                    if (person.getAdresseEntreprise() != null) {
                        existingPerson.setAdresseEntreprise(person.getAdresseEntreprise());
                    }
                    if (person.getContactEntreprise() != null) {
                        existingPerson.setContactEntreprise(person.getContactEntreprise());
                    }
                    if (person.getDescContactEntreprise() != null) {
                        existingPerson.setDescContactEntreprise(person.getDescContactEntreprise());
                    }
                    if (person.getFinanceFraisVoyage() != null) {
                        existingPerson.setFinanceFraisVoyage(person.getFinanceFraisVoyage());
                    }
                    if (person.getNomCit() != null) {
                        existingPerson.setNomCit(person.getNomCit());
                    }
                    if (person.getPrenomCit() != null) {
                        existingPerson.setPrenomCit(person.getPrenomCit());
                    }
                    if (person.getDateNaissCit() != null) {
                        existingPerson.setDateNaissCit(person.getDateNaissCit());
                    }
                    if (person.getNationCit() != null) {
                        existingPerson.setNationCit(person.getNationCit());
                    }
                    if (person.getNumDocCit() != null) {
                        existingPerson.setNumDocCit(person.getNumDocCit());
                    }
                    if (person.getLieuForm() != null) {
                        existingPerson.setLieuForm(person.getLieuForm());
                    }
                    if (person.getDateForm() != null) {
                        existingPerson.setDateForm(person.getDateForm());
                    }
                    if (person.getDescPhotoForm() != null) {
                        existingPerson.setDescPhotoForm(person.getDescPhotoForm());
                    }
                    if (person.getDescForm1() != null) {
                        existingPerson.setDescForm1(person.getDescForm1());
                    }
                    if (person.getDescForm2() != null) {
                        existingPerson.setDescForm2(person.getDescForm2());
                    }
                    if (person.getEmailComp() != null) {
                        existingPerson.setEmailComp(person.getEmailComp());
                    }
                    if (person.getNomForm() != null) {
                        existingPerson.setNomForm(person.getNomForm());
                    }
                    if (person.getPrenomForm() != null) {
                        existingPerson.setPrenomForm(person.getPrenomForm());
                    }
                    if (person.getIdPaysForm() != null) {
                        existingPerson.setIdPaysForm(person.getIdPaysForm());
                    }
                    if (person.getDescForm3() != null) {
                        existingPerson.setDescForm3(person.getDescForm3());
                    }
                    if (person.getDescForm4() != null) {
                        existingPerson.setDescForm4(person.getDescForm4());
                    }
                    if (person.getAutreetatcivil() != null) {
                        existingPerson.setAutreetatcivil(person.getAutreetatcivil());
                    }
                    if (person.getAutredoc() != null) {
                        existingPerson.setAutredoc(person.getAutredoc());
                    }
                    if (person.getAutreobj() != null) {
                        existingPerson.setAutreobj(person.getAutreobj());
                    }
                    if (person.getAutrefris() != null) {
                        existingPerson.setAutrefris(person.getAutrefris());
                    }
                    if (person.getDescpromot() != null) {
                        existingPerson.setDescpromot(person.getDescpromot());
                    }
                    if (person.getDescinvit() != null) {
                        existingPerson.setDescinvit(person.getDescinvit());
                    }
                    if (person.getIsVisa() != null) {
                        existingPerson.setIsVisa(person.getIsVisa());
                    }
                    if (person.getDateaut() != null) {
                        existingPerson.setDateaut(person.getDateaut());
                    }
                    if (person.getDatecreatefo() != null) {
                        existingPerson.setDatecreatefo(person.getDatecreatefo());
                    }
                    if (person.getTelForm() != null) {
                        existingPerson.setTelForm(person.getTelForm());
                    }
                    if (person.getProfessionForm() != null) {
                        existingPerson.setProfessionForm(person.getProfessionForm());
                    }
                    if (person.getReferenceForm() != null) {
                        existingPerson.setReferenceForm(person.getReferenceForm());
                    }
                    if (person.getOtherNation() != null) {
                        existingPerson.setOtherNation(person.getOtherNation());
                    }
                    if (person.getTelAutori() != null) {
                        existingPerson.setTelAutori(person.getTelAutori());
                    }
                    if (person.getEmailAutori() != null) {
                        existingPerson.setEmailAutori(person.getEmailAutori());
                    }
                    if (person.getNumSejour() != null) {
                        existingPerson.setNumSejour(person.getNumSejour());
                    }
                    if (person.getNomEmp() != null) {
                        existingPerson.setNomEmp(person.getNomEmp());
                    }
                    if (person.getNumEmp() != null) {
                        existingPerson.setNumEmp(person.getNumEmp());
                    }
                    if (person.getVilleDestination() != null) {
                        existingPerson.setVilleDestination(person.getVilleDestination());
                    }
                    if (person.getInfoObjeVoyage() != null) {
                        existingPerson.setInfoObjeVoyage(person.getInfoObjeVoyage());
                    }
                    if (person.getAutoDelivrePar() != null) {
                        existingPerson.setAutoDelivrePar(person.getAutoDelivrePar());
                    }
                    if (person.getOtherMoyen() != null) {
                        existingPerson.setOtherMoyen(person.getOtherMoyen());
                    }
                    if (person.getOtherLien() != null) {
                        existingPerson.setOtherLien(person.getOtherLien());
                    }
                    if (person.getFaxInvite() != null) {
                        existingPerson.setFaxInvite(person.getFaxInvite());
                    }
                    if (person.getFaxEntreprise() != null) {
                        existingPerson.setFaxEntreprise(person.getFaxEntreprise());
                    }
                    if (person.getNatActuel() != null) {
                        existingPerson.setNatActuel(person.getNatActuel());
                    }
                    if (person.getNatDiffer() != null) {
                        existingPerson.setNatDiffer(person.getNatDiffer());
                    }
                    if (person.getNatMineur() != null) {
                        existingPerson.setNatMineur(person.getNatMineur());
                    }
                    if (person.getNatCitoyen() != null) {
                        existingPerson.setNatCitoyen(person.getNatCitoyen());
                    }
                    if (person.getMoysub1() != null) {
                        existingPerson.setMoysub1(person.getMoysub1());
                    }
                    if (person.getMoysub2() != null) {
                        existingPerson.setMoysub2(person.getMoysub2());
                    }
                    if (person.getMoysub3() != null) {
                        existingPerson.setMoysub3(person.getMoysub3());
                    }
                    if (person.getMoysub4() != null) {
                        existingPerson.setMoysub4(person.getMoysub4());
                    }
                    if (person.getMoysub5() != null) {
                        existingPerson.setMoysub5(person.getMoysub5());
                    }
                    if (person.getMoysub8() != null) {
                        existingPerson.setMoysub8(person.getMoysub8());
                    }
                    if (person.getMoysubs1() != null) {
                        existingPerson.setMoysubs1(person.getMoysubs1());
                    }
                    if (person.getMoysubs5() != null) {
                        existingPerson.setMoysubs5(person.getMoysubs5());
                    }
                    if (person.getMoysubs6() != null) {
                        existingPerson.setMoysubs6(person.getMoysubs6());
                    }
                    if (person.getMoysubs7() != null) {
                        existingPerson.setMoysubs7(person.getMoysubs7());
                    }
                    if (person.getMoysubs8() != null) {
                        existingPerson.setMoysubs8(person.getMoysubs8());
                    }
                    if (person.getAutreFrais() != null) {
                        existingPerson.setAutreFrais(person.getAutreFrais());
                    }
                    if (person.getEtatCivil() != null) {
                        existingPerson.setEtatCivil(person.getEtatCivil());
                    }
                    if (person.getIdLienF() != null) {
                        existingPerson.setIdLienF(person.getIdLienF());
                    }
                    if (person.getObjPerson() != null) {
                        existingPerson.setObjPerson(person.getObjPerson());
                    }
                    if (person.getIdSexeAvs() != null) {
                        existingPerson.setIdSexeAvs(person.getIdSexeAvs());
                    }
                    if (person.getTypeDocV() != null) {
                        existingPerson.setTypeDocV(person.getTypeDocV());
                    }
                    if (person.getTypeFinance() != null) {
                        existingPerson.setTypeFinance(person.getTypeFinance());
                    }
                    if (person.getDeleted() != null) {
                        existingPerson.setDeleted(person.getDeleted());
                    }

                    return existingPerson;
                }
            )
            .map(personRepository::save)
            .map(
                savedPerson -> {
                    personSearchRepository.save(savedPerson);

                    return savedPerson;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, person.getId().toString())
        );
    }

    /**
     * {@code GET  /people} : get all the people.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of people in body.
     */
    @GetMapping("/people")
    public List<Person> getAllPeople() {
        log.debug("REST request to get all People");
        return personRepository.findAll();
    }

    /**
     * {@code GET  /people/:id} : get the "id" person.
     *
     * @param id the id of the person to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the person, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/people/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable Long id) {
        log.debug("REST request to get Person : {}", id);
        Optional<Person> person = personRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(person);
    }

    /**
     * {@code DELETE  /people/:id} : delete the "id" person.
     *
     * @param id the id of the person to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/people/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        log.debug("REST request to delete Person : {}", id);
        personRepository.deleteById(id);
        personSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/people?query=:query} : search for the person corresponding
     * to the query.
     *
     * @param query the query of the person search.
     * @return the result of the search.
     */
    @GetMapping("/_search/people")
    public List<Person> searchPeople(@RequestParam String query) {
        log.debug("REST request to search People for query {}", query);
        return StreamSupport
            .stream(personSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
