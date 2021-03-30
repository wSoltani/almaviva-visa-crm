package com.almaviva.vs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A VisaDocuments.
 */
@Entity
@Table(name = "visa_documents")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "visadocuments")
public class VisaDocuments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_visa_documents__visa",
        joinColumns = @JoinColumn(name = "visa_documents_id"),
        inverseJoinColumns = @JoinColumn(name = "visa_id")
    )
    @JsonIgnoreProperties(value = { "folders", "documents" }, allowSetters = true)
    private Set<Visa> visas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VisaDocuments id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public VisaDocuments title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public VisaDocuments description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public VisaDocuments deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Visa> getVisas() {
        return this.visas;
    }

    public VisaDocuments visas(Set<Visa> visas) {
        this.setVisas(visas);
        return this;
    }

    public VisaDocuments addVisa(Visa visa) {
        this.visas.add(visa);
        visa.getDocuments().add(this);
        return this;
    }

    public VisaDocuments removeVisa(Visa visa) {
        this.visas.remove(visa);
        visa.getDocuments().remove(this);
        return this;
    }

    public void setVisas(Set<Visa> visas) {
        this.visas = visas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VisaDocuments)) {
            return false;
        }
        return id != null && id.equals(((VisaDocuments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VisaDocuments{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
