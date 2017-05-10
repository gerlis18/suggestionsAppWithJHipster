package com.intergrupo.appsuggestions.web.rest;

import com.intergrupo.appsuggestions.SuggestionsApp;

import com.intergrupo.appsuggestions.domain.SuggestionBlog;
import com.intergrupo.appsuggestions.domain.User;
import com.intergrupo.appsuggestions.domain.Category;
import com.intergrupo.appsuggestions.repository.SuggestionBlogRepository;
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
 * Test class for the SuggestionBlogResource REST controller.
 *
 * @see SuggestionBlogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SuggestionsApp.class)
public class SuggestionBlogResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SuggestionBlogRepository suggestionBlogRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSuggestionBlogMockMvc;

    private SuggestionBlog suggestionBlog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SuggestionBlogResource suggestionBlogResource = new SuggestionBlogResource(suggestionBlogRepository);
        this.restSuggestionBlogMockMvc = MockMvcBuilders.standaloneSetup(suggestionBlogResource)
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
    public static SuggestionBlog createEntity(EntityManager em) {
        SuggestionBlog suggestionBlog = new SuggestionBlog()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .create(DEFAULT_CREATE);
        // Add required entity
        User author = UserResourceIntTest.createEntity(em);
        em.persist(author);
        em.flush();
        suggestionBlog.setAuthor(author);
        // Add required entity
        Category category = CategoryResourceIntTest.createEntity(em);
        em.persist(category);
        em.flush();
        suggestionBlog.setCategory(category);
        return suggestionBlog;
    }

    @Before
    public void initTest() {
        suggestionBlog = createEntity(em);
    }

    @Test
    @Transactional
    public void createSuggestionBlog() throws Exception {
        int databaseSizeBeforeCreate = suggestionBlogRepository.findAll().size();

        // Create the SuggestionBlog
        restSuggestionBlogMockMvc.perform(post("/api/suggestion-blogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestionBlog)))
            .andExpect(status().isCreated());

        // Validate the SuggestionBlog in the database
        List<SuggestionBlog> suggestionBlogList = suggestionBlogRepository.findAll();
        assertThat(suggestionBlogList).hasSize(databaseSizeBeforeCreate + 1);
        SuggestionBlog testSuggestionBlog = suggestionBlogList.get(suggestionBlogList.size() - 1);
        assertThat(testSuggestionBlog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSuggestionBlog.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSuggestionBlog.getCreate()).isEqualTo(DEFAULT_CREATE);
    }

    @Test
    @Transactional
    public void createSuggestionBlogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = suggestionBlogRepository.findAll().size();

        // Create the SuggestionBlog with an existing ID
        suggestionBlog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuggestionBlogMockMvc.perform(post("/api/suggestion-blogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestionBlog)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SuggestionBlog> suggestionBlogList = suggestionBlogRepository.findAll();
        assertThat(suggestionBlogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestionBlogRepository.findAll().size();
        // set the field null
        suggestionBlog.setTitle(null);

        // Create the SuggestionBlog, which fails.

        restSuggestionBlogMockMvc.perform(post("/api/suggestion-blogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestionBlog)))
            .andExpect(status().isBadRequest());

        List<SuggestionBlog> suggestionBlogList = suggestionBlogRepository.findAll();
        assertThat(suggestionBlogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestionBlogRepository.findAll().size();
        // set the field null
        suggestionBlog.setContent(null);

        // Create the SuggestionBlog, which fails.

        restSuggestionBlogMockMvc.perform(post("/api/suggestion-blogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestionBlog)))
            .andExpect(status().isBadRequest());

        List<SuggestionBlog> suggestionBlogList = suggestionBlogRepository.findAll();
        assertThat(suggestionBlogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuggestionBlogs() throws Exception {
        // Initialize the database
        suggestionBlogRepository.saveAndFlush(suggestionBlog);

        // Get all the suggestionBlogList
        restSuggestionBlogMockMvc.perform(get("/api/suggestion-blogs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suggestionBlog.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].create").value(hasItem(sameInstant(DEFAULT_CREATE))));
    }

    @Test
    @Transactional
    public void getSuggestionBlog() throws Exception {
        // Initialize the database
        suggestionBlogRepository.saveAndFlush(suggestionBlog);

        // Get the suggestionBlog
        restSuggestionBlogMockMvc.perform(get("/api/suggestion-blogs/{id}", suggestionBlog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(suggestionBlog.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.create").value(sameInstant(DEFAULT_CREATE)));
    }

    @Test
    @Transactional
    public void getNonExistingSuggestionBlog() throws Exception {
        // Get the suggestionBlog
        restSuggestionBlogMockMvc.perform(get("/api/suggestion-blogs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuggestionBlog() throws Exception {
        // Initialize the database
        suggestionBlogRepository.saveAndFlush(suggestionBlog);
        int databaseSizeBeforeUpdate = suggestionBlogRepository.findAll().size();

        // Update the suggestionBlog
        SuggestionBlog updatedSuggestionBlog = suggestionBlogRepository.findOne(suggestionBlog.getId());
        updatedSuggestionBlog
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .create(UPDATED_CREATE);

        restSuggestionBlogMockMvc.perform(put("/api/suggestion-blogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSuggestionBlog)))
            .andExpect(status().isOk());

        // Validate the SuggestionBlog in the database
        List<SuggestionBlog> suggestionBlogList = suggestionBlogRepository.findAll();
        assertThat(suggestionBlogList).hasSize(databaseSizeBeforeUpdate);
        SuggestionBlog testSuggestionBlog = suggestionBlogList.get(suggestionBlogList.size() - 1);
        assertThat(testSuggestionBlog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSuggestionBlog.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSuggestionBlog.getCreate()).isEqualTo(UPDATED_CREATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSuggestionBlog() throws Exception {
        int databaseSizeBeforeUpdate = suggestionBlogRepository.findAll().size();

        // Create the SuggestionBlog

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSuggestionBlogMockMvc.perform(put("/api/suggestion-blogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestionBlog)))
            .andExpect(status().isCreated());

        // Validate the SuggestionBlog in the database
        List<SuggestionBlog> suggestionBlogList = suggestionBlogRepository.findAll();
        assertThat(suggestionBlogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSuggestionBlog() throws Exception {
        // Initialize the database
        suggestionBlogRepository.saveAndFlush(suggestionBlog);
        int databaseSizeBeforeDelete = suggestionBlogRepository.findAll().size();

        // Get the suggestionBlog
        restSuggestionBlogMockMvc.perform(delete("/api/suggestion-blogs/{id}", suggestionBlog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SuggestionBlog> suggestionBlogList = suggestionBlogRepository.findAll();
        assertThat(suggestionBlogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuggestionBlog.class);
    }
}
