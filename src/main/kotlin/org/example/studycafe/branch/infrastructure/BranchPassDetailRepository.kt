package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.domain.BranchPassDetail
import org.springframework.data.jpa.repository.JpaRepository

interface BranchPassDetailRepository : JpaRepository<BranchPassDetail, Long>
