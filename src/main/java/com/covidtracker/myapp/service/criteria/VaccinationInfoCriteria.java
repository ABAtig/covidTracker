package com.covidtracker.myapp.service.criteria;

import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import com.covidtracker.myapp.domain.enumeration.EnumVaccinationNumber;
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
 * Criteria class for the {@link com.covidtracker.myapp.domain.VaccinationInfo} entity. This class is used
 * in {@link com.covidtracker.myapp.web.rest.VaccinationInfoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vaccination-infos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class VaccinationInfoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EnumVaccinationNumber
     */
    public static class EnumVaccinationNumberFilter extends Filter<EnumVaccinationNumber> {

        public EnumVaccinationNumberFilter() {}

        public EnumVaccinationNumberFilter(EnumVaccinationNumberFilter filter) {
            super(filter);
        }

        @Override
        public EnumVaccinationNumberFilter copy() {
            return new EnumVaccinationNumberFilter(this);
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

    private StringFilter identityCardNumber;

    private EnumVaccinationNumberFilter vaccinationNumber;

    private ZonedDateTimeFilter firstVaccinationDate;

    private ZonedDateTimeFilter secondVaccinationDate;

    private EnumCountryFilter country;

    private StringFilter adress;

    private Boolean distinct;

    public VaccinationInfoCriteria() {}

    public VaccinationInfoCriteria(VaccinationInfoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.birthday = other.birthday == null ? null : other.birthday.copy();
        this.identityCardNumber = other.identityCardNumber == null ? null : other.identityCardNumber.copy();
        this.vaccinationNumber = other.vaccinationNumber == null ? null : other.vaccinationNumber.copy();
        this.firstVaccinationDate = other.firstVaccinationDate == null ? null : other.firstVaccinationDate.copy();
        this.secondVaccinationDate = other.secondVaccinationDate == null ? null : other.secondVaccinationDate.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.adress = other.adress == null ? null : other.adress.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VaccinationInfoCriteria copy() {
        return new VaccinationInfoCriteria(this);
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

    public StringFilter getIdentityCardNumber() {
        return identityCardNumber;
    }

    public StringFilter identityCardNumber() {
        if (identityCardNumber == null) {
            identityCardNumber = new StringFilter();
        }
        return identityCardNumber;
    }

    public void setIdentityCardNumber(StringFilter identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public EnumVaccinationNumberFilter getVaccinationNumber() {
        return vaccinationNumber;
    }

    public EnumVaccinationNumberFilter vaccinationNumber() {
        if (vaccinationNumber == null) {
            vaccinationNumber = new EnumVaccinationNumberFilter();
        }
        return vaccinationNumber;
    }

    public void setVaccinationNumber(EnumVaccinationNumberFilter vaccinationNumber) {
        this.vaccinationNumber = vaccinationNumber;
    }

    public ZonedDateTimeFilter getFirstVaccinationDate() {
        return firstVaccinationDate;
    }

    public ZonedDateTimeFilter firstVaccinationDate() {
        if (firstVaccinationDate == null) {
            firstVaccinationDate = new ZonedDateTimeFilter();
        }
        return firstVaccinationDate;
    }

    public void setFirstVaccinationDate(ZonedDateTimeFilter firstVaccinationDate) {
        this.firstVaccinationDate = firstVaccinationDate;
    }

    public ZonedDateTimeFilter getSecondVaccinationDate() {
        return secondVaccinationDate;
    }

    public ZonedDateTimeFilter secondVaccinationDate() {
        if (secondVaccinationDate == null) {
            secondVaccinationDate = new ZonedDateTimeFilter();
        }
        return secondVaccinationDate;
    }

    public void setSecondVaccinationDate(ZonedDateTimeFilter secondVaccinationDate) {
        this.secondVaccinationDate = secondVaccinationDate;
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
        final VaccinationInfoCriteria that = (VaccinationInfoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(name, that.name) &&
            Objects.equals(birthday, that.birthday) &&
            Objects.equals(identityCardNumber, that.identityCardNumber) &&
            Objects.equals(vaccinationNumber, that.vaccinationNumber) &&
            Objects.equals(firstVaccinationDate, that.firstVaccinationDate) &&
            Objects.equals(secondVaccinationDate, that.secondVaccinationDate) &&
            Objects.equals(country, that.country) &&
            Objects.equals(adress, that.adress) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            uuid,
            name,
            birthday,
            identityCardNumber,
            vaccinationNumber,
            firstVaccinationDate,
            secondVaccinationDate,
            country,
            adress,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VaccinationInfoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (birthday != null ? "birthday=" + birthday + ", " : "") +
            (identityCardNumber != null ? "identityCardNumber=" + identityCardNumber + ", " : "") +
            (vaccinationNumber != null ? "vaccinationNumber=" + vaccinationNumber + ", " : "") +
            (firstVaccinationDate != null ? "firstVaccinationDate=" + firstVaccinationDate + ", " : "") +
            (secondVaccinationDate != null ? "secondVaccinationDate=" + secondVaccinationDate + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (adress != null ? "adress=" + adress + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
