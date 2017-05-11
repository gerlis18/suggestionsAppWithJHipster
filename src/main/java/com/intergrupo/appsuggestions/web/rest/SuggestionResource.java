package com.intergrupo.appsuggestions.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.intergrupo.appsuggestions.domain.Suggestion;

import com.intergrupo.appsuggestions.repository.SuggestionRepository;
import com.intergrupo.appsuggestions.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Suggestion.
 */
@RestController
@RequestMapping("/api")
public class SuggestionResource {

    private final Logger log = LoggerFactory.getLogger(SuggestionResource.class);

    private static final String ENTITY_NAME = "suggestion";
        
    private final SuggestionRepository suggestionRepository;

    public SuggestionResource(SuggestionRepository suggestionRepository) {
        this.suggestionRepository = suggestionRepository;
    }

    /**
     * POST  /suggestions : Create a new suggestion.
     *
     * @param suggestion the suggestion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new suggestion, or with status 400 (Bad Request) if the suggestion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/suggestions")
    @Timed
    public ResponseEntity<Suggestion> createSuggestion(@Valid @RequestBody Suggestion suggestion) throws URISyntaxException {
        log.debug("REST request to save Suggestion : {}", suggestion);
        if (suggestion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new suggestion cannot already have an ID")).body(null);
        }
        Suggestion result = suggestionRepository.save(suggestion);
        return ResponseEntity.created(new URI("/api/suggestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /suggestions : Updates an existing suggestion.
     *
     * @param suggestion the suggestion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated suggestion,
     * or with status 400 (Bad Request) if the suggestion is not valid,
     * or with status 500 (Internal Server Error) if the suggestion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/suggestions")
    @Timed
    public ResponseEntity<Suggestion> updateSuggestion(@Valid @RequestBody Suggestion suggestion) throws URISyntaxException {
        log.debug("REST request to update Suggestion : {}", suggestion);
        if (suggestion.getId() == null) {
            return createSuggestion(suggestion);
        }
        Suggestion result = suggestionRepository.save(suggestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, suggestion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /suggestions : get all the suggestions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of suggestions in body
     */
    @GetMapping("/suggestions")
    @Timed
    public List<Suggestion> getAllSuggestions() {
        log.debug("REST request to get all Suggestions");
        List<Suggestion> suggestions = suggestionRepository.findAll();
        return suggestions;
    }

    /**
     * GET  /suggestions/:id : get the "id" suggestion.
     *
     * @param id the id of the suggestion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the suggestion, or with status 404 (Not Found)
     */
    @GetMapping("/suggestions/{id}")
    @Timed
    public ResponseEntity<Suggestion> getSuggestion(@PathVariable Long id) {
        log.debug("REST request to get Suggestion : {}", id);
        Suggestion suggestion = suggestionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(suggestion));
    }

    /**
     * DELETE  /suggestions/:id : delete the "id" suggestion.
     *
     * @param id the id of the suggestion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/suggestions/{id}")
    @Timed
    public ResponseEntity<Void> deleteSuggestion(@PathVariable Long id) {
        log.debug("REST request to delete Suggestion : {}", id);
        suggestionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
