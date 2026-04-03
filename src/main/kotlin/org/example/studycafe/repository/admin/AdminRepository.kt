package org.example.studycafe.repository.admin

import org.example.studycafe.domain.admin.Admin
import org.springframework.data.jpa.repository.JpaRepository

interface AdminRepository : JpaRepository<Admin, Long>
