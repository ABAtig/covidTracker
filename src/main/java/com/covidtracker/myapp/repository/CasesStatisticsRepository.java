package com.covidtracker.myapp.repository;

import com.covidtracker.myapp.domain.CasesStatistics;
import com.covidtracker.myapp.domain.enumeration.EnumCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the CasesStatistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CasesStatisticsRepository extends JpaRepository<CasesStatistics, Long>, JpaSpecificationExecutor<CasesStatistics> {

    Optional<CasesStatistics> findByCountryAndDate(EnumCountry country, LocalDate date);

    @Query("SELECT SUM(cs.totalCasesNumber), cs.country, cs.date, cs.month, cs.year from CasesStatistics AS cs WHERE cs.date =:date GROUP BY cs.date, cs.month, cs.year, cs.country")
    List<Object> findByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(cs.totalCasesNumber),  cs.country, MAX(cs.date), cs.month, cs.year from CasesStatistics AS cs WHERE cs.month =:month GROUP BY cs.month, cs.country, cs.year")
    List<Object> findByMonth(@Param("month")Integer month);

    @Query("SELECT SUM(cs.totalCasesNumber), cs.country, MAX(cs.date), MAX(cs.month), cs.year from CasesStatistics AS cs WHERE cs.year =:year GROUP BY cs.year, cs.country")
    List<Object> findByYear(@Param("year") Integer year);


}
