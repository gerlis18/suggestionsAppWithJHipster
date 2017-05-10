package com.intergrupo.appsuggestions.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.intergrupo.appsuggestions.domain.SuggestionBlog;

import com.intergrupo.appsuggestions.repository.SuggestionBlogRepository;
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
 * REST controller for managing SuggestionBlog.
 */
@RestController
@RequestMapping("/api")
public class SuggestionBlogResource {

    private final Logger log = LoggerFactory.getLogger(SuggestionBlogResource.class);

    private static final String ENTITY_NAME = "suggestionBlog";
        
    private final SuggestionBlogRepository suggestionBlogRepository;

    public SuggestionBlogResource(SuggestionBlogRepository suggestionBlogRepository) {
        this.suggestionBlogRepository = suggestionBlogRepository;
    }

    /**
     * POST  /suggestion-blogs : Create a new suggestionBlog.
     *
     * @param suggestionBlog the suggestionBlog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new suggestionBlog, or with status 400 (Bad Request) if the suggestionBlog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/suggestion-blogs")
    @Timed
    public ResponseEntity<SuggestionBlog> createSuggestionBlog(@Valid @RequestBody SuggestionBlog suggestionBlog) throws URISyntaxException {
        log.debug("REST request to save SuggestionBlog : {}", suggestionBlog);
        if (suggestionBlog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new suggestionBlog cannot already have an ID")).body(null);
        }
        SuggestionBlog result = suggestionBlogRepository.save(suggestionBlog);
        return ResponseEntity.created(new URI("/api/suggestion-blogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /suggestion-blogs : Updates an existing suggestionBlog.
     *
     * @param suggestionBlog the suggestionBlog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated suggestionBlog,
     * or with status 400 (Bad Request) if the suggestionBlog is not valid,
     * or with status 500 (Internal Server Error) if the suggestionBlog couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/suggestion-blogs")
    @Timed
    public ResponseEntity<SuggestionBlog> updateSuggestionBlog(@Valid @RequestBody SuggestionBlog suggestionBlog) throws URISyntaxException {
        log.debug("REST request to update SuggestionBlog : {}", suggestionBlog);
        if (suggestionBlog.getId() == null) {
            return createSuggestionBlog(suggestionBlog);
        }
        SuggestionBlog result = suggestionBlogRepository.save(suggestionBlog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, suggestionBlog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /suggestion-blogs : get all the suggestionBlogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of suggestionBlogs in body
     */
    @GetMapping("/suggestion-blogs")
    @Timed
    public List<SuggestionBlog> getAllSuggestionBlogs() {
        log.debug("REST request to get all SuggestionBlogs");
        List<SuggestionBlog> suggestionBlogs = suggestionBlogRepository.findAll();
        return suggestionBlogs;
    }

    /**
     * GET  /suggestion-blogs/:id : get the "id" suggestionBlog.
     *
     * @param id the id of the suggestionBlog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the suggestionBlog, or with status 404 (Not Found)
     */
    @GetMapping("/suggestion-blogs/{id}")
    @Timed
    public ResponseEntity<SuggestionBlog> getSuggestionBlog(@PathVariable Long id) {
        log.debug("REST request to get SuggestionBlog : {}", id);
        SuggestionBlog suggestionBlog = suggestionBlogRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(suggestionBlog));
    }

    /**
     * DELETE  /suggestion-blogs/:id : delete the "id" suggestionBlog.
     *
     * @param id the id of the suggestionBlog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/suggestion-blogs/{id}")
    @Timed
    public ResponseEntity<Void> deleteSuggestionBlog(@PathVariable Long id) {
        log.debug("REST request to delete SuggestionBlog : {}", id);
        suggestionBlogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
