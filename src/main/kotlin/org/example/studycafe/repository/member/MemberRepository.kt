package org.example.studycafe.repository.member

import org.example.studycafe.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>
