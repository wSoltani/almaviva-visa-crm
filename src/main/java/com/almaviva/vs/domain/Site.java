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
 * A Site.
 */
@Entity
@Table(name = "site")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "site")
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "address")
    private String address;

    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(mappedBy = "site")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "site" }, allowSetters = true)
    private Set<SiteConfiguration> siteConfigurations = new HashSet<>();

    @OneToMany(mappedBy = "site")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "site", "folders" }, allowSetters = true)
    private Set<AVService> siteServices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Site id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Site name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public Site imgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAddress() {
        return this.address;
    }

    public Site address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Site deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<SiteConfiguration> getSiteConfigurations() {
        return this.siteConfigurations;
    }

    public Site siteConfigurations(Set<SiteConfiguration> siteConfigurations) {
        this.setSiteConfigurations(siteConfigurations);
        return this;
    }

    public Site addSiteConfiguration(SiteConfiguration siteConfiguration) {
        this.siteConfigurations.add(siteConfiguration);
        siteConfiguration.setSite(this);
        return this;
    }

    public Site removeSiteConfiguration(SiteConfiguration siteConfiguration) {
        this.siteConfigurations.remove(siteConfiguration);
        siteConfiguration.setSite(null);
        return this;
    }

    public void setSiteConfigurations(Set<SiteConfiguration> siteConfigurations) {
        if (this.siteConfigurations != null) {
            this.siteConfigurations.forEach(i -> i.setSite(null));
        }
        if (siteConfigurations != null) {
            siteConfigurations.forEach(i -> i.setSite(this));
        }
        this.siteConfigurations = siteConfigurations;
    }

    public Set<AVService> getSiteServices() {
        return this.siteServices;
    }

    public Site siteServices(Set<AVService> aVServices) {
        this.setSiteServices(aVServices);
        return this;
    }

    public Site addSiteService(AVService aVService) {
        this.siteServices.add(aVService);
        aVService.setSite(this);
        return this;
    }

    public Site removeSiteService(AVService aVService) {
        this.siteServices.remove(aVService);
        aVService.setSite(null);
        return this;
    }

    public void setSiteServices(Set<AVService> aVServices) {
        if (this.siteServices != null) {
            this.siteServices.forEach(i -> i.setSite(null));
        }
        if (aVServices != null) {
            aVServices.forEach(i -> i.setSite(this));
        }
        this.siteServices = aVServices;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Site)) {
            return false;
        }
        return id != null && id.equals(((Site) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Site{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", address='" + getAddress() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
