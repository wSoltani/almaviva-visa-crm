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
 * A Folder.
 */
@Entity
@Table(name = "folder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "folder")
public class Folder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "folder_id")
    private Long folderId;

    @Column(name = "status")
    private String status;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "waiting_room")
    private String waitingRoom;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "is_avs_free")
    private Boolean isAvsFree;

    @Column(name = "deleted")
    private Boolean deleted;

    @OneToOne
    @JoinColumn(unique = true)
    private Appointment appointment;

    @ManyToOne
    @JsonIgnoreProperties(value = { "siteConfigurations", "siteServices" }, allowSetters = true)
    private Site site;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_folder__service",
        joinColumns = @JoinColumn(name = "folder_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @JsonIgnoreProperties(value = { "site", "folders" }, allowSetters = true)
    private Set<AVService> services = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "folders", "documents" }, allowSetters = true)
    private Visa visa;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Folder id(Long id) {
        this.id = id;
        return this;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public Folder folderId(Long folderId) {
        this.folderId = folderId;
        return this;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getStatus() {
        return this.status;
    }

    public Folder status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public Folder paymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getWaitingRoom() {
        return this.waitingRoom;
    }

    public Folder waitingRoom(String waitingRoom) {
        this.waitingRoom = waitingRoom;
        return this;
    }

    public void setWaitingRoom(String waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    public String getServiceType() {
        return this.serviceType;
    }

    public Folder serviceType(String serviceType) {
        this.serviceType = serviceType;
        return this;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Boolean getIsAvsFree() {
        return this.isAvsFree;
    }

    public Folder isAvsFree(Boolean isAvsFree) {
        this.isAvsFree = isAvsFree;
        return this;
    }

    public void setIsAvsFree(Boolean isAvsFree) {
        this.isAvsFree = isAvsFree;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Folder deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Appointment getAppointment() {
        return this.appointment;
    }

    public Folder appointment(Appointment appointment) {
        this.setAppointment(appointment);
        return this;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Site getSite() {
        return this.site;
    }

    public Folder site(Site site) {
        this.setSite(site);
        return this;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Set<AVService> getServices() {
        return this.services;
    }

    public Folder services(Set<AVService> aVServices) {
        this.setServices(aVServices);
        return this;
    }

    public Folder addService(AVService aVService) {
        this.services.add(aVService);
        aVService.getFolders().add(this);
        return this;
    }

    public Folder removeService(AVService aVService) {
        this.services.remove(aVService);
        aVService.getFolders().remove(this);
        return this;
    }

    public void setServices(Set<AVService> aVServices) {
        this.services = aVServices;
    }

    public Visa getVisa() {
        return this.visa;
    }

    public Folder visa(Visa visa) {
        this.setVisa(visa);
        return this;
    }

    public void setVisa(Visa visa) {
        this.visa = visa;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Folder)) {
            return false;
        }
        return id != null && id.equals(((Folder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Folder{" +
            "id=" + getId() +
            ", folderId=" + getFolderId() +
            ", status='" + getStatus() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", waitingRoom='" + getWaitingRoom() + "'" +
            ", serviceType='" + getServiceType() + "'" +
            ", isAvsFree='" + getIsAvsFree() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
