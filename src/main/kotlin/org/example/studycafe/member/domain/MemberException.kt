package org.example.studycafe.member.domain

class MemberException(message: String) : RuntimeException(message)

object MemberExceptionMessage {
    const val DUPLICATE_NICKNAME = "이미 사용 중인 닉네임입니다."
    const val MEMBER_NOT_FOUND = "존재하지 않는 회원입니다."
    const val INVALID_PASSWORD = "닉네임 또는 비밀번호가 올바르지 않습니다."
}
