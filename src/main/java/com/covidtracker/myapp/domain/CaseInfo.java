package com.covidtracker.myapp.domain;

import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import com.covidtracker.myapp.domain.enumeration.EnumTestResult;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CaseInfo.
 */
@Entity
@Table(name = "case_info")
public class CaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * UUID of positive case of covid
     */
    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    /**
     * Name of case
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Date of birthday of case
     */
    @Column(name = "birthday")
    private LocalDate birthday;

    /**
     * Result of test of covid
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "test_result", nullable = false)
    private EnumTestResult testResult;

    /**
     * Date of test of covid
     */
    @Column(name = "test_date")
    private ZonedDateTime testDate;

    /**
     * Country of covid case
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "country", nullable = false)
    private EnumCountry country;

    /**
     * Adress of covid case
     */
    @Column(name = "adress")
    private String adress;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CaseInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public CaseInfo uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public CaseInfo name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public CaseInfo birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public EnumTestResult getTestResult() {
        return this.testResult;
    }

    public CaseInfo testResult(EnumTestResult testResult) {
        this.setTestResult(testResult);
        return this;
    }

    public void setTestResult(EnumTestResult testResult) {
        this.testResult = testResult;
    }

    public ZonedDateTime getTestDate() {
        return this.testDate;
    }

    public CaseInfo testDate(ZonedDateTime testDate) {
        this.setTestDate(testDate);
        return this;
    }

    public void setTestDate(ZonedDateTime testDate) {
        this.testDate = testDate;
    }

    public EnumCountry getCountry() {
        return this.country;
    }

    public CaseInfo country(EnumCountry country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(EnumCountry country) {
        this.country = country;
    }

    public String getAdress() {
        return this.adress;
    }

    public CaseInfo adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @PrePersist
    private void generatorUuid() {
        this.setUuid(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CaseInfo)) {
            return false;
        }
        return id != null && id.equals(((CaseInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaseInfo{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", testResult='" + getTestResult() + "'" +
            ", testDate='" + getTestDate() + "'" +
            ", country='" + getCountry() + "'" +
            ", adress='" + getAdress() + "'" +
            "}";
    }
}
