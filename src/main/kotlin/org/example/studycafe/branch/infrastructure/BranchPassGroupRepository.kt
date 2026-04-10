package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.domain.BranchPassGroup
import org.springframework.data.jpa.repository.JpaRepository

interface BranchPassGroupRepository : JpaRepository<BranchPassGroup, Long>
