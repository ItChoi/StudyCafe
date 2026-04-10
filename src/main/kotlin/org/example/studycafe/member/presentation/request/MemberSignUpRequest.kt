package org.example.studycafe.member.presentation.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.example.studycafe.common.enumeration.GenderType
import org.example.studycafe.member.application.dto.MemberSignUpCommand

data class MemberSignUpRequest(
    @field:NotBlank(message = "닉네임은 필수입니다.")
    val nickname: String,

    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val password: String,

    @field:NotNull(message = "성별은 필수입니다.")
    val gender: GenderType,
) {
    fun toCommand() = MemberSignUpCommand(
        nickname = nickname,
        password = password,
        gender = gender,
    )
}
