package org.example.studycafe.admin.infrastructure

import org.example.studycafe.admin.domain.BranchAdmin
import org.springframework.data.jpa.repository.JpaRepository

interface BranchAdminRepository : JpaRepository<BranchAdmin, Long>
