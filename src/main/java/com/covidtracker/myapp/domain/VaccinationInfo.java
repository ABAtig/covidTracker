package com.covidtracker.myapp.domain;

import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import com.covidtracker.myapp.domain.enumeration.EnumVaccinationNumber;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A VaccinationInfo.
 */
@Entity
@Table(name = "vaccination_info")
public class VaccinationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * UUID of vaccinated person
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
     * Date of birthday of vaccinated person
     */
    @Column(name = "birthday")
    private LocalDate birthday;

    /**
     * Identity carte number of vaccinated person
     */
    @Column(name = "identity_card_number")
    private String identityCardNumber;

    /**
     * number of vaccination of vaccinated person
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "vaccination_number", nullable = false)
    private EnumVaccinationNumber vaccinationNumber;

    /**
     * Date of first vaccination
     */
    @NotNull
    @Column(name = "first_vaccination_date", nullable = false)
    private ZonedDateTime firstVaccinationDate;

    /**
     * Date of second vaccination
     */
    @Column(name = "second_vaccination_date")
    private ZonedDateTime secondVaccinationDate;

    /**
     * Country of vaccinated person
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "country", nullable = false)
    private EnumCountry country;

    /**
     * Adress of vaccinated person
     */
    @Column(name = "adress")
    private String adress;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VaccinationInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public VaccinationInfo uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public VaccinationInfo name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public VaccinationInfo birthday(LocalDate birthday) {
        this.setBirthday(birthday);
        return this;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getIdentityCardNumber() {
        return this.identityCardNumber;
    }

    public VaccinationInfo identityCardNumber(String identityCardNumber) {
        this.setIdentityCardNumber(identityCardNumber);
        return this;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public EnumVaccinationNumber getVaccinationNumber() {
        return this.vaccinationNumber;
    }

    public VaccinationInfo vaccinationNumber(EnumVaccinationNumber vaccinationNumber) {
        this.setVaccinationNumber(vaccinationNumber);
        return this;
    }

    public void setVaccinationNumber(EnumVaccinationNumber vaccinationNumber) {
        this.vaccinationNumber = vaccinationNumber;
    }

    public ZonedDateTime getFirstVaccinationDate() {
        return this.firstVaccinationDate;
    }

    public VaccinationInfo firstVaccinationDate(ZonedDateTime firstVaccinationDate) {
        this.setFirstVaccinationDate(firstVaccinationDate);
        return this;
    }

    public void setFirstVaccinationDate(ZonedDateTime firstVaccinationDate) {
        this.firstVaccinationDate = firstVaccinationDate;
    }

    public ZonedDateTime getSecondVaccinationDate() {
        return this.secondVaccinationDate;
    }

    public VaccinationInfo secondVaccinationDate(ZonedDateTime secondVaccinationDate) {
        this.setSecondVaccinationDate(secondVaccinationDate);
        return this;
    }

    public void setSecondVaccinationDate(ZonedDateTime secondVaccinationDate) {
        this.secondVaccinationDate = secondVaccinationDate;
    }

    public EnumCountry getCountry() {
        return this.country;
    }

    public VaccinationInfo country(EnumCountry country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(EnumCountry country) {
        this.country = country;
    }

    public String getAdress() {
        return this.adress;
    }

    public VaccinationInfo adress(String adress) {
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
        if (!(o instanceof VaccinationInfo)) {
            return false;
        }
        return id != null && id.equals(((VaccinationInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccinationInfo{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", identityCardNumber='" + getIdentityCardNumber() + "'" +
            ", vaccinationNumber='" + getVaccinationNumber() + "'" +
            ", firstVaccinationDate='" + getFirstVaccinationDate() + "'" +
            ", secondVaccinationDate='" + getSecondVaccinationDate() + "'" +
            ", country='" + getCountry() + "'" +
            ", adress='" + getAdress() + "'" +
            "}";
    }
}
