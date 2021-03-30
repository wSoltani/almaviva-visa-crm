package com.almaviva.vs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A SiteConfiguration.
 */
@Entity
@Table(name = "site_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "siteconfiguration")
public class SiteConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "opening_time")
    private String openingTime;

    @Column(name = "closing_time")
    private String closingTime;

    @Column(name = "appointment_time")
    private Integer appointmentTime;

    @Column(name = "appointment_quota")
    private Integer appointmentQuota;

    @Column(name = "appointment_quota_web")
    private Integer appointmentQuotaWeb;

    @Column(name = "information")
    private String information;

    @Column(name = "daily_message")
    private String dailyMessage;

    @Column(name = "prestation_margin")
    private Integer prestationMargin;

    @Column(name = "simultaneous")
    private Integer simultaneous;

    @Column(name = "is_specific")
    private Boolean isSpecific;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne
    @JsonIgnoreProperties(value = { "siteConfigurations", "siteServices" }, allowSetters = true)
    private Site site;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SiteConfiguration id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public SiteConfiguration startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public SiteConfiguration endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getOpeningTime() {
        return this.openingTime;
    }

    public SiteConfiguration openingTime(String openingTime) {
        this.openingTime = openingTime;
        return this;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return this.closingTime;
    }

    public SiteConfiguration closingTime(String closingTime) {
        this.closingTime = closingTime;
        return this;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public Integer getAppointmentTime() {
        return this.appointmentTime;
    }

    public SiteConfiguration appointmentTime(Integer appointmentTime) {
        this.appointmentTime = appointmentTime;
        return this;
    }

    public void setAppointmentTime(Integer appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Integer getAppointmentQuota() {
        return this.appointmentQuota;
    }

    public SiteConfiguration appointmentQuota(Integer appointmentQuota) {
        this.appointmentQuota = appointmentQuota;
        return this;
    }

    public void setAppointmentQuota(Integer appointmentQuota) {
        this.appointmentQuota = appointmentQuota;
    }

    public Integer getAppointmentQuotaWeb() {
        return this.appointmentQuotaWeb;
    }

    public SiteConfiguration appointmentQuotaWeb(Integer appointmentQuotaWeb) {
        this.appointmentQuotaWeb = appointmentQuotaWeb;
        return this;
    }

    public void setAppointmentQuotaWeb(Integer appointmentQuotaWeb) {
        this.appointmentQuotaWeb = appointmentQuotaWeb;
    }

    public String getInformation() {
        return this.information;
    }

    public SiteConfiguration information(String information) {
        this.information = information;
        return this;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getDailyMessage() {
        return this.dailyMessage;
    }

    public SiteConfiguration dailyMessage(String dailyMessage) {
        this.dailyMessage = dailyMessage;
        return this;
    }

    public void setDailyMessage(String dailyMessage) {
        this.dailyMessage = dailyMessage;
    }

    public Integer getPrestationMargin() {
        return this.prestationMargin;
    }

    public SiteConfiguration prestationMargin(Integer prestationMargin) {
        this.prestationMargin = prestationMargin;
        return this;
    }

    public void setPrestationMargin(Integer prestationMargin) {
        this.prestationMargin = prestationMargin;
    }

    public Integer getSimultaneous() {
        return this.simultaneous;
    }

    public SiteConfiguration simultaneous(Integer simultaneous) {
        this.simultaneous = simultaneous;
        return this;
    }

    public void setSimultaneous(Integer simultaneous) {
        this.simultaneous = simultaneous;
    }

    public Boolean getIsSpecific() {
        return this.isSpecific;
    }

    public SiteConfiguration isSpecific(Boolean isSpecific) {
        this.isSpecific = isSpecific;
        return this;
    }

    public void setIsSpecific(Boolean isSpecific) {
        this.isSpecific = isSpecific;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public SiteConfiguration deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Site getSite() {
        return this.site;
    }

    public SiteConfiguration site(Site site) {
        this.setSite(site);
        return this;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SiteConfiguration)) {
            return false;
        }
        return id != null && id.equals(((SiteConfiguration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteConfiguration{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", openingTime='" + getOpeningTime() + "'" +
            ", closingTime='" + getClosingTime() + "'" +
            ", appointmentTime=" + getAppointmentTime() +
            ", appointmentQuota=" + getAppointmentQuota() +
            ", appointmentQuotaWeb=" + getAppointmentQuotaWeb() +
            ", information='" + getInformation() + "'" +
            ", dailyMessage='" + getDailyMessage() + "'" +
            ", prestationMargin=" + getPrestationMargin() +
            ", simultaneous=" + getSimultaneous() +
            ", isSpecific='" + getIsSpecific() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
