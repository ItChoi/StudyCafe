package org.example.studycafe.repository.branch

import org.example.studycafe.domain.branch.BranchPassGroup
import org.springframework.data.jpa.repository.JpaRepository

interface BranchPassGroupRepository : JpaRepository<BranchPassGroup, Long>
