package org.example.studycafe.repository.admin

import org.example.studycafe.domain.admin.BranchAdmin
import org.springframework.data.jpa.repository.JpaRepository

interface BranchAdminRepository : JpaRepository<BranchAdmin, Long>
