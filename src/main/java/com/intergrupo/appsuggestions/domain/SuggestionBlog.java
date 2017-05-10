package com.intergrupo.appsuggestions.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A SuggestionBlog.
 */
@Entity
@Table(name = "suggestion_blog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SuggestionBlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "jhi_create")
    private ZonedDateTime create;

    @ManyToOne(optional = false)
    @NotNull
    private User author;

    @ManyToOne(optional = false)
    @NotNull
    private Category category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public SuggestionBlog title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public SuggestionBlog content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreate() {
        return create;
    }

    public SuggestionBlog create(ZonedDateTime create) {
        this.create = create;
        return this;
    }

    public void setCreate(ZonedDateTime create) {
        this.create = create;
    }

    public User getAuthor() {
        return author;
    }

    public SuggestionBlog author(User user) {
        this.author = user;
        return this;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    public Category getCategory() {
        return category;
    }

    public SuggestionBlog category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuggestionBlog suggestionBlog = (SuggestionBlog) o;
        if (suggestionBlog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, suggestionBlog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SuggestionBlog{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", content='" + content + "'" +
            ", create='" + create + "'" +
            '}';
    }
}
