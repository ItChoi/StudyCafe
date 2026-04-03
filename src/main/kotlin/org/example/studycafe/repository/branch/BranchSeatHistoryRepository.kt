package org.example.studycafe.repository.branch

import org.example.studycafe.domain.branch.BranchSeatHistory
import org.springframework.data.jpa.repository.JpaRepository

interface BranchSeatHistoryRepository : JpaRepository<BranchSeatHistory, Long>
