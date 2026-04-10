package org.example.studycafe.member.presentation.request

import jakarta.validation.constraints.NotBlank
import org.example.studycafe.member.application.dto.MemberLoginCommand

data class MemberLoginRequest(
    @field:NotBlank(message = "닉네임은 필수입니다.")
    val nickname: String,

    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val password: String,
) {
    fun toCommand() = MemberLoginCommand(
        nickname = nickname,
        password = password,
    )
}
