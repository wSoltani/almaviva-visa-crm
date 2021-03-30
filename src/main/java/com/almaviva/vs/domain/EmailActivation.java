package com.almaviva.vs.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A EmailActivation.
 */
@Entity
@Table(name = "email_activation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "emailactivation")
public class EmailActivation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_activated")
    private Boolean isActivated;

    @Column(name = "activation_key")
    private String activationKey;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "deleted")
    private Boolean deleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmailActivation id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getIsActivated() {
        return this.isActivated;
    }

    public EmailActivation isActivated(Boolean isActivated) {
        this.isActivated = isActivated;
        return this;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    public String getActivationKey() {
        return this.activationKey;
    }

    public EmailActivation activationKey(String activationKey) {
        this.activationKey = activationKey;
        return this;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public EmailActivation expirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public EmailActivation deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailActivation)) {
            return false;
        }
        return id != null && id.equals(((EmailActivation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailActivation{" +
            "id=" + getId() +
            ", isActivated='" + getIsActivated() + "'" +
            ", activationKey='" + getActivationKey() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
