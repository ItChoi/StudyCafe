package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.domain.BranchPass
import org.springframework.data.jpa.repository.JpaRepository

interface BranchPassRepository : JpaRepository<BranchPass, Long>
