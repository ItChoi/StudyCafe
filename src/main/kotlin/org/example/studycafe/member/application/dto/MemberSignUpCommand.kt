package org.example.studycafe.member.application.dto

import org.example.studycafe.common.enumeration.GenderType

data class MemberSignUpCommand(
    val nickname: String,
    val password: String,
    val gender: GenderType,
)
