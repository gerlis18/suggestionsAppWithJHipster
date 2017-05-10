package com.intergrupo.appsuggestions.web.rest;

import com.intergrupo.appsuggestions.SuggestionsApp;

import com.intergrupo.appsuggestions.domain.Suggestion;
import com.intergrupo.appsuggestions.domain.User;
import com.intergrupo.appsuggestions.repository.SuggestionRepository;
import com.intergrupo.appsuggestions.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.intergrupo.appsuggestions.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SuggestionResource REST controller.
 *
 * @see SuggestionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SuggestionsApp.class)
public class SuggestionResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SuggestionRepository suggestionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSuggestionMockMvc;

    private Suggestion suggestion;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SuggestionResource suggestionResource = new SuggestionResource(suggestionRepository);
        this.restSuggestionMockMvc = MockMvcBuilders.standaloneSetup(suggestionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suggestion createEntity(EntityManager em) {
        Suggestion suggestion = new Suggestion()
            .title(DEFAULT_TITLE)
            .message(DEFAULT_MESSAGE)
            .create(DEFAULT_CREATE);
        // Add required entity
        User addressee = UserResourceIntTest.createEntity(em);
        em.persist(addressee);
        em.flush();
        suggestion.setAddressee(addressee);
        return suggestion;
    }

    @Before
    public void initTest() {
        suggestion = createEntity(em);
    }

    @Test
    @Transactional
    public void createSuggestion() throws Exception {
        int databaseSizeBeforeCreate = suggestionRepository.findAll().size();

        // Create the Suggestion
        restSuggestionMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isCreated());

        // Validate the Suggestion in the database
        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeCreate + 1);
        Suggestion testSuggestion = suggestionList.get(suggestionList.size() - 1);
        assertThat(testSuggestion.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSuggestion.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testSuggestion.getCreate()).isEqualTo(DEFAULT_CREATE);
    }

    @Test
    @Transactional
    public void createSuggestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = suggestionRepository.findAll().size();

        // Create the Suggestion with an existing ID
        suggestion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuggestionMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestionRepository.findAll().size();
        // set the field null
        suggestion.setTitle(null);

        // Create the Suggestion, which fails.

        restSuggestionMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isBadRequest());

        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestionRepository.findAll().size();
        // set the field null
        suggestion.setMessage(null);

        // Create the Suggestion, which fails.

        restSuggestionMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isBadRequest());

        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuggestions() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestionList
        restSuggestionMockMvc.perform(get("/api/suggestions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suggestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].create").value(hasItem(sameInstant(DEFAULT_CREATE))));
    }

    @Test
    @Transactional
    public void getSuggestion() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get the suggestion
        restSuggestionMockMvc.perform(get("/api/suggestions/{id}", suggestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(suggestion.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.create").value(sameInstant(DEFAULT_CREATE)));
    }

    @Test
    @Transactional
    public void getNonExistingSuggestion() throws Exception {
        // Get the suggestion
        restSuggestionMockMvc.perform(get("/api/suggestions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuggestion() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);
        int databaseSizeBeforeUpdate = suggestionRepository.findAll().size();

        // Update the suggestion
        Suggestion updatedSuggestion = suggestionRepository.findOne(suggestion.getId());
        updatedSuggestion
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .create(UPDATED_CREATE);

        restSuggestionMockMvc.perform(put("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSuggestion)))
            .andExpect(status().isOk());

        // Validate the Suggestion in the database
        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeUpdate);
        Suggestion testSuggestion = suggestionList.get(suggestionList.size() - 1);
        assertThat(testSuggestion.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSuggestion.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testSuggestion.getCreate()).isEqualTo(UPDATED_CREATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSuggestion() throws Exception {
        int databaseSizeBeforeUpdate = suggestionRepository.findAll().size();

        // Create the Suggestion

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSuggestionMockMvc.perform(put("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestion)))
            .andExpect(status().isCreated());

        // Validate the Suggestion in the database
        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSuggestion() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);
        int databaseSizeBeforeDelete = suggestionRepository.findAll().size();

        // Get the suggestion
        restSuggestionMockMvc.perform(delete("/api/suggestions/{id}", suggestion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Suggestion> suggestionList = suggestionRepository.findAll();
        assertThat(suggestionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Suggestion.class);
    }
}
