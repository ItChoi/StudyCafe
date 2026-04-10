package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.domain.BranchBusinessHour
import org.springframework.data.jpa.repository.JpaRepository

interface BranchBusinessHourRepository : JpaRepository<BranchBusinessHour, Long>
