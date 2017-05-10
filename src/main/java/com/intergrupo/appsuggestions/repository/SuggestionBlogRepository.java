package com.intergrupo.appsuggestions.repository;

import com.intergrupo.appsuggestions.domain.SuggestionBlog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SuggestionBlog entity.
 */
@SuppressWarnings("unused")
public interface SuggestionBlogRepository extends JpaRepository<SuggestionBlog,Long> {

    @Query("select suggestionBlog from SuggestionBlog suggestionBlog where suggestionBlog.author.login = ?#{principal.username}")
    List<SuggestionBlog> findByAuthorIsCurrentUser();

}
