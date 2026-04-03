package org.example.studycafe.repository.branch

import org.example.studycafe.domain.branch.BranchBusinessHourException
import org.springframework.data.jpa.repository.JpaRepository

interface BranchBusinessHourExceptionRepository : JpaRepository<BranchBusinessHourException, Long>
