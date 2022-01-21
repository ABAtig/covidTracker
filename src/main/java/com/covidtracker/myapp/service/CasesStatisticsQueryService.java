package com.covidtracker.myapp.service;

import com.covidtracker.myapp.domain.*; // for static metamodels
import com.covidtracker.myapp.domain.CasesStatistics;
import com.covidtracker.myapp.repository.CasesStatisticsRepository;
import com.covidtracker.myapp.service.criteria.CasesStatisticsCriteria;
import com.covidtracker.myapp.service.dto.CasesStatisticsDTO;
import com.covidtracker.myapp.service.mapper.CasesStatisticsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CasesStatistics} entities in the database.
 * The main input is a {@link CasesStatisticsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CasesStatisticsDTO} or a {@link Page} of {@link CasesStatisticsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CasesStatisticsQueryService extends QueryService<CasesStatistics> {

    private final Logger log = LoggerFactory.getLogger(CasesStatisticsQueryService.class);

    private final CasesStatisticsRepository casesStatisticsRepository;

    private final CasesStatisticsMapper casesStatisticsMapper;

    public CasesStatisticsQueryService(CasesStatisticsRepository casesStatisticsRepository, CasesStatisticsMapper casesStatisticsMapper) {
        this.casesStatisticsRepository = casesStatisticsRepository;
        this.casesStatisticsMapper = casesStatisticsMapper;
    }

    /**
     * Return a {@link List} of {@link CasesStatisticsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CasesStatisticsDTO> findByCriteria(CasesStatisticsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CasesStatistics> specification = createSpecification(criteria);
        return casesStatisticsMapper.toDto(casesStatisticsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CasesStatisticsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CasesStatisticsDTO> findByCriteria(CasesStatisticsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CasesStatistics> specification = createSpecification(criteria);
        return casesStatisticsRepository.findAll(specification, page).map(casesStatisticsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CasesStatisticsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CasesStatistics> specification = createSpecification(criteria);
        return casesStatisticsRepository.count(specification);
    }

    /**
     * Function to convert {@link CasesStatisticsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CasesStatistics> createSpecification(CasesStatisticsCriteria criteria) {
        Specification<CasesStatistics> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CasesStatistics_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), CasesStatistics_.uuid));
            }
            if (criteria.getTotalCasesNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTotalCasesNumber(), CasesStatistics_.totalCasesNumber));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildSpecification(criteria.getCountry(), CasesStatistics_.country));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), CasesStatistics_.date));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), CasesStatistics_.year));
            }
            if (criteria.getMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonth(), CasesStatistics_.month));
            }
        }
        return specification;
    }
}
