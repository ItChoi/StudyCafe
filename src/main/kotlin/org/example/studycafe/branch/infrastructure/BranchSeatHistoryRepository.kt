package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.domain.BranchSeatHistory
import org.springframework.data.jpa.repository.JpaRepository

interface BranchSeatHistoryRepository : JpaRepository<BranchSeatHistory, Long>
