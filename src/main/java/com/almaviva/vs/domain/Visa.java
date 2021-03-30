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
 * A Visa.
 */
@Entity
@Table(name = "visa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "visa")
public class Visa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Double price;

    @Column(name = "description")
    private String description;

    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "visa")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appointment", "site", "services", "visa" }, allowSetters = true)
    private Set<Folder> folders = new HashSet<>();

    @ManyToMany(mappedBy = "visas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "visas" }, allowSetters = true)
    private Set<VisaDocuments> documents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Visa id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Visa title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return this.price;
    }

    public Visa price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public Visa description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Visa deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Folder> getFolders() {
        return this.folders;
    }

    public Visa folders(Set<Folder> folders) {
        this.setFolders(folders);
        return this;
    }

    public Visa addFolder(Folder folder) {
        this.folders.add(folder);
        folder.setVisa(this);
        return this;
    }

    public Visa removeFolder(Folder folder) {
        this.folders.remove(folder);
        folder.setVisa(null);
        return this;
    }

    public void setFolders(Set<Folder> folders) {
        if (this.folders != null) {
            this.folders.forEach(i -> i.setVisa(null));
        }
        if (folders != null) {
            folders.forEach(i -> i.setVisa(this));
        }
        this.folders = folders;
    }

    public Set<VisaDocuments> getDocuments() {
        return this.documents;
    }

    public Visa documents(Set<VisaDocuments> visaDocuments) {
        this.setDocuments(visaDocuments);
        return this;
    }

    public Visa addDocument(VisaDocuments visaDocuments) {
        this.documents.add(visaDocuments);
        visaDocuments.getVisas().add(this);
        return this;
    }

    public Visa removeDocument(VisaDocuments visaDocuments) {
        this.documents.remove(visaDocuments);
        visaDocuments.getVisas().remove(this);
        return this;
    }

    public void setDocuments(Set<VisaDocuments> visaDocuments) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.removeVisa(this));
        }
        if (visaDocuments != null) {
            visaDocuments.forEach(i -> i.addVisa(this));
        }
        this.documents = visaDocuments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Visa)) {
            return false;
        }
        return id != null && id.equals(((Visa) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Visa{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", description='" + getDescription() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
