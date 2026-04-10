package org.example.studycafe.admin.presentation

import org.example.studycafe.admin.application.AdminService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admins")
class AdminController(
    private val adminService: AdminService,
) {
    // TODO: 엔드포인트 구현
}
