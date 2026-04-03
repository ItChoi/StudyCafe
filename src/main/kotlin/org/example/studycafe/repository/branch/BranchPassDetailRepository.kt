package org.example.studycafe.repository.branch

import org.example.studycafe.domain.branch.BranchPassDetail
import org.springframework.data.jpa.repository.JpaRepository

interface BranchPassDetailRepository : JpaRepository<BranchPassDetail, Long>
