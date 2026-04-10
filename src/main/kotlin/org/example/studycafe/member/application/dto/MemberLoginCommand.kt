package org.example.studycafe.member.application.dto

data class MemberLoginCommand(
    val nickname: String,
    val password: String,
)
