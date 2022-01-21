package com.covidtracker.myapp.service;

import com.covidtracker.myapp.domain.*; // for static metamodels
import com.covidtracker.myapp.domain.VaccinationInfo;
import com.covidtracker.myapp.repository.VaccinationInfoRepository;
import com.covidtracker.myapp.service.criteria.VaccinationInfoCriteria;
import com.covidtracker.myapp.service.dto.VaccinationInfoDTO;
import com.covidtracker.myapp.service.mapper.VaccinationInfoMapper;
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
 * Service for executing complex queries for {@link VaccinationInfo} entities in the database.
 * The main input is a {@link VaccinationInfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VaccinationInfoDTO} or a {@link Page} of {@link VaccinationInfoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VaccinationInfoQueryService extends QueryService<VaccinationInfo> {

    private final Logger log = LoggerFactory.getLogger(VaccinationInfoQueryService.class);

    private final VaccinationInfoRepository vaccinationInfoRepository;

    private final VaccinationInfoMapper vaccinationInfoMapper;

    public VaccinationInfoQueryService(VaccinationInfoRepository vaccinationInfoRepository, VaccinationInfoMapper vaccinationInfoMapper) {
        this.vaccinationInfoRepository = vaccinationInfoRepository;
        this.vaccinationInfoMapper = vaccinationInfoMapper;
    }

    /**
     * Return a {@link List} of {@link VaccinationInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VaccinationInfoDTO> findByCriteria(VaccinationInfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VaccinationInfo> specification = createSpecification(criteria);
        return vaccinationInfoMapper.toDto(vaccinationInfoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VaccinationInfoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VaccinationInfoDTO> findByCriteria(VaccinationInfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VaccinationInfo> specification = createSpecification(criteria);
        return vaccinationInfoRepository.findAll(specification, page).map(vaccinationInfoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VaccinationInfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VaccinationInfo> specification = createSpecification(criteria);
        return vaccinationInfoRepository.count(specification);
    }

    /**
     * Function to convert {@link VaccinationInfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VaccinationInfo> createSpecification(VaccinationInfoCriteria criteria) {
        Specification<VaccinationInfo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VaccinationInfo_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), VaccinationInfo_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), VaccinationInfo_.name));
            }
            if (criteria.getBirthday() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthday(), VaccinationInfo_.birthday));
            }
            if (criteria.getIdentityCardNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getIdentityCardNumber(), VaccinationInfo_.identityCardNumber));
            }
            if (criteria.getVaccinationNumber() != null) {
                specification = specification.and(buildSpecification(criteria.getVaccinationNumber(), VaccinationInfo_.vaccinationNumber));
            }
            if (criteria.getFirstVaccinationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getFirstVaccinationDate(), VaccinationInfo_.firstVaccinationDate));
            }
            if (criteria.getSecondVaccinationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSecondVaccinationDate(), VaccinationInfo_.secondVaccinationDate));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildSpecification(criteria.getCountry(), VaccinationInfo_.country));
            }
            if (criteria.getAdress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdress(), VaccinationInfo_.adress));
            }
        }
        return specification;
    }
}
