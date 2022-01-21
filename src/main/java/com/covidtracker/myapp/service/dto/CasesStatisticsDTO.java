package com.covidtracker.myapp.service.dto;

import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.covidtracker.myapp.domain.CasesStatistics} entity.
 */
public class CasesStatisticsDTO implements Serializable {

    private Long id;

    /**
     * UUID du mobile stats
     */
    @NotNull
    @ApiModelProperty(value = "UUID du mobile stats", required = true)
    private UUID uuid;

    /**
     * Total Number Cases
     */
    @ApiModelProperty(value = "Total Number Cases")
    private Double totalCasesNumber;

    /**
     * duration by bike
     */
    @NotNull
    @ApiModelProperty(value = "duration by bike", required = true)
    private EnumCountry country;

    /**
     * Date/heure
     */
    @NotNull
    @ApiModelProperty(value = "Date/heure", required = true)
    private LocalDate date;

    /**
     * année
     */
    @ApiModelProperty(value = "année")
    private Integer year;

    /**
     * mois
     */
    @ApiModelProperty(value = "mois")
    private Integer month;

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

    public Double getTotalCasesNumber() {
        return totalCasesNumber;
    }

    public void setTotalCasesNumber(Double totalCasesNumber) {
        this.totalCasesNumber = totalCasesNumber;
    }

    public EnumCountry getCountry() {
        return country;
    }

    public void setCountry(EnumCountry country) {
        this.country = country;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CasesStatisticsDTO)) {
            return false;
        }

        CasesStatisticsDTO casesStatisticsDTO = (CasesStatisticsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, casesStatisticsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CasesStatisticsDTO{" +
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
