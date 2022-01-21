package com.covidtracker.myapp.repository;

import com.covidtracker.myapp.domain.CaseInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CaseInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaseInfoRepository extends JpaRepository<CaseInfo, Long>, JpaSpecificationExecutor<CaseInfo> {}
