package org.example.studycafe.repository.branch

import org.example.studycafe.domain.branch.BranchSeatGroup
import org.springframework.data.jpa.repository.JpaRepository

interface BranchSeatGroupRepository : JpaRepository<BranchSeatGroup, Long>
