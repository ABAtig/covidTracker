package com.covidtracker.myapp.service.criteria;

import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link com.covidtracker.myapp.domain.CasesStatistics} entity. This class is used
 * in {@link com.covidtracker.myapp.web.rest.CasesStatisticsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cases-statistics?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CasesStatisticsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumCountry
     */
    public static class EnumCountryFilter extends Filter<EnumCountry> {

        public EnumCountryFilter() {}

        public EnumCountryFilter(EnumCountryFilter filter) {
            super(filter);
        }

        @Override
        public EnumCountryFilter copy() {
            return new EnumCountryFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private DoubleFilter totalCasesNumber;

    private EnumCountryFilter country;

    private LocalDateFilter date;

    private IntegerFilter year;

    private IntegerFilter month;

    private Boolean distinct;

    public CasesStatisticsCriteria() {}

    public CasesStatisticsCriteria(CasesStatisticsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.totalCasesNumber = other.totalCasesNumber == null ? null : other.totalCasesNumber.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.year = other.year == null ? null : other.year.copy();
        this.month = other.month == null ? null : other.month.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CasesStatisticsCriteria copy() {
        return new CasesStatisticsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UUIDFilter getUuid() {
        return uuid;
    }

    public UUIDFilter uuid() {
        if (uuid == null) {
            uuid = new UUIDFilter();
        }
        return uuid;
    }

    public void setUuid(UUIDFilter uuid) {
        this.uuid = uuid;
    }

    public DoubleFilter getTotalCasesNumber() {
        return totalCasesNumber;
    }

    public DoubleFilter totalCasesNumber() {
        if (totalCasesNumber == null) {
            totalCasesNumber = new DoubleFilter();
        }
        return totalCasesNumber;
    }

    public void setTotalCasesNumber(DoubleFilter totalCasesNumber) {
        this.totalCasesNumber = totalCasesNumber;
    }

    public EnumCountryFilter getCountry() {
        return country;
    }

    public EnumCountryFilter country() {
        if (country == null) {
            country = new EnumCountryFilter();
        }
        return country;
    }

    public void setCountry(EnumCountryFilter country) {
        this.country = country;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public IntegerFilter year() {
        if (year == null) {
            year = new IntegerFilter();
        }
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public IntegerFilter getMonth() {
        return month;
    }

    public IntegerFilter month() {
        if (month == null) {
            month = new IntegerFilter();
        }
        return month;
    }

    public void setMonth(IntegerFilter month) {
        this.month = month;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CasesStatisticsCriteria that = (CasesStatisticsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(totalCasesNumber, that.totalCasesNumber) &&
            Objects.equals(country, that.country) &&
            Objects.equals(date, that.date) &&
            Objects.equals(year, that.year) &&
            Objects.equals(month, that.month) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, totalCasesNumber, country, date, year, month, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CasesStatisticsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (totalCasesNumber != null ? "totalCasesNumber=" + totalCasesNumber + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (year != null ? "year=" + year + ", " : "") +
            (month != null ? "month=" + month + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
