package org.example.studycafe.member.presentation.request

import jakarta.validation.constraints.NotBlank

// TODO: 회원 생성 요청 DTO 구현
data class MemberCreateRequest(
    @field:NotBlank
    val nickname: String,
)
