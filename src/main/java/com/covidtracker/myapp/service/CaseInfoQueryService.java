package com.covidtracker.myapp.service;

import com.covidtracker.myapp.domain.*; // for static metamodels
import com.covidtracker.myapp.domain.CaseInfo;
import com.covidtracker.myapp.repository.CaseInfoRepository;
import com.covidtracker.myapp.service.criteria.CaseInfoCriteria;
import com.covidtracker.myapp.service.dto.CaseInfoDTO;
import com.covidtracker.myapp.service.mapper.CaseInfoMapper;
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
 * Service for executing complex queries for {@link CaseInfo} entities in the database.
 * The main input is a {@link CaseInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CaseInfoDTO} or a {@link Page} of {@link CaseInfoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CaseInfoQueryService extends QueryService<CaseInfo> {

    private final Logger log = LoggerFactory.getLogger(CaseInfoQueryService.class);

    private final CaseInfoRepository caseInfoRepository;

    private final CaseInfoMapper caseInfoMapper;

    public CaseInfoQueryService(CaseInfoRepository caseInfoRepository, CaseInfoMapper caseInfoMapper) {
        this.caseInfoRepository = caseInfoRepository;
        this.caseInfoMapper = caseInfoMapper;
    }

    /**
     * Return a {@link List} of {@link CaseInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CaseInfoDTO> findByCriteria(CaseInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CaseInfo> specification = createSpecification(criteria);
        return caseInfoMapper.toDto(caseInfoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CaseInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CaseInfoDTO> findByCriteria(CaseInfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CaseInfo> specification = createSpecification(criteria);
        return caseInfoRepository.findAll(specification, page).map(caseInfoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CaseInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CaseInfo> specification = createSpecification(criteria);
        return caseInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link CaseInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CaseInfo> createSpecification(CaseInfoCriteria criteria) {
        Specification<CaseInfo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CaseInfo_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), CaseInfo_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CaseInfo_.name));
            }
            if (criteria.getBirthday() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthday(), CaseInfo_.birthday));
            }
            if (criteria.getTestResult() != null) {
                specification = specification.and(buildSpecification(criteria.getTestResult(), CaseInfo_.testResult));
            }
            if (criteria.getTestDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTestDate(), CaseInfo_.testDate));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildSpecification(criteria.getCountry(), CaseInfo_.country));
            }
            if (criteria.getAdress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdress(), CaseInfo_.adress));
            }
        }
        return specification;
    }
}
