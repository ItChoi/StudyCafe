package org.example.studycafe.member.infrastructure

import org.example.studycafe.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>, MemberRepositoryCustom {
    fun findByNickname(nickname: String): Member?
    fun existsByNickname(nickname: String): Boolean
}
