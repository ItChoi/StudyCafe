package org.example.studycafe.admin.application

import org.example.studycafe.admin.infrastructure.AdminRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AdminService(
    private val adminRepository: AdminRepository,
) {
    // TODO: 유스케이스 구현
}
