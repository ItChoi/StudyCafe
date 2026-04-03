package org.example.studycafe.repository.branch

import org.example.studycafe.domain.branch.BranchBusinessHour
import org.springframework.data.jpa.repository.JpaRepository

interface BranchBusinessHourRepository : JpaRepository<BranchBusinessHour, Long>
