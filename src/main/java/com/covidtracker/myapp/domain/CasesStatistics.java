package com.covidtracker.myapp.domain;

import com.covidtracker.myapp.domain.enumeration.EnumCountry;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CasesStatistics.
 */
@Entity
@Table(name = "cases_statistics")
public class CasesStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * UUID du mobile stats
     */
    @NotNull
    @Column(name = "uuid", nullable = false, unique = true)
    private UUID uuid;

    /**
     * Total Number Cases
     */
    @Column(name = "total_cases_number")
    private Double totalCasesNumber;

    /**
     * duration by bike
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "country", nullable = false)
    private EnumCountry country;

    /**
     * Date/heure
     */
    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    /**
     * année
     */
    @Column(name = "year")
    private Integer year;

    /**
     * mois
     */
    @Column(name = "month")
    private Integer month;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CasesStatistics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public CasesStatistics uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Double getTotalCasesNumber() {
        return this.totalCasesNumber;
    }

    public CasesStatistics totalCasesNumber(Double totalCasesNumber) {
        this.setTotalCasesNumber(totalCasesNumber);
        return this;
    }

    public void setTotalCasesNumber(Double totalCasesNumber) {
        this.totalCasesNumber = totalCasesNumber;
    }

    public EnumCountry getCountry() {
        return this.country;
    }

    public CasesStatistics country(EnumCountry country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(EnumCountry country) {
        this.country = country;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public CasesStatistics date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getYear() {
        return this.year;
    }

    public CasesStatistics year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return this.month;
    }

    public CasesStatistics month(Integer month) {
        this.setMonth(month);
        return this;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }


    @PrePersist
    private void generatorUuid() {
        this.setUuid(UUID.randomUUID());
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CasesStatistics)) {
            return false;
        }
        return id != null && id.equals(((CasesStatistics) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CasesStatistics{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", totalCasesNumber=" + getTotalCasesNumber() +
            ", country='" + getCountry() + "'" +
            ", date='" + getDate() + "'" +
            ", year=" + getYear() +
            ", month=" + getMonth() +
            "}";
    }
}
