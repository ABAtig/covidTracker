package com.covidtracker.myapp.service.dto;

import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import com.covidtracker.myapp.domain.enumeration.EnumVaccinationNumber;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.covidtracker.myapp.domain.VaccinationInfo} entity.
 */
public class VaccinationInfoDTO implements Serializable {

    private Long id;

    /**
     * UUID of vaccinated person
     */
    @NotNull
    @ApiModelProperty(value = "UUID of vaccinated person", required = true)
    private UUID uuid;

    /**
     * Name of case
     */
    @NotNull
    @ApiModelProperty(value = "Name of case", required = true)
    private String name;

    /**
     * Date of birthday of vaccinated person
     */
    @ApiModelProperty(value = "Date of birthday of vaccinated person")
    private LocalDate birthday;

    /**
     * Identity carte number of vaccinated person
     */
    @ApiModelProperty(value = "Identity carte number of vaccinated person")
    private String identityCardNumber;

    /**
     * number of vaccination of vaccinated person
     */
    @NotNull
    @ApiModelProperty(value = "number of vaccination of vaccinated person", required = true)
    private EnumVaccinationNumber vaccinationNumber;

    /**
     * Date of first vaccination
     */
    @NotNull
    @ApiModelProperty(value = "Date of first vaccination", required = true)
    private ZonedDateTime firstVaccinationDate;

    /**
     * Date of second vaccination
     */
    @ApiModelProperty(value = "Date of second vaccination")
    private ZonedDateTime secondVaccinationDate;

    /**
     * Country of vaccinated person
     */
    @NotNull
    @ApiModelProperty(value = "Country of vaccinated person", required = true)
    private EnumCountry country;

    /**
     * Adress of vaccinated person
     */
    @ApiModelProperty(value = "Adress of vaccinated person")
    private String adress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public EnumVaccinationNumber getVaccinationNumber() {
        return vaccinationNumber;
    }

    public void setVaccinationNumber(EnumVaccinationNumber vaccinationNumber) {
        this.vaccinationNumber = vaccinationNumber;
    }

    public ZonedDateTime getFirstVaccinationDate() {
        return firstVaccinationDate;
    }

    public void setFirstVaccinationDate(ZonedDateTime firstVaccinationDate) {
        this.firstVaccinationDate = firstVaccinationDate;
    }

    public ZonedDateTime getSecondVaccinationDate() {
        return secondVaccinationDate;
    }

    public void setSecondVaccinationDate(ZonedDateTime secondVaccinationDate) {
        this.secondVaccinationDate = secondVaccinationDate;
    }

    public EnumCountry getCountry() {
        return country;
    }

    public void setCountry(EnumCountry country) {
        this.country = country;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VaccinationInfoDTO)) {
            return false;
        }

        VaccinationInfoDTO vaccinationInfoDTO = (VaccinationInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vaccinationInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccinationInfoDTO{" +
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
