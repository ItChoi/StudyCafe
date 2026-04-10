package org.example.studycafe.member.presentation.response

import org.example.studycafe.member.domain.Member

data class AuthResponse(
    val token: String,
    val member: MemberResponse,
) {
    companion object {
        fun of(token: String, member: Member) = AuthResponse(
            token = token,
            member = MemberResponse.from(member),
        )
    }
}
