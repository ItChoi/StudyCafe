package org.example.studycafe.member.infrastructure

import org.example.studycafe.member.domain.MemberBranchPass
import org.springframework.data.jpa.repository.JpaRepository

interface MemberBranchPassRepository : JpaRepository<MemberBranchPass, Long>
