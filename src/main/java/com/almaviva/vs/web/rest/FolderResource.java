package com.almaviva.vs.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.almaviva.vs.domain.Folder;
import com.almaviva.vs.repository.FolderRepository;
import com.almaviva.vs.repository.search.FolderSearchRepository;
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
 * REST controller for managing {@link com.almaviva.vs.domain.Folder}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FolderResource {

    private final Logger log = LoggerFactory.getLogger(FolderResource.class);

    private static final String ENTITY_NAME = "folder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FolderRepository folderRepository;

    private final FolderSearchRepository folderSearchRepository;

    public FolderResource(FolderRepository folderRepository, FolderSearchRepository folderSearchRepository) {
        this.folderRepository = folderRepository;
        this.folderSearchRepository = folderSearchRepository;
    }

    /**
     * {@code POST  /folders} : Create a new folder.
     *
     * @param folder the folder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new folder, or with status {@code 400 (Bad Request)} if the folder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/folders")
    public ResponseEntity<Folder> createFolder(@RequestBody Folder folder) throws URISyntaxException {
        log.debug("REST request to save Folder : {}", folder);
        if (folder.getId() != null) {
            throw new BadRequestAlertException("A new folder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Folder result = folderRepository.save(folder);
        folderSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/folders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /folders/:id} : Updates an existing folder.
     *
     * @param id the id of the folder to save.
     * @param folder the folder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated folder,
     * or with status {@code 400 (Bad Request)} if the folder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the folder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/folders/{id}")
    public ResponseEntity<Folder> updateFolder(@PathVariable(value = "id", required = false) final Long id, @RequestBody Folder folder)
        throws URISyntaxException {
        log.debug("REST request to update Folder : {}, {}", id, folder);
        if (folder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, folder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!folderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Folder result = folderRepository.save(folder);
        folderSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, folder.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /folders/:id} : Partial updates given fields of an existing folder, field will ignore if it is null
     *
     * @param id the id of the folder to save.
     * @param folder the folder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated folder,
     * or with status {@code 400 (Bad Request)} if the folder is not valid,
     * or with status {@code 404 (Not Found)} if the folder is not found,
     * or with status {@code 500 (Internal Server Error)} if the folder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/folders/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Folder> partialUpdateFolder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Folder folder
    ) throws URISyntaxException {
        log.debug("REST request to partial update Folder partially : {}, {}", id, folder);
        if (folder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, folder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!folderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Folder> result = folderRepository
            .findById(folder.getId())
            .map(
                existingFolder -> {
                    if (folder.getFolderId() != null) {
                        existingFolder.setFolderId(folder.getFolderId());
                    }
                    if (folder.getStatus() != null) {
                        existingFolder.setStatus(folder.getStatus());
                    }
                    if (folder.getPaymentMethod() != null) {
                        existingFolder.setPaymentMethod(folder.getPaymentMethod());
                    }
                    if (folder.getWaitingRoom() != null) {
                        existingFolder.setWaitingRoom(folder.getWaitingRoom());
                    }
                    if (folder.getServiceType() != null) {
                        existingFolder.setServiceType(folder.getServiceType());
                    }
                    if (folder.getIsAvsFree() != null) {
                        existingFolder.setIsAvsFree(folder.getIsAvsFree());
                    }
                    if (folder.getDeleted() != null) {
                        existingFolder.setDeleted(folder.getDeleted());
                    }

                    return existingFolder;
                }
            )
            .map(folderRepository::save)
            .map(
                savedFolder -> {
                    folderSearchRepository.save(savedFolder);

                    return savedFolder;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, folder.getId().toString())
        );
    }

    /**
     * {@code GET  /folders} : get all the folders.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of folders in body.
     */
    @GetMapping("/folders")
    public List<Folder> getAllFolders(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Folders");
        return folderRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /folders/:id} : get the "id" folder.
     *
     * @param id the id of the folder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the folder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/folders/{id}")
    public ResponseEntity<Folder> getFolder(@PathVariable Long id) {
        log.debug("REST request to get Folder : {}", id);
        Optional<Folder> folder = folderRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(folder);
    }

    /**
     * {@code DELETE  /folders/:id} : delete the "id" folder.
     *
     * @param id the id of the folder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/folders/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable Long id) {
        log.debug("REST request to delete Folder : {}", id);
        folderRepository.deleteById(id);
        folderSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/folders?query=:query} : search for the folder corresponding
     * to the query.
     *
     * @param query the query of the folder search.
     * @return the result of the search.
     */
    @GetMapping("/_search/folders")
    public List<Folder> searchFolders(@RequestParam String query) {
        log.debug("REST request to search Folders for query {}", query);
        return StreamSupport
            .stream(folderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
