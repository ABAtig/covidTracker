package com.covidtracker.myapp.service.dto;

import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import com.covidtracker.myapp.domain.enumeration.EnumTestResult;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.covidtracker.myapp.domain.CaseInfo} entity.
 */
public class CaseInfoDTO implements Serializable {

    private Long id;

    /**
     * UUID of positive case of covid
     */
    @NotNull
    @ApiModelProperty(value = "UUID of positive case of covid", required = true)
    private UUID uuid;

    /**
     * Name of case
     */
    @NotNull
    @ApiModelProperty(value = "Name of case", required = true)
    private String name;

    /**
     * Date of birthday of case
     */
    @ApiModelProperty(value = "Date of birthday of case")
    private LocalDate birthday;

    /**
     * Result of test of covid
     */
    @NotNull
    @ApiModelProperty(value = "Result of test of covid", required = true)
    private EnumTestResult testResult;

    /**
     * Date of test of covid
     */
    @ApiModelProperty(value = "Date of test of covid")
    private ZonedDateTime testDate;

    /**
     * Country of covid case
     */
    @NotNull
    @ApiModelProperty(value = "Country of covid case", required = true)
    private EnumCountry country;

    /**
     * Adress of covid case
     */
    @ApiModelProperty(value = "Adress of covid case")
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

    public EnumTestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(EnumTestResult testResult) {
        this.testResult = testResult;
    }

    public ZonedDateTime getTestDate() {
        return testDate;
    }

    public void setTestDate(ZonedDateTime testDate) {
        this.testDate = testDate;
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
        if (!(o instanceof CaseInfoDTO)) {
            return false;
        }

        CaseInfoDTO caseInfoDTO = (CaseInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, caseInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaseInfoDTO{" +
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
