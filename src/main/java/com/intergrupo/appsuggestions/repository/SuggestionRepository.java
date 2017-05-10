package com.intergrupo.appsuggestions.repository;

import com.intergrupo.appsuggestions.domain.Suggestion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Suggestion entity.
 */
@SuppressWarnings("unused")
public interface SuggestionRepository extends JpaRepository<Suggestion,Long> {

    @Query("select suggestion from Suggestion suggestion where suggestion.addressee.login = ?#{principal.username}")
    List<Suggestion> findByAddresseeIsCurrentUser();

    @Query("select suggestion from Suggestion suggestion where suggestion.author.login = ?#{principal.username}")
    List<Suggestion> findByAuthorIsCurrentUser();

}
