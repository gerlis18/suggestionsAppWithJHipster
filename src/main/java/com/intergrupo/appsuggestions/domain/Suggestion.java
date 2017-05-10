package com.intergrupo.appsuggestions.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Suggestion.
 */
@Entity
@Table(name = "suggestion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Suggestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "jhi_create")
    private ZonedDateTime create;

    @ManyToOne(optional = false)
    @NotNull
    private User addressee;

    @ManyToOne
    private User author;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Suggestion title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public Suggestion message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getCreate() {
        return create;
    }

    public Suggestion create(ZonedDateTime create) {
        this.create = create;
        return this;
    }

    public void setCreate(ZonedDateTime create) {
        this.create = create;
    }

    public User getAddressee() {
        return addressee;
    }

    public Suggestion addressee(User user) {
        this.addressee = user;
        return this;
    }

    public void setAddressee(User user) {
        this.addressee = user;
    }

    public User getAuthor() {
        return author;
    }

    public Suggestion author(User user) {
        this.author = user;
        return this;
    }

    public void setAuthor(User user) {
        this.author = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Suggestion suggestion = (Suggestion) o;
        if (suggestion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, suggestion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Suggestion{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", message='" + message + "'" +
            ", create='" + create + "'" +
            '}';
    }
}
