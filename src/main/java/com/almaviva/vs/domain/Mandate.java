package com.almaviva.vs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Mandate.
 */
@Entity
@Table(name = "mandate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "mandate")
public class Mandate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private Integer code;

    @Column(name = "location")
    private String location;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "is_avs_paiment")
    private Boolean isAVSPaiment;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appointment", "site", "services", "visa" }, allowSetters = true)
    private Folder folder;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mandate id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCode() {
        return this.code;
    }

    public Mandate code(Integer code) {
        this.code = code;
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLocation() {
        return this.location;
    }

    public Mandate location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Mandate amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Mandate date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getIsAVSPaiment() {
        return this.isAVSPaiment;
    }

    public Mandate isAVSPaiment(Boolean isAVSPaiment) {
        this.isAVSPaiment = isAVSPaiment;
        return this;
    }

    public void setIsAVSPaiment(Boolean isAVSPaiment) {
        this.isAVSPaiment = isAVSPaiment;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Mandate deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Folder getFolder() {
        return this.folder;
    }

    public Mandate folder(Folder folder) {
        this.setFolder(folder);
        return this;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mandate)) {
            return false;
        }
        return id != null && id.equals(((Mandate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mandate{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", location='" + getLocation() + "'" +
            ", amount=" + getAmount() +
            ", date='" + getDate() + "'" +
            ", isAVSPaiment='" + getIsAVSPaiment() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
