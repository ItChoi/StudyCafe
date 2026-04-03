package org.example.studycafe.repository.branch

import org.example.studycafe.domain.branch.BranchPass
import org.springframework.data.jpa.repository.JpaRepository

interface BranchPassRepository : JpaRepository<BranchPass, Long>
