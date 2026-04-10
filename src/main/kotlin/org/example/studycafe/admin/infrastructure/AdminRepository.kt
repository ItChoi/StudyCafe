package org.example.studycafe.admin.infrastructure

import org.example.studycafe.admin.domain.Admin
import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository : JpaRepository<Admin, Long>
