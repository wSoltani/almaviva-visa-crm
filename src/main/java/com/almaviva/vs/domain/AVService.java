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
 * A AVService.
 */
@Entity
@Table(name = "av_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "avservice")
public class AVService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "is_principal")
    private Boolean isPrincipal;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne
    @JsonIgnoreProperties(value = { "siteConfigurations", "siteServices" }, allowSetters = true)
    private Site site;

    @ManyToMany(mappedBy = "services")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appointment", "site", "services", "visa" }, allowSetters = true)
    private Set<Folder> folders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AVService id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public AVService title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public AVService description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return this.price;
    }

    public AVService price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public AVService quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsPrincipal() {
        return this.isPrincipal;
    }

    public AVService isPrincipal(Boolean isPrincipal) {
        this.isPrincipal = isPrincipal;
        return this;
    }

    public void setIsPrincipal(Boolean isPrincipal) {
        this.isPrincipal = isPrincipal;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public AVService deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Site getSite() {
        return this.site;
    }

    public AVService site(Site site) {
        this.setSite(site);
        return this;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Set<Folder> getFolders() {
        return this.folders;
    }

    public AVService folders(Set<Folder> folders) {
        this.setFolders(folders);
        return this;
    }

    public AVService addFolder(Folder folder) {
        this.folders.add(folder);
        folder.getServices().add(this);
        return this;
    }

    public AVService removeFolder(Folder folder) {
        this.folders.remove(folder);
        folder.getServices().remove(this);
        return this;
    }

    public void setFolders(Set<Folder> folders) {
        if (this.folders != null) {
            this.folders.forEach(i -> i.removeService(this));
        }
        if (folders != null) {
            folders.forEach(i -> i.addService(this));
        }
        this.folders = folders;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AVService)) {
            return false;
        }
        return id != null && id.equals(((AVService) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AVService{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", isPrincipal='" + getIsPrincipal() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
