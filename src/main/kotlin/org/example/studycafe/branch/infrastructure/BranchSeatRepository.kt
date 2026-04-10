package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.domain.BranchSeat
import org.springframework.data.jpa.repository.JpaRepository

interface BranchSeatRepository : JpaRepository<BranchSeat, Long>
