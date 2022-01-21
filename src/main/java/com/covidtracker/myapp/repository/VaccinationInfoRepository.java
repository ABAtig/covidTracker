package com.covidtracker.myapp.repository;

import com.covidtracker.myapp.domain.VaccinationInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VaccinationInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaccinationInfoRepository extends JpaRepository<VaccinationInfo, Long>, JpaSpecificationExecutor<VaccinationInfo> {}
