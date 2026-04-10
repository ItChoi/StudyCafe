package org.example.studycafe.member.presentation.response

import org.example.studycafe.common.enumeration.GenderType
import org.example.studycafe.member.domain.Member
import java.time.OffsetDateTime

data class MemberResponse(
    val id: Long,
    val nickname: String,
    val gender: GenderType,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?,
) {
    companion object {
        fun from(member: Member) = MemberResponse(
            id = requireNotNull(member.id),
            nickname = member.nickname,
            gender = member.gender,
            createdAt = member.createdDtm,
            updatedAt = member.updatedDtm,
        )
    }
}
