package org.example.studycafe.repository.branch

import org.example.studycafe.domain.branch.BranchSeat
import org.springframework.data.jpa.repository.JpaRepository

interface BranchSeatRepository : JpaRepository<BranchSeat, Long>
