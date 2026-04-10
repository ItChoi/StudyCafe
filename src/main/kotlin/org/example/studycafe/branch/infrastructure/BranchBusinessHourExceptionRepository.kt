package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.domain.BranchBusinessHourException
import org.springframework.data.jpa.repository.JpaRepository

interface BranchBusinessHourExceptionRepository : JpaRepository<BranchBusinessHourException, Long>
