package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.domain.BranchSeatGroup
import org.springframework.data.jpa.repository.JpaRepository

interface BranchSeatGroupRepository : JpaRepository<BranchSeatGroup, Long>
