package org.example.studycafe.repository.branch

import org.example.studycafe.domain.branch.Branch
import org.springframework.data.jpa.repository.JpaRepository

interface BranchRepository : JpaRepository<Branch, Long>
