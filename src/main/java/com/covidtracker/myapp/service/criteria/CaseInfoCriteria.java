package com.covidtracker.myapp.service.criteria;

import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import com.covidtracker.myapp.domain.enumeration.EnumTestResult;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.covidtracker.myapp.domain.CaseInfo} entity. This class is used
 * in {@link com.covidtracker.myapp.web.rest.CaseInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /case-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CaseInfoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumTestResult
     */
    public static class EnumTestResultFilter extends Filter<EnumTestResult> {

        public EnumTestResultFilter() {}

        public EnumTestResultFilter(EnumTestResultFilter filter) {
            super(filter);
        }

        @Override
        public EnumTestResultFilter copy() {
            return new EnumTestResultFilter(this);
        }
    }

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

    private StringFilter name;

    private LocalDateFilter birthday;

    private EnumTestResultFilter testResult;

    private ZonedDateTimeFilter testDate;

    private EnumCountryFilter country;

    private StringFilter adress;

    private Boolean distinct;

    public CaseInfoCriteria() {}

    public CaseInfoCriteria(CaseInfoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.birthday = other.birthday == null ? null : other.birthday.copy();
        this.testResult = other.testResult == null ? null : other.testResult.copy();
        this.testDate = other.testDate == null ? null : other.testDate.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.adress = other.adress == null ? null : other.adress.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CaseInfoCriteria copy() {
        return new CaseInfoCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LocalDateFilter getBirthday() {
        return birthday;
    }

    public LocalDateFilter birthday() {
        if (birthday == null) {
            birthday = new LocalDateFilter();
        }
        return birthday;
    }

    public void setBirthday(LocalDateFilter birthday) {
        this.birthday = birthday;
    }

    public EnumTestResultFilter getTestResult() {
        return testResult;
    }

    public EnumTestResultFilter testResult() {
        if (testResult == null) {
            testResult = new EnumTestResultFilter();
        }
        return testResult;
    }

    public void setTestResult(EnumTestResultFilter testResult) {
        this.testResult = testResult;
    }

    public ZonedDateTimeFilter getTestDate() {
        return testDate;
    }

    public ZonedDateTimeFilter testDate() {
        if (testDate == null) {
            testDate = new ZonedDateTimeFilter();
        }
        return testDate;
    }

    public void setTestDate(ZonedDateTimeFilter testDate) {
        this.testDate = testDate;
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

    public StringFilter getAdress() {
        return adress;
    }

    public StringFilter adress() {
        if (adress == null) {
            adress = new StringFilter();
        }
        return adress;
    }

    public void setAdress(StringFilter adress) {
        this.adress = adress;
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
        final CaseInfoCriteria that = (CaseInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(name, that.name) &&
            Objects.equals(birthday, that.birthday) &&
            Objects.equals(testResult, that.testResult) &&
            Objects.equals(testDate, that.testDate) &&
            Objects.equals(country, that.country) &&
            Objects.equals(adress, that.adress) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, name, birthday, testResult, testDate, country, adress, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CaseInfoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (birthday != null ? "birthday=" + birthday + ", " : "") +
            (testResult != null ? "testResult=" + testResult + ", " : "") +
            (testDate != null ? "testDate=" + testDate + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (adress != null ? "adress=" + adress + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
